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

import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.api.Emoji;
import net.deechael.khl.api.User;
import net.deechael.khl.core.action.Operation;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;
import com.fasterxml.jackson.databind.JsonNode;

public class PrivateDeletedReactionEvent extends AbstractEvent {

    public static final String _AcceptType = "private_deleted_reaction";

    private final String msgId;
    private final String userId;
    private final String chatCode;
    private final String emojiId;

    public PrivateDeletedReactionEvent(KaiheilaBot rabbit, JsonNode node) {
        super(rabbit, node);
        JsonNode body = super.getEventExtraBody(node);
        msgId = body.get("msg_id").asText();
        userId = body.get("user_id").asText();
        chatCode = body.get("chat_code").asText();
        emojiId = body.get("emoji").get("id").asText();
    }

    @Override
    public Operation action() {
        return null;
    }

    public String getMsgId() {
        return msgId;
    }

    public User getUser() {
        return getKaiheilaBot().getCacheManager().getUserCache().getElementById(userId);
    }

    public String getChatCode() {
        return chatCode;
    }

    public Emoji getEmoji() {
        return getKaiheilaBot().getCacheManager().getGuildEmojisCache().getElementById(emojiId);
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        return this;
    }
}
