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

import net.deechael.khl.RabbitImpl;
import net.deechael.khl.api.objects.Guild;
import net.deechael.khl.api.objects.Role;
import net.deechael.khl.api.objects.User;
import net.deechael.khl.core.RabbitObject;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class MessageExtra extends RabbitObject {

    private int type;
    private String guildId;
    private String channelName;
    private List<String> mention;
    private boolean mentionAll;
    private List<Integer> mentionRoles;
    private boolean mentionHere;
    private String authorId;

    public MessageExtra(RabbitImpl rabbit) {
        super(rabbit);
    }

    public int getType() {
        return type;
    }

    public Guild getGuild() {
        return getRabbitImpl().getCacheManager().getGuildCache().getElementById(guildId);
    }

    public String getChannelName() {
        return channelName;
    }

    public List<User> getMention() {
        ArrayList<User> r = new ArrayList<>();
        mention.forEach(s -> r.add(getRabbitImpl().getCacheManager().getUserCache().getElementById(s)));
        return r;
    }

    public boolean isMentionAll() {
        return mentionAll;
    }

    public List<Role> getMentionRoles() {
        ArrayList<Role> r = new ArrayList<>();
        mentionRoles.forEach(s -> r.add(getRabbitImpl().getCacheManager().getRoleCache().getElementById(s)));
        return r;
    }

    public boolean isMentionHere() {
        return mentionHere;
    }

    public User getAuthor() {
        return getRabbitImpl().getCacheManager().getUserCache().getElementById(authorId);
    }

    public static MessageExtra buildMessageExtra(RabbitImpl rabbit, JsonNode node) {
        MessageExtra r = new MessageExtra(rabbit);
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

}
