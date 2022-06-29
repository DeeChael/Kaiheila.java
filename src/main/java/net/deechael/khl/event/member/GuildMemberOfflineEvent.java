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

package net.deechael.khl.event.member;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.Guild;
import net.deechael.khl.api.User;
import net.deechael.khl.cache.CacheManager;
import net.deechael.khl.entity.GuildEntity;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;
import net.deechael.khl.gate.Gateway;
import net.deechael.khl.util.TimeUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GuildMemberOfflineEvent extends AbstractEvent {

    public static final String _AcceptType = "guild_member_offline";

    private final String userId;
    private final LocalDateTime offlineTime;
    private final List<String> guilds;

    public GuildMemberOfflineEvent(Gateway gateway, JsonNode node) {
        super(gateway, node);
        JsonNode body = super.getEventExtraBody(node);
        userId = body.get("user_id").asText();
        offlineTime = TimeUtil.convertUnixTimeMillisecondLocalDateTime(body.get("event_time").asLong());
        ArrayList<String> gId = new ArrayList<>();
        body.get("guilds").iterator().forEachRemaining(r -> gId.add(r.asText()));
        guilds = gId;
    }

    public User getUser() {
        return getKaiheilaBot().getCacheManager().getUserCache().getElementById(userId);
    }

    public LocalDateTime getUserOnlineTime() {
        return offlineTime;
    }

    public List<Guild> getGuilds() {
        ArrayList<Guild> r = new ArrayList<>();
        guilds.forEach(s -> r.add(getKaiheilaBot().getCacheManager().getGuildCache().getElementById(s)));
        return r;
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        CacheManager cacheManager = getKaiheilaBot().getCacheManager();
        for (String guild : guilds) {
            GuildEntity guildEntity = cacheManager.getGuildCache().getElementById(guild);
            guildEntity.setOnlineCount(guildEntity.getOnlineCount() - 1);
            guildEntity.setOfflineCount(guildEntity.getOfflineCount() + 1);
        }
        return this;
    }
}
