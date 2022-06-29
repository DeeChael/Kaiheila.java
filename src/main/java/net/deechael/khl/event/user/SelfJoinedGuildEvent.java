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

package net.deechael.khl.event.user;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.Guild;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;
import net.deechael.khl.gate.Gateway;

public class SelfJoinedGuildEvent extends AbstractEvent {

    public static final String _AcceptType = "self_joined_guild";

    private final String guildId;

    public SelfJoinedGuildEvent(Gateway gateway, JsonNode node) {
        super(gateway, node);
        JsonNode body = super.getEventExtraBody(node);
        guildId = body.get("guild_id").asText();
    }

    public Guild getGuild() {
        return getKaiheilaBot().getCacheManager().getGuildCache().getElementById(guildId);
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        return this;
    }
}
