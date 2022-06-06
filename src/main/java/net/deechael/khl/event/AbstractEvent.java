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

import com.google.gson.JsonObject;
import net.deechael.khl.RabbitImpl;
import net.deechael.khl.api.objects.User;
import net.deechael.khl.core.RabbitObject;
import net.deechael.khl.util.TimeUtil;

import java.time.LocalDateTime;

public abstract class AbstractEvent extends RabbitObject implements IEvent {

    private final String eventChannelType;
    private final int eventType;
    private final String eventTargetId;
    private final String eventAuthorId;
    private final String eventContent;
    private final String eventId;
    private final LocalDateTime eventTime;
    private final String eventNonce;

    public AbstractEvent(RabbitImpl rabbit, JsonObject node) {
        super(rabbit);
        this.eventChannelType = node.get("channel_type").getAsString();
        this.eventType = node.get("type").getAsInt();
        this.eventTargetId = node.get("target_id").getAsString();
        this.eventAuthorId = node.get("author_id").getAsString();
        this.eventContent = node.get("content").getAsString();
        this.eventId = node.get("msg_id").getAsString();
        this.eventTime = TimeUtil.convertUnixTimeMillisecondLocalDateTime(node.get("msg_timestamp").getAsLong());
        this.eventNonce = node.get("nonce").getAsString();
    }

    public JsonObject getEventExtraBody(JsonObject node) {
        return node.getAsJsonObject("extra").getAsJsonObject("body");
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

    public User getEventAuthorId() {
        return getRabbitImpl().getCacheManager().getUserCache().getElementById(eventAuthorId);
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
