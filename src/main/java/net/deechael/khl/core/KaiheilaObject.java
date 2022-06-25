package net.deechael.khl.core;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.Bot;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.client.http.HttpCall;
import net.deechael.khl.client.http.HttpHeaders;
import net.deechael.khl.restful.RestPageable;
import net.deechael.khl.restful.RestRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class KaiheilaObject {

    protected static final Logger Log = LoggerFactory.getLogger(KaiheilaObject.class);

    private final KaiheilaBot kaiheilaBot;
    protected final HttpHeaders defaultHeaders = new HttpHeaders();

    public KaiheilaObject(KaiheilaBot kaiheilaBot) {
        this.kaiheilaBot = kaiheilaBot;
        if (kaiheilaBot.getConfiguration() != null) {
            defaultHeaders.addHeader("Authorization", "Bot " + getKaiheilaBot().getConfiguration().getApiConfigurer().getToken());
        }
    }

    protected KaiheilaBot getKaiheilaBot() {
        return kaiheilaBot;
    }

    public Bot getBot() {
        return kaiheilaBot;
    }


    protected List<JsonNode> getRestJsonResponse(RestRoute.CompiledRoute compiledRoute, HttpCall call) throws InterruptedException {
        ArrayList<JsonNode> result = new ArrayList<>();
        JsonNode root = callRestApi(call);
        if (root == null) {
            Log.error("Not set the root");
            return null;
        }
        result.add(root);
        result.addAll(getRemainPageRestData(compiledRoute, getRestApiData(root)));
        return result;
    }

    private List<JsonNode> getRemainPageRestData(RestRoute.CompiledRoute compiledRoute, JsonNode data) throws InterruptedException {
        ArrayList<JsonNode> result = new ArrayList<>();
        RestPageable pageable = RestPageable.of(getKaiheilaBot(), compiledRoute, data);
        while (pageable.hasNext()) {
            RestRoute.CompiledRoute nextRoute = pageable.next();
            HttpCall nextCall = HttpCall.createRequest(nextRoute.getMethod(), getCompleteUrl(nextRoute), this.defaultHeaders);
            JsonNode next = callRestApi(nextCall);
            if (next == null) {
                continue;
            }
            result.add(next);
        }
        return result;
    }

    private JsonNode getRestApiData(JsonNode node) {
        return node.get("data");
    }

    protected String getCompleteUrl(RestRoute.CompiledRoute route) {
        return getKaiheilaBot().getConfiguration().getApiConfigurer().getBaseUrl() + route.getQueryStringCompleteRoute();
    }

    protected JsonNode callRestApi(HttpCall call) throws InterruptedException {
        JsonNode root = null;
        boolean callFailed;
        int callRetry = 0;
        HttpCall.Response response;
        do {
            try {
                response = getKaiheilaBot().getHttpClient().execute(call);
                if (response.getCode() != 200) {
                    reportRequestFailed(++callRetry, call.getRequest().getUrl());
                    callFailed = true;
                    continue;
                }
                root = getKaiheilaBot().getJsonEngine().readTree(response.getResponseBody().getBuffer().array());
            } catch (IOException e) {
                reportRequestFailed(++callRetry, call.getRequest().getUrl());
                callFailed = true;
                continue;
            }
            callFailed = false;
        } while (callFailed && callRetry != 3);
        if (callFailed || hasRestApiError(root)) {
            return null;
        }
        return root;
    }

    protected void reportRequestFailed(int retryCount, String url) throws InterruptedException {
        if (Log.isWarnEnabled()) {
            Log.warn("[数据同步] 获取数据失败，3秒后第{}次重试, [{}]", retryCount, url);
        }
        TimeUnit.SECONDS.sleep(3);
    }

    protected boolean hasRestApiError(JsonNode root) {
        if (root == null) {
            return true;
        }
        if (!root.has("code")) {
            return true;
        }
        if (root.get("code").asInt() != 0) {
            Log.error("[数据同步] API请求失败，错误码：{}, 错误消息{}", root.get("code").asInt(), root.get("message").asText());
            return true;
        }
        return false;
    }

    protected boolean handleResult(JsonNode res) {
        return res != null && res.get("code").asInt() == 0;
    }

    protected HttpHeaders getDefaultHeaders() {
        return defaultHeaders;
    }

}
