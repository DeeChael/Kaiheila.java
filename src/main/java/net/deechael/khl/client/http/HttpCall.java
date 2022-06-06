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

package net.deechael.khl.client.http;

public class HttpCall {

    private boolean called;

    private Request request;
    private Response response;

    private HttpCall(Request request) {
        this.request = request;
    }

    public static HttpCall createRequest(HttpMethod method, String url, HttpHeaders headers, HttpRequestBody requestBody) {
        Request request = new Request(method, url, headers != null ? headers : new HttpHeaders(), requestBody);
        return new HttpCall(request);
    }

    public static HttpCall createRequest(HttpMethod method, String url, HttpHeaders headers) {
        return createRequest(method, url, headers, null);
    }

    public static HttpCall createRequest(String method, String url, HttpHeaders headers, HttpRequestBody requestBody) {
        return createRequest(HttpMethod.valueOf(method), url, headers, requestBody);
    }

    public static HttpCall createRequest(String method, String url, HttpHeaders headers) {
        return createRequest(HttpMethod.valueOf(method), url, headers);
    }

    public boolean isCalled() {
        return called;
    }

    public void setCalled(boolean called) {
        this.called = called;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public static class Request {
        private final String url;
        private final HttpMethod method;
        private final HttpHeaders headers;
        private final HttpRequestBody requestBody;

        private Request(HttpMethod method, String url, HttpHeaders headers, HttpRequestBody requestBody) {
            this.method = method;
            this.url = url;
            this.headers = headers;
            this.requestBody = requestBody;
        }

        public String getUrl() {
            return url;
        }

        public HttpMethod getMethod() {
            return method;
        }

        public HttpHeaders getHeaders() {
            return headers;
        }

        public HttpRequestBody getRequestBody() {
            return requestBody;
        }
    }

    public static class Response {
        private final int code;
        private final HttpHeaders header;
        private final HttpResponseBody responseBody;

        public Response(int code, HttpHeaders header, HttpResponseBody responseBody) {
            this.code = code;
            this.header = header;
            this.responseBody = responseBody;
        }

        public int getCode() {
            return code;
        }

        public HttpHeaders getHeader() {
            return header;
        }

        public HttpResponseBody getResponseBody() {
            return responseBody;
        }
    }

}
