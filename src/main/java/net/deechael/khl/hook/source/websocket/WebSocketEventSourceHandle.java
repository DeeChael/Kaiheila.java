package net.deechael.khl.hook.source.websocket;

import net.deechael.khl.client.ws.IWebSocketContext;
import net.deechael.khl.client.ws.IWebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class WebSocketEventSourceHandle extends IWebSocketListener implements Runnable {
    protected static final Logger Log = LoggerFactory.getLogger(WebSocketEventSourceHandle.class);

    private final WebSocketEventSource eventSource;
    private IWebSocketContext client;

    public WebSocketEventSourceHandle(WebSocketEventSource eventSource) {
        this.eventSource = eventSource;
        this.eventSource.senderThread = new Thread(this, "WebSocketSenderThread");
        this.eventSource.senderThread.start();
    }

    @Override
    public void onOpen(IWebSocketContext client) {
        this.client = client;
        this.eventSource.setCurrentState(WebSocketState.WAIT_HELLO);
        this.eventSource.pingTime = this.eventSource.pongTime = LocalDateTime.now();
        LockSupport.unpark(this.eventSource.senderThread);
    }

    @Override
    public void onTextMessage(IWebSocketContext client, String text) {
        eventSource.transfer(text);
    }

    @Override
    public void onBinaryMessage(IWebSocketContext client, ByteBuffer bytes) {
        ByteBuffer buffer;
        try {
            buffer = eventSource.compression.decompress(bytes);
        } catch (IOException ignored) {
            return;
        }
        eventSource.transfer(new String(buffer.array(), StandardCharsets.UTF_8));
    }

    @Override
    public void onFailure(IWebSocketContext client, Throwable t) {
        if (this.catchException(t, EOFException.class)) {
            this.eventSource.setCurrentState(WebSocketState.SOCKET_FAILED);
            Log.error("WebSocket 连接被远程服务器终止");
            this.eventSource.restartWebSocket(true);
        } else if (this.catchException(t, SocketException.class)) {
            this.eventSource.setCurrentState(WebSocketState.SOCKET_FAILED);
            Log.error("WebSocket 网络连接发生错误");
            this.eventSource.restartWebSocket(true);
        } else if (this.catchException(t, ProtocolException.class)) {
            this.eventSource.setCurrentState(WebSocketState.SOCKET_FAILED);
            Log.error("Websocket 内部协议处理错误");
            this.eventSource.restartWebSocket(true);
        } else {
            Log.error("Websocket 发生未知错误", t);
            this.eventSource.restartWebSocket(true);
        }
    }

    private boolean catchException(Throwable t, Class<? extends Throwable> tClass) {
        Throwable throwable = t;
        do {
            if (tClass.isAssignableFrom(throwable.getClass())) {
                return true;
            }
        }
        while ((throwable = throwable.getCause()) != null);
        return false;
    }

    private long pingDelay() {
        long duration = Duration.between(this.eventSource.pingTime, LocalDateTime.now()).getSeconds();
        long delay = 0;
        if (this.eventSource.state == WebSocketState.ESTABLISHED) {
            delay = 25 - duration;
        } else if (this.eventSource.state == WebSocketState.PONG_TIMEOUT) {
            delay = (1L << ++this.eventSource.pingRetryTimes) - duration;
        }
        delay = delay < 0 ? 0 : delay;
        Log.debug("下次发送 PING 数据包剩余 {} 秒", delay);
        return delay;
    }

    private boolean receiveTimeout() throws InterruptedException {
        if (this.eventSource.state == WebSocketState.WAIT_HELLO) {
            TimeUnit.SECONDS.sleep(6);
            return this.eventSource.state != WebSocketState.ESTABLISHED;
        } else if (this.eventSource.state == WebSocketState.ESTABLISHED) {
            TimeUnit.SECONDS.sleep(6);
            return (Duration.between(this.eventSource.pingTime, this.eventSource.pongTime).getSeconds() > 6);
        }
        return false;
    }

    private void sendReportPing() throws InterruptedException {
        String ping = "{\"s\": 2, \"sn\": " + this.eventSource.session.getSn() + "}";
        Log.debug("当前发送 PING SN 为 {}", this.eventSource.session.getSn());
        this.client.sendTextMessage(ping);
        this.eventSource.pingTime = LocalDateTime.now();
        if (this.receiveTimeout()) {
            this.eventSource.setCurrentState(WebSocketState.PONG_TIMEOUT);
        }
    }

    private boolean receiveHelloTimeout() {
        boolean timeout;
        try {
            timeout = this.receiveTimeout();
        } catch (InterruptedException e) {
            return true;
        }
        if (timeout) {
            this.eventSource.setCurrentState(WebSocketState.HELLO_TIMEOUT);
        }
        return timeout;
    }

    @Override
    public void run() {
        LockSupport.park();
        if (this.receiveHelloTimeout()) {
            Log.debug("{} 已关闭", Thread.currentThread().getName());
            return;
        }
        while (!this.client.isClosed() && !Thread.currentThread().isInterrupted()) {
            try {
                TimeUnit.SECONDS.sleep(this.pingDelay());
                this.sendReportPing();
            } catch (InterruptedException ignored) {
                Log.debug("{} 被关闭", Thread.currentThread().getName());
                break;
            }
        }
        Log.debug("{} 已关闭", Thread.currentThread().getName());
    }
}