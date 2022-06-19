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

import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.api.Guild;
import net.deechael.khl.api.Role;
import net.deechael.khl.api.User;
import net.deechael.khl.core.KaiheilaObject;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class MessageExtra extends KaiheilaObject {
    protected int type;
    protected String authorId;

    public MessageExtra(KaiheilaBot rabbit) {
        super(rabbit);
    }

    public int getType() {
        return type;
    }

    public User getAuthor() {
        return getKaiheilaBot().getCacheManager().getUserCache().getElementById(authorId);
    }

    public static PersonalMessageExtra buildPersonalMessageExtra(KaiheilaBot rabbit, JsonNode node) {
        PersonalMessageExtra r = new PersonalMessageExtra(rabbit);
        JsonNode extra = node.get("extra");
        r.type = extra.get("type").asInt();
        r.chatCode = extra.get("code").asText();
        r.authorId = extra.get("author").get("id").asText();
        return r;
    }

    public static MessageExtra buildMessageExtra(KaiheilaBot rabbit, JsonNode node){
        JsonNode extra = node.get("extra");
        if(node.get("channel_type").asText().equals("PERSON")){
            return buildPersonalMessageExtra(rabbit, node);
        }else{
            return buildChannelMessageExtra(rabbit, node);
        }
    }

    public static ChannelMessageExtra buildChannelMessageExtra(KaiheilaBot rabbit, JsonNode node) {
        ChannelMessageExtra r = new ChannelMessageExtra(rabbit);
        JsonNode extra = node.get("extra");
        r.type = extra.get("type").asInt();
        r.guildId = extra.get("guild_id").asText();
        r.channelName = extra.get("channel_name").asText();

        ArrayList<String> mentions = new ArrayList<>();
        extra.get("mention").iterator().forEachRemaining(s -> mentions.add(s.asText()));
        r.mention = mentions;

        r.mentionAll = extra.get("mention_all").asBoolean();

        ArrayList<Integer> roles = new ArrayList<>();
        extra.get("mention_roles").iterator().forEachRemaining(s -> roles.add(s.asInt()));
        r.mentionRoles = roles;

        r.mentionHere = extra.get("mention_here").asBoolean();
        r.authorId = extra.get("author").get("id").asText();
        return r;
    }

    public static class ChannelMessageExtra extends PersonalMessageExtra {
        protected String guildId;
        protected String channelName;
        protected List<String> mention;
        protected boolean mentionAll;
        protected List<Integer> mentionRoles;
        protected boolean mentionHere;

        public Guild getGuild() {
            return getKaiheilaBot().getCacheManager().getGuildCache().getElementById(guildId);
        }

        public String getChannelName() {
            return channelName;
        }

        public List<User> getMention() {
            ArrayList<User> r = new ArrayList<>();
            mention.forEach(s -> r.add(getKaiheilaBot().getCacheManager().getUserCache().getElementById(s)));
            return r;
        }

        public boolean isMentionAll() {
            return mentionAll;
        }

        public List<Role> getMentionRoles() {
            ArrayList<Role> r = new ArrayList<>();
            mentionRoles.forEach(s -> r.add(getKaiheilaBot().getCacheManager().getRoleCache().getElementById(s)));
            return r;
        }

        public boolean isMentionHere() {
            return mentionHere;
        }

        public ChannelMessageExtra(KaiheilaBot rabbit) {
            super(rabbit);
        }
    }

    public static class PersonalMessageExtra extends MessageExtra {
        String chatCode;
        public PersonalMessageExtra(KaiheilaBot rabbit) {
            super(rabbit);
        }

        public String getChatCode() {
            return chatCode;
        }
    }
}
