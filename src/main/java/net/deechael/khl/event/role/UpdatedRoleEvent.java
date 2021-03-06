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
import net.deechael.khl.entity.RoleEntity;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;
import net.deechael.khl.gate.Gateway;

public class UpdatedRoleEvent extends AbstractEvent {

    public static final String _AcceptType = "updated_role";

    private final Integer roleId;

    public UpdatedRoleEvent(Gateway gateway, JsonNode node) {
        super(gateway, node);
        JsonNode body = super.getEventExtraBody(node);
        roleId = body.get("role_id").asInt();
    }

    public Role getRole() {
        return getGateway().getKaiheilaBot().getCacheManager().getRoleCache().getElementById(roleId);
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        JsonNode node = super.getEventExtraBody(body);
        RoleEntity roleEntity = new RoleEntity(this.getGateway(), node);
        BaseCache<Integer, RoleEntity> roleCache = getGateway().getKaiheilaBot().getCacheManager().getRoleCache();
        roleCache.updateElementById(roleEntity.getRoleId(), roleEntity);
        return this;
    }
}
