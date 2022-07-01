/*
 *    Copyright 2020-2021 Rabbit author and contributors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.deechael.khl.client.http.impl;

import net.deechael.khl.client.http.*;
import okhttp3.*;
import okhttp3.internal.Util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public class OkHttpClientImpl implements IHttpClient {

    private final OkHttpClient client;

    public OkHttpClientImpl(OkHttpClient client) {
        this.client = client;
    }

    private String getHeadersFirstValue(List<Object> values) {
        return String.valueOf(values.get(0));
    }

    private RequestBody parseHttpCallRequestBody(HttpMethod method, HttpCall.Request request) {
        HttpRequestBody httpRequestBody = request.getRequestBody();
        if (httpRequestBody == null) {
            if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH) {
                return request.getBody();
            } else {
                return null;
            }
        }
        HttpMediaType contentType = httpRequestBody.getContentType();
        MediaType mediaType = MediaType.get(contentType.getName());
        return RequestBody.create(httpRequestBody.getBuffer().array(), mediaType);
    }

    private Request parseHttpCallRequest(HttpCall httpCall) {
        HttpCall.Request request = httpCall.getRequest();
        Request.Builder builder = new Request.Builder();
        builder.url(request.getUrl());
        if (httpCall.getRequest().getMethod() == HttpMethod.GET) {
            builder.post(Util.EMPTY_REQUEST);
        }
        // OkHttpClient 不支持 单个Header多个值
        request.getHeaders().forEach((key, values) -> builder.addHeader(key, getHeadersFirstValue(values)));
        builder.method(request.getMethod().name(), parseHttpCallRequestBody(request.getMethod(), request));
        return builder.build();
    }

    private void response(HttpCall context, Response response) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        // Header 转换
        response.headers().toMultimap().forEach((key, values) -> values.forEach(value -> headers.addHeader(key, value)));
        // Body 转换
        ResponseBody body = response.body();
        HttpResponseBody responseBody = null;
        if (body != null) {
            // content-type
            MediaType mediaType = body.contentType();
            HttpMediaType httpMediaType = HttpMediaType.of(mediaType != null ? mediaType.toString() : "");
            // body data
            byte[] bodyBytes = body.bytes();
            int length = bodyBytes.length;
            responseBody = new HttpResponseBody(length, httpMediaType, ByteBuffer.allocate(length).put(bodyBytes));
        }
        HttpCall.Response contextResponse = new HttpCall.Response(response.code(), headers, responseBody);
        context.setResponse(contextResponse);
    }

    @Override
    public HttpCall.Response execute(HttpCall context) throws IOException {
        if (context.isCalled()) {
            throw new IOException(context.getRequest().getUrl() + " is called");
        }
        context.setCalled(true);
        Request request = parseHttpCallRequest(context);
        Call call = client.newCall(request);
        response(context, call.execute());
        return context.getResponse();
    }
}
