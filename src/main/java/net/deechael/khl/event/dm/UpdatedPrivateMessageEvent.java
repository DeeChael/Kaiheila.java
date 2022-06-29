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

package net.deechael.khl.event.dm;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.User;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;
import net.deechael.khl.gate.Gateway;
import net.deechael.khl.util.TimeUtil;

import java.time.LocalDateTime;

public class UpdatedPrivateMessageEvent extends AbstractEvent {

    public static final String _AcceptType = "updated_private_message";

    private final String msgId;
    private final String authorId;
    private final String targetId;
    private final String content;
    private final String chatCode;
    private final LocalDateTime updatedAt;

    public UpdatedPrivateMessageEvent(Gateway gateway, JsonNode node) {
        super(gateway, node);
        JsonNode body = super.getEventExtraBody(node);
        msgId = body.get("msg_id").asText();
        authorId = body.get("author_id").asText();
        targetId = body.get("target_id").asText();
        content = body.get("content").asText();
        chatCode = body.get("chat_code").asText();
        updatedAt = TimeUtil.convertUnixTimeMillisecondLocalDateTime(body.get("updated_at").asLong());
    }

    public String getMsgId() {
        return msgId;
    }

    public User getAuthor() {
        return getGateway().getKaiheilaBot().getCacheManager().getUserCache().getElementById(authorId);
    }

    public User getTarget() {
        return getGateway().getKaiheilaBot().getCacheManager().getUserCache().getElementById(targetId);
    }

    public String getContent() {
        return content;
    }

    public String getChatCode() {
        return chatCode;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedAt;
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        return this;
    }
}
