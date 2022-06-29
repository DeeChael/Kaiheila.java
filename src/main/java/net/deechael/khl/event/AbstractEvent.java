/*
 *    Copyright 2021 FightingGuys Team and khl-sdk-java contributors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.deechael.khl.event;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.User;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.gate.Gateway;
import net.deechael.khl.util.TimeUtil;

import java.time.LocalDateTime;

public abstract class AbstractEvent extends KaiheilaObject implements IEvent {

    private final String eventChannelType;
    private final int eventType;
    private final String eventTargetId;
    private final String eventAuthorId;
    private final String eventContent;
    private final String eventId;
    private final LocalDateTime eventTime;
    private final String eventNonce;

    public AbstractEvent(Gateway gateway, JsonNode node) {
        super(gateway);
        this.eventChannelType = node.get("channel_type").asText();
        this.eventType = node.get("type").asInt();
        this.eventTargetId = node.get("target_id").asText();
        this.eventAuthorId = node.get("author_id").asText();
        this.eventContent = node.get("content").asText();
        this.eventId = node.get("msg_id").asText();
        this.eventTime = TimeUtil.convertUnixTimeMillisecondLocalDateTime(node.get("msg_timestamp").asLong());
        this.eventNonce = node.get("nonce").asText();
    }

    protected AbstractEvent(Gateway gateway, AbstractEvent event) {
        super(gateway);
        this.eventChannelType = event.eventChannelType;
        this.eventType = event.eventType;
        this.eventTargetId = event.eventTargetId;
        this.eventAuthorId = event.eventAuthorId;
        this.eventContent = event.eventContent;
        this.eventId = event.eventId;
        this.eventTime = event.eventTime;
        this.eventNonce = event.eventNonce;
    }

    public JsonNode getEventExtraBody(JsonNode node) {
        return node.get("extra").get("body");
    }

    public JsonNode getEventType(JsonNode node) {
        return node.get("extra").get("type");
    }

    public String getEventChannelType() {
        return eventChannelType;
    }

    public int getEventType() {
        return eventType;
    }

    public String getEventTargetId() {
        return eventTargetId;
    }

    public User getEventAuthor() {
        return getKaiheilaBot().getCacheManager().getUserCache().getElementById(eventAuthorId);
    }

    public String getEventAuthorId() {
        return eventAuthorId;
    }

    public String getEventContent() {
        return eventContent;
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public String getEventNonce() {
        return eventNonce;
    }

}
