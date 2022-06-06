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

package net.deechael.khl.hook.source.websocket;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class WebSocketEventSourceSession {

    private String sessionId;

    private String gateway;

    private int sn;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public void reset() {
        this.sessionId = null;
        this.gateway = null;
        this.sn = 0;
    }

    private String makeUrlParam(String key, Object value) {
        return URLEncoder.encode(key, StandardCharsets.UTF_8) + "=" + URLEncoder.encode(String.valueOf(value), StandardCharsets.UTF_8);
    }

    public String getReconnectUrl() {
        Objects.requireNonNull(gateway, "gateway == null");
        Objects.requireNonNull(sessionId, "sessionId == null");
        return this.gateway +
                '&' + makeUrlParam("resume", 1) +
                '&' + makeUrlParam("sn", this.sn) +
                '&' + makeUrlParam("session_id", this.sessionId);
    }
}
