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

import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.api.Channel;
import net.deechael.khl.api.User;
import net.deechael.khl.core.action.Operation;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;
import com.fasterxml.jackson.databind.JsonNode;

public class PinnedMessageEvent extends AbstractEvent {

    public static final String _AcceptType = "pinned_message";

    private final String channelId;
    private final String operatorId;
    private final String msgId;

    public PinnedMessageEvent(KaiheilaBot rabbit, JsonNode node) {
        super(rabbit, node);
        JsonNode body = super.getEventExtraBody(node);
        msgId = body.get("msg_id").asText();
        operatorId = body.get("operator_id").asText();
        channelId = body.get("channel_id").asText();
    }

    @Override
    public Operation action() {
        return null;
    }

    public Channel getChannel() {
        return getKaiheilaBot().getCacheManager().getChannelCache().getElementById(channelId);
    }

    public User getOperator() {
        return getKaiheilaBot().getCacheManager().getUserCache().getElementById(operatorId);
    }

    public String getMsgId() {
        return msgId;
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        return this;
    }
}
