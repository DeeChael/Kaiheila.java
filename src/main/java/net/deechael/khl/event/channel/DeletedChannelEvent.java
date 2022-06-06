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
import net.deechael.khl.cache.BaseCache;
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

    public DeletedChannelEvent(RabbitImpl rabbit, JsonNode node) {
        super(rabbit, node);
        JsonNode body = super.getEventExtraBody(node);
        channelId = body.get("id").asText();
        deletedAt = TimeUtil.convertUnixTimeMillisecondLocalDateTime(body.get("deleted_at").asLong());
    }

    public Channel getChannel() {
        return getRabbitImpl().getCacheManager().getChannelCache().getElementById(channelId);
    }

    public LocalDateTime getDeletedTime() {
        return deletedAt;
    }

    @Override
    public IEvent handleSystemEvent(JsonObject body) {
        // 更新缓存
        BaseCache<String, GuildEntity> guildCache = (BaseCache<String, GuildEntity>) getRabbitImpl().getCacheManager().getGuildCache();
        BaseCache<String, ChannelEntity> channelCache = (BaseCache<String, ChannelEntity>) getRabbitImpl().getCacheManager().getChannelCache();
        channelCache.unloadElementById(channelId);
        for (GuildEntity guild : guildCache) {
            guild.getChannels().remove(channelId);
        }
        return this;
    }
}
