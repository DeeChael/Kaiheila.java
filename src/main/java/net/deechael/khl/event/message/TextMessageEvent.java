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

package net.deechael.khl.event.message;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.Channel;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;

public class TextMessageEvent extends AbstractEvent {
    public enum Type {
        Group,
        Person;

        public final String value;

        Type() {
            this.value = this.name().toUpperCase();
        }

        public static Type from(String value) {
            for (Type type : values()) {
                if (type.value.toUpperCase().equals(value)) {
                    return type;
                }
            }
            return null;
        }
    }

    public final Type type;
    private final MessageExtra extra;
    public final String messageID;

    public TextMessageEvent(KaiheilaBot rabbit, JsonNode node) {
        super(rabbit, node);
        type = Type.from(getEventChannelType());
        messageID = node.get("msg_id").asText();
        this.extra = MessageExtra.buildMessageExtra(rabbit, node);
    }

    protected TextMessageEvent(KaiheilaBot rabbit, TextMessageEvent event) {
        super(rabbit, event);
        this.type = event.type;
        this.extra = event.extra;
        this.messageID = event.messageID;
    }

    protected MessageExtra getExtra() {
        return this.extra;
    }

    public PrivateMessageEvent asPrivateMessageEvent() {
        return new PrivateMessageEvent(getKaiheilaBot(), this);
    }

    public ChannelMessageEvent asChannelMessageEvent() {
        return new ChannelMessageEvent(getKaiheilaBot(), this);
    }

    public Channel getChannel() {
        return getKaiheilaBot().getCacheManager().getChannelCache().getElementById(super.getEventTargetId());
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        return this;
    }

    public static class PrivateMessageEvent extends TextMessageEvent {

        public PrivateMessageEvent(KaiheilaBot rabbit, TextMessageEvent event) {
            super(rabbit, event);
        }

        @Override
        public MessageExtra.PersonalMessageExtra getExtra() {
            return (MessageExtra.PersonalMessageExtra) super.getExtra();
        }
    }

    public static class ChannelMessageEvent extends TextMessageEvent {

        public ChannelMessageEvent(KaiheilaBot rabbit, TextMessageEvent event) {
            super(rabbit, event);
        }

        @Override
        public MessageExtra.ChannelMessageExtra getExtra() {
            return (MessageExtra.ChannelMessageExtra) super.getExtra();
        }
    }
}