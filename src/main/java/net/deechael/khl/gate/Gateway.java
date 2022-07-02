package net.deechael.khl.gate;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.client.http.HttpCall;
import net.deechael.khl.client.http.HttpHeaders;
import net.deechael.khl.client.http.HttpMediaType;
import net.deechael.khl.client.http.HttpRequestBody;
import net.deechael.khl.restful.RestPageable;
import net.deechael.khl.restful.RestRoute;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Gateway {

    protected static final Logger Log = LoggerFactory.getLogger(Gateway.class);

    private final KaiheilaBot kaiheilaBot;
    private final HttpHeaders defaultHeaders = new HttpHeaders();

    private boolean initialized = false;

    public Gateway(KaiheilaBot kaiheilaBot) {
        this.kaiheilaBot = kaiheilaBot;
    }

    public void initialize() {
        if (!initialized) {
            if (kaiheilaBot.getConfiguration() != null) {
                defaultHeaders.addHeader("Authorization", "Bot " + kaiheilaBot.getConfiguration().getApiConfigurer().getToken());
            }
            initialized = true;
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public KaiheilaBot getKaiheilaBot() {
        return kaiheilaBot;
    }

    public JsonNode executeRequest(RestRoute.CompiledRoute route) {
        String jsonString = new Gson().toJson(route.getQueryJson());
        HttpRequestBody requestBody = null;
        if (route.getMethod() == "POST") {
            requestBody = new HttpRequestBody(jsonString.length(), HttpMediaType.JSON, ByteBuffer.wrap(jsonString.getBytes(StandardCharsets.UTF_8)));
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        this.defaultHeaders.forEach(httpHeaders::addHeaders);
        if (route.getRoute().equals(RestRoute.Misc.UPLOAD_ASSET)) {
            httpHeaders.addHeader("Content-Type", "multipart/form-data");
            requestBody = new HttpRequestBody(jsonString.length(), HttpMediaType.FORM_DATA, ByteBuffer.wrap(jsonString.getBytes(StandardCharsets.UTF_8)));
        }
        HttpCall request = HttpCall.createRequest(route.getMethod(), getCompleteUrl(route), httpHeaders, requestBody);
        List<JsonNode> data = getRestJsonResponse(route, request);
        if (data == null) {
            throw new RuntimeException("An error was thrown when executing " + route.getCompiledRoute());
        }
        return data.get(0).get("data");
    }

    public String uploadAsset(File file) {
        HttpHeaders httpHeaders = new HttpHeaders();
        this.defaultHeaders.forEach(httpHeaders::addHeaders);
        httpHeaders.addHeader("Content-Type", "multipart/form-data");
        HttpCall request = HttpCall.createRequest(RestRoute.Misc.UPLOAD_ASSET.compile().getMethod(), getCompleteUrl(RestRoute.Misc.UPLOAD_ASSET.compile()), httpHeaders, null);
        request.getRequest().setBody(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse(HttpMediaType.FORM_DATA.getName()), file)).build());
        List<JsonNode> data = getRestJsonResponse(RestRoute.Misc.UPLOAD_ASSET.compile(), request);
        if (data == null) {
            throw new RuntimeException("An error was thrown when executing " + RestRoute.Misc.UPLOAD_ASSET.compile().getCompiledRoute());
        }
        return data.get(0).get("data").get("url").asText();
    }

    public List<JsonNode> executePaginationRequest(RestRoute.CompiledRoute route) {
        HttpCall request = HttpCall.createRequest(route.getMethod(), getCompleteUrl(route), this.defaultHeaders);
        List<JsonNode> data = getRestJsonResponse(route, request);
        if (data == null) {
            throw new RuntimeException("An error was thrown when executing " + route.getCompiledRoute());
        }
        List<JsonNode> list = new ArrayList<>();
        for (JsonNode node1 : data) {
            Iterator<JsonNode> iterator = node1.get("data").get("items").elements();
            while (iterator.hasNext()) {
                JsonNode item = iterator.next();
                list.add(item);
            }
        }
        return list;
    }

    private List<JsonNode> getRestJsonResponse(RestRoute.CompiledRoute compiledRoute, HttpCall call) {
        ArrayList<JsonNode> result = new ArrayList<>();
        JsonNode root = callRestApi(call);
        if (root == null) {
            return null;
        }
        result.add(root);
        result.addAll(getRemainPageRestData(compiledRoute, getRestApiData(root)));
        return result;
    }

    private List<JsonNode> getRemainPageRestData(RestRoute.CompiledRoute compiledRoute, JsonNode data) {
        ArrayList<JsonNode> result = new ArrayList<>();
        RestPageable pageable = RestPageable.of(this.kaiheilaBot, compiledRoute, data);
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

    private String getCompleteUrl(RestRoute.CompiledRoute route) {
        return kaiheilaBot.getConfiguration().getApiConfigurer().getBaseUrl() + route.getQueryStringCompleteRoute();
    }

    private JsonNode callRestApi(HttpCall call) {
        JsonNode root = null;
        boolean callFailed;
        int callRetry = 0;
        HttpCall.Response response;
        do {
            try {
                response = kaiheilaBot.getHttpClient().execute(call);
                if (response.getCode() != 200) {
                    reportRequestFailed(++callRetry, call.getRequest().getUrl());
                    callFailed = true;
                    continue;
                }
                root = kaiheilaBot.getJsonEngine().readTree(response.getResponseBody().getBuffer().array());
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

    private void reportRequestFailed(int retryCount, String url) {
        if (Log.isWarnEnabled()) {
            Log.warn("[数据同步] 获取数据失败，3秒后第{}次重试, [{}]", retryCount, url);
        }
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean hasRestApiError(JsonNode root) {
        if (root == null) {
            return true;
        }
        if (!root.has("code")) {
            return true;
        }
        if (root.get("code").asInt() != 0) {
            Log.error("[数据同步] API请求失败，错误码：{}, 错误消息：{}", root.get("code").asInt(), root.get("message").asText());
            return true;
        }
        return false;
    }

    private boolean handleResult(JsonNode res) {
        return res != null && res.get("code").asInt() == 0;
    }

    private HttpHeaders getDefaultHeaders() {
        return defaultHeaders;
    }

}
