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

package net.deechael.khl.event.guild;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.Guild;
import net.deechael.khl.cache.BaseCache;
import net.deechael.khl.cache.CacheManager;
import net.deechael.khl.entity.GuildEntity;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;
import net.deechael.khl.gate.Gateway;

public class DeletedGuildEvent extends AbstractEvent {

    public static final String _AcceptType = "deleted_guild";

    private final String guildId;

    public DeletedGuildEvent(Gateway gateway, JsonNode node) {
        super(gateway, node);
        JsonNode body = super.getEventExtraBody(node);
        guildId = body.get("id").asText();
    }

    public Guild getGuild() {
        return getKaiheilaBot().getCacheManager().getGuildCache().getElementById(guildId);
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        CacheManager cacheManager = getKaiheilaBot().getCacheManager();
        BaseCache<String, GuildEntity> guildCache = cacheManager.getGuildCache();
        GuildEntity guildEntity = guildCache.getElementById(guildId);
        for (Integer role : guildEntity.getRoles()) {
            cacheManager.getRoleCache().unloadElementById(role);
        }
        for (String channel : guildEntity.getChannelIDs()) {
            cacheManager.getChannelCache().unloadElementById(channel);
        }
        for (String emoji : guildEntity.getEmojis()) {
            cacheManager.getGuildEmojisCache().unloadElementById(emoji);
        }
        guildCache.unloadElementById(guildId);
        return this;
    }
}
