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
import net.deechael.khl.entity.GuildEntity;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;
import net.deechael.khl.gate.Gateway;

public class UpdatedGuildEvent extends AbstractEvent {

    public static final String _AcceptType = "updated_guild";

    private final String guildId;

    public UpdatedGuildEvent(Gateway gateway, JsonNode node) {
        super(gateway, node);
        JsonNode body = super.getEventExtraBody(node);
        guildId = body.get("id").asText();
    }

    public Guild getGuild() {
        return getKaiheilaBot().getCacheManager().getGuildCache().getElementById(guildId);
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        BaseCache<String, GuildEntity> guildCache = getKaiheilaBot().getCacheManager().getGuildCache();
        GuildEntity guildEntity = guildCache.getElementById(guildId);
        JsonNode node = super.getEventExtraBody(body);
        guildEntity.setName(node.get("name").asText());
        guildEntity.setIcon(node.get("icon").asText());
        guildEntity.setNotifyType(node.get("notify_type").asInt());
        guildEntity.setRegion(node.get("region").asText());
        guildEntity.setEnableOpen(node.get("enable_open").asBoolean());
        guildEntity.setOpenId(node.get("open_id").asText());
        guildEntity.setDefaultChannelId(node.get("default_channel_id").asText());
        guildEntity.setWelcomeChannelId(node.get("welcome_channel_id").asText());
        guildCache.updateElementById(guildId, guildEntity);
        return this;
    }
}
