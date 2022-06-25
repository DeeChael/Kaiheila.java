package net.deechael.khl.hook.source.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.client.http.HttpCall;
import net.deechael.khl.client.http.HttpHeaders;
import net.deechael.khl.client.http.HttpResponseBody;
import net.deechael.khl.client.http.IHttpClient;
import net.deechael.khl.client.ws.IWebSocketClient;
import net.deechael.khl.client.ws.IWebSocketContext;
import net.deechael.khl.configurer.KaiheilaConfiguration;
import net.deechael.khl.hook.EventManager;
import net.deechael.khl.hook.EventSource;
import net.deechael.khl.hook.source.EventSourceStringListener;
import net.deechael.khl.hook.source.websocket.session.storage.WebSocketSessionStorage;
import net.deechael.khl.restful.RestRoute;
import net.deechael.khl.util.compression.Compression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class WebSocketEventSource extends EventSource implements EventSourceStringListener {

    protected static final Logger Log = LoggerFactory.getLogger(WebSocketEventSource.class);

    protected final KaiheilaBot rabbit;
    protected final IHttpClient httpClient;
    protected final IWebSocketClient websocketClient;
    protected final Compression compression;
    protected final WebSocketSessionStorage sessionStorage;
    protected final ObjectMapper jsonEngine;
    protected Thread restartThread;

    protected IWebSocketContext websocketContext;
    protected WebSocketEventSourceSession session;
    protected String url;
    protected WebSocketState state;
    protected LocalDateTime pingTime;
    protected LocalDateTime pongTime;
    protected int pingRetryTimes;
    protected Thread senderThread;

    private int failedRetry = 0;

    public WebSocketEventSource(EventManager manager, KaiheilaBot rabbit, IHttpClient httpClient, IWebSocketClient websocketClient, ObjectMapper jsonEngine, Compression compression, WebSocketSessionStorage sessionStorage) {
        super(manager);
        this.rabbit = rabbit;
        this.httpClient = httpClient;
        this.websocketClient = websocketClient;
        this.compression = compression;
        this.sessionStorage = sessionStorage;
        this.jsonEngine = jsonEngine;
    }

    private void resetEventSourceState() {
        this.websocketContext = null;
        this.session = null;
        this.url = null;
        this.state = WebSocketState.UNKNOWN;
        this.pingTime = null;
        this.pongTime = null;
        this.pingRetryTimes = 0;
        this.senderThread = null;
    }

    protected void setCurrentState(WebSocketState state) {
        if (this.state != state) {
            if (KaiheilaConfiguration.isDebug) Log.trace("Websocket 状态 {} 切换至 {}", this.state, state);
            this.state = state;
        }
    }

    private void saveSession() {
        if (sessionStorage.saveSession(this.session)) {
            if (KaiheilaConfiguration.isDebug) Log.warn("WebSocket session 保存成功");
        } else {
            Log.warn("WebSocket session 保存失败");
        }
    }

    private boolean parseHelloSession(JsonNode data) {
        int code = data.get("code").asInt();
        if (code != 0) {
            return false;
        }
        if (this.session == null) {
            this.session = new WebSocketEventSourceSession();
            String sessionId = data.get("session_id").asText();
            this.session.setSessionId(sessionId);
            this.session.setGateway(url);
        }
        return true;
    }

    protected void restartWebSocket(boolean failed) {
        if (this.state != WebSocketState.RESTARTING) {
            if (KaiheilaConfiguration.isDebug) Log.trace("{} 进入重启函数", Thread.currentThread().getName());
            this.setCurrentState(WebSocketState.RESTARTING);
            if (failed) {
                Log.warn("因内部运行异常重新连接，当前为第 {} 次发生异常", ++failedRetry);
            }
            // 使用新线程重启，避免 WebSocketReceiverThread 与 shutdownCurrentService 函数进入死锁
            if (this.restartThread == null) {
                this.restartThread = new Thread(() -> {
                    if (KaiheilaConfiguration.isDebug) Log.trace("WebSocket 重启线程启动");
                    this.shutdownCurrentService();
                    Log.warn("WebSocket 3秒后重新连接");
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        return;
                    }
                    this.openConnection();
                    this.restartThread = null;
                }, "RestartWebSocketEventSource");
                this.restartThread.start();
            }
        }
    }

    private synchronized void shutdownCurrentService() {
        Log.warn("Sender/Receiver 线程等待退出");
        if (this.senderThread != null) {
            this.senderThread.interrupt();
            try {
                this.senderThread.join();
                this.senderThread = null;
            } catch (InterruptedException ignored) {
            }
        }
        if (KaiheilaConfiguration.isDebug) Log.trace("Sender 线程完成关闭");
        if (this.websocketContext != null) {
            this.websocketContext.closeWebSocket(1000, "User Shutdown Service");
            this.websocketContext.await();
            this.websocketContext = null;
        }
        if (KaiheilaConfiguration.isDebug) Log.trace("Receiver 线程完成关闭");
        Log.warn("Sender/Receiver 线程已经退出");
    }

    private String requestGatewayUrl() throws IOException {
        RestRoute.CompiledRoute route = RestRoute.Misc.GATEWAY.compile();
        String url = rabbit.getConfiguration().getApiConfigurer().getBaseUrl() + route.getQueryStringCompleteRoute();
        HttpHeaders headers = new HttpHeaders();
        headers.addHeader("Authorization", "Bot " + rabbit.getConfiguration().getApiConfigurer().getToken());
        HttpCall request = HttpCall.createRequest(route.getMethod(), url, headers, null);
        HttpCall.Response response;
        response = httpClient.execute(request);
        HttpResponseBody responseBody = response.getResponseBody();
        if (responseBody != null) {
            JsonNode root = jsonEngine.readTree(responseBody.getBuffer().array());
            return root.get("data").get("url").asText();
        }
        return null;
    }

    private String getNewGateway() {
        int retryCount = 0;
        int retryTimeDelay;
        do {
            try {
                String gatewayUrl = requestGatewayUrl();
                if (gatewayUrl != null) {
                    return gatewayUrl;
                }
            } catch (IOException ignored) {
            }
            if (++retryCount > 5) {
                retryTimeDelay = 60;
            } else {
                retryTimeDelay = 1 << retryCount;
            }
            Log.error("获取Gateway地址失败，{} 秒后开始 {} 次重试", retryTimeDelay, retryCount);
            try {
                TimeUnit.SECONDS.sleep(retryTimeDelay);
            } catch (InterruptedException ignored) {
            }
        } while (true);
    }

    private String getCurrentGateway() {
        setCurrentState(WebSocketState.FETCH_GATEWAY);
        if (this.session == null) {
            super.manager.initialSn(0);
            return getNewGateway();
        } else {
            if (KaiheilaConfiguration.isDebug)
                Log.debug("使用重连地址 Session: {}, sn:{}", this.session.getSessionId(), this.session.getSn());
            super.manager.initialSn(this.session.getSn());
            return this.session.getReconnectUrl();
        }
    }

    private void openConnection() {
        this.resetEventSourceState();
        this.setCurrentState(WebSocketState.INITIALIZING);
        this.session = sessionStorage.getSession();
        String connectUrl;
        if (this.session == null) {
            connectUrl = this.url = getCurrentGateway();
        } else {
            this.url = this.session.getGateway();
            connectUrl = getCurrentGateway();
        }
        this.setCurrentState(WebSocketState.CONNECT);
        this.websocketContext = websocketClient.openWebSocket(connectUrl, new WebSocketEventSourceHandle(this));
    }

    @Override
    public String transfer(String message) {
        JsonNode root;
        try {
            root = jsonEngine.readTree(message);
        } catch (JsonProcessingException ignored) {
            return null;
        }
        int s = root.get("s").asInt();
        JsonNode data = root.get("d");
        if (s == WebSocketEventSourceSignaling.EVENT.getType()) {
            int latestSn = root.get("sn").asInt();
            if (super.paused) {
                session.setSn(latestSn);
            } else {
                int latestProcessSn = super.manager.process(latestSn, data.toString());
                session.setSn(latestProcessSn);
            }
            this.saveSession();
        } else if (s == WebSocketEventSourceSignaling.HELLO.getType()) {
            boolean helloSucceed = parseHelloSession(data);
            if (helloSucceed) {
                this.setCurrentState(WebSocketState.ESTABLISHED);
            } else {
                this.restartWebSocket(false);
                this.setCurrentState(WebSocketState.FAILED);
            }
        } else if (s == WebSocketEventSourceSignaling.PONG.getType()) {
            this.pingRetryTimes = 0;
            this.pongTime = LocalDateTime.now();
            this.setCurrentState(WebSocketState.ESTABLISHED);
        } else if (s == WebSocketEventSourceSignaling.RECONNECT.getType()) {
            this.setCurrentState(WebSocketState.RECONNECT);
            super.disableEventPipe();
            super.manager.resetMessageQueue();
            this.session.reset();
            this.sessionStorage.clearSession();
            this.restartWebSocket(false);
        } else if (s == WebSocketEventSourceSignaling.RESUME_ACK.getType()) {
            this.setCurrentState(WebSocketState.ESTABLISHED);
        }
        return null;
    }

    @Override
    public void start() {
        if (this.websocketContext == null) {
            this.openConnection();
        }
    }

    @Override
    public void shutdown() {
        this.shutdownCurrentService();
        this.resetEventSourceState();
        this.failedRetry = 0;
    }
}
