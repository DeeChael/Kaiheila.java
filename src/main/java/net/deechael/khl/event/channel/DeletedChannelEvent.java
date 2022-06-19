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
import net.deechael.khl.cache.BaseCache;
import net.deechael.khl.core.action.Operation;
import net.deechael.khl.entity.ChannelEntity;
import net.deechael.khl.entity.GuildEntity;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;
import net.deechael.khl.util.TimeUtil;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public class DeletedChannelEvent extends AbstractEvent {

    public static final String _AcceptType = "deleted_channel";

    private final String channelId;
    private final LocalDateTime deletedAt;

    public DeletedChannelEvent(KaiheilaBot rabbit, JsonNode node) {
        super(rabbit, node);
        JsonNode body = super.getEventExtraBody(node);
        channelId = body.get("id").asText();
        deletedAt = TimeUtil.convertUnixTimeMillisecondLocalDateTime(body.get("deleted_at").asLong());
    }

    @Override
    public Operation action() {
        return null;
    }

    public Channel getChannel() {
        return getKaiheilaBot().getCacheManager().getChannelCache().getElementById(channelId);
    }

    public LocalDateTime getDeletedTime() {
        return deletedAt;
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        // 更新缓存
        BaseCache<String, GuildEntity> guildCache = (BaseCache<String, GuildEntity>) getKaiheilaBot().getCacheManager().getGuildCache();
        BaseCache<String, ChannelEntity> channelCache = (BaseCache<String, ChannelEntity>) getKaiheilaBot().getCacheManager().getChannelCache();
        channelCache.unloadElementById(channelId);
        for (GuildEntity guild : guildCache) {
            guild.getChannels().remove(channelId);
        }
        return this;
    }
}
