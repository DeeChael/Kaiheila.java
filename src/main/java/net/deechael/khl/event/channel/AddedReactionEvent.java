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

package net.deechael.khl.event.channel;

import com.google.gson.JsonObject;
import net.deechael.khl.RabbitImpl;
import net.deechael.khl.api.objects.Channel;
import net.deechael.khl.api.objects.Emoji;
import net.deechael.khl.api.objects.User;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;
import com.fasterxml.jackson.databind.JsonNode;

public class AddedReactionEvent extends AbstractEvent {

    public static final String _AcceptType = "added_reaction";

    private final String msgId;
    private final String userId;
    private final String channelId;
    private final String emojiId;

    public AddedReactionEvent(RabbitImpl rabbit, JsonNode node) {
        super(rabbit, node);
        JsonNode body = super.getEventExtraBody(node);
        this.msgId = body.get("msg_id").asText();
        this.userId = body.get("user_id").asText();
        this.channelId = body.get("channel_id").asText();
        this.emojiId = body.get("emoji").get("id").asText();
    }

    public String getMsgId() {
        return msgId;
    }

    public User getUser() {
        return getRabbitImpl().getCacheManager().getUserCache().getElementById(userId);
    }

    public Channel getChannel() {
        return getRabbitImpl().getCacheManager().getChannelCache().getElementById(channelId);
    }

    public Emoji getEmoji() {
        return getRabbitImpl().getCacheManager().getGuildEmojisCache().getElementById(emojiId);
    }

    @Override
    public IEvent handleSystemEvent(JsonObject body) {
        return this;
    }
}
