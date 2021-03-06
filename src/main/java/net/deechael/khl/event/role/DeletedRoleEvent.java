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

package net.deechael.khl.event.role;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.Role;
import net.deechael.khl.cache.BaseCache;
import net.deechael.khl.cache.CacheManager;
import net.deechael.khl.entity.GuildEntity;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;
import net.deechael.khl.gate.Gateway;

public class DeletedRoleEvent extends AbstractEvent {

    public static final String _AcceptType = "deleted_role";

    private final Integer roleId;

    public DeletedRoleEvent(Gateway gateway, JsonNode node) {
        super(gateway, node);
        JsonNode body = super.getEventExtraBody(node);
        roleId = body.get("role_id").asInt();
    }

    public Role getRole() {
        return getGateway().getKaiheilaBot().getCacheManager().getRoleCache().getElementById(roleId);
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        CacheManager cacheManager = getGateway().getKaiheilaBot().getCacheManager();
        cacheManager.getRoleCache().unloadElementById(roleId);
        BaseCache<String, GuildEntity> guildCache = cacheManager.getGuildCache();
        for (GuildEntity guild : guildCache) {
            guild.getRoles().remove(roleId);
        }
        return this;
    }
}
