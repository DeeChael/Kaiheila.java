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

import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.api.Guild;
import net.deechael.khl.cache.BaseCache;
import net.deechael.khl.core.action.Operation;
import net.deechael.khl.entity.GuildEntity;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;
import com.fasterxml.jackson.databind.JsonNode;

public class UpdatedGuildEvent extends AbstractEvent {

    public static final String _AcceptType = "updated_guild";

    private final String guildId;

    public UpdatedGuildEvent(KaiheilaBot rabbit, JsonNode node) {
        super(rabbit, node);
        JsonNode body = super.getEventExtraBody(node);
        guildId = body.get("id").asText();
    }

    @Override
    public Operation action() {
        return null;
    }

    public Guild getGuild() {
        return getKaiheilaBot().getCacheManager().getGuildCache().getElementById(guildId);
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        BaseCache<String, GuildEntity> guildCache = (BaseCache<String, GuildEntity>) getKaiheilaBot().getCacheManager().getGuildCache();
        GuildEntity guildEntity = guildCache.getElementById(guildId);
        guildEntity = getKaiheilaBot().getEntitiesBuilder().updateGuildEntityForEvent(guildEntity, super.getEventExtraBody(body));
        guildCache.updateElementById(guildId, guildEntity);
        return this;
    }
}
