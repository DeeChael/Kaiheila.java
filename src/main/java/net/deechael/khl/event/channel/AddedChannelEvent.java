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

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.Channel;
import net.deechael.khl.api.Guild;
import net.deechael.khl.cache.BaseCache;
import net.deechael.khl.cache.CacheManager;
import net.deechael.khl.entity.ChannelEntity;
import net.deechael.khl.entity.GuildEntity;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;
import net.deechael.khl.gate.Gateway;

public class AddedChannelEvent extends AbstractEvent {

    public static final String _AcceptType = "added_channel";

    private final String guildId;
    private final String channelId;

    public AddedChannelEvent(Gateway gateway, JsonNode node) {
        super(gateway, node);
        JsonNode body = super.getEventExtraBody(node);
        guildId = body.get("guild_id").asText();
        channelId = body.get("id").asText();
    }

    public Guild getGuild() {
        return getKaiheilaBot().getCacheManager().getGuildCache().getElementById(guildId);
    }

    public Channel getChannel() {
        return getKaiheilaBot().getCacheManager().getChannelCache().getElementById(channelId);
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        JsonNode node = super.getEventExtraBody(body);
        ChannelEntity entity = new ChannelEntity(this.getGateway(), node, true);
        // 更新缓存
        CacheManager cacheManager = getKaiheilaBot().getCacheManager();
        GuildEntity guild = cacheManager.getGuildCache().getElementById(guildId);
        guild.getChannelIDs().add(entity.getId());
        ((BaseCache<String, ChannelEntity>) cacheManager.getChannelCache()).updateElementById(entity.getId(), entity);
        return this;
    }
}
