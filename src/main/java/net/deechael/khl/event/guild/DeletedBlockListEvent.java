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

import com.google.gson.JsonObject;
import net.deechael.khl.RabbitImpl;
import net.deechael.khl.api.objects.User;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class DeletedBlockListEvent extends AbstractEvent {

    public static final String _AcceptType = "deleted_block_list";

    private final String operatorId;
    private final List<String> userId;

    public DeletedBlockListEvent(RabbitImpl rabbit, JsonNode node) {
        super(rabbit, node);
        JsonNode body = super.getEventExtraBody(node);
        operatorId = body.get("operator_id").asText();
        ArrayList<String> users = new ArrayList<>();
        body.get("user_id").iterator().forEachRemaining(r -> users.add(r.asText()));
        userId = users;
    }

    public User getOperator() {
        return getRabbitImpl().getCacheManager().getUserCache().getElementById(operatorId);
    }

    public List<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        userId.forEach(s -> users.add(getRabbitImpl().getCacheManager().getUserCache().getElementById(operatorId)));
        return users;
    }

    @Override
    public IEvent handleSystemEvent(JsonObject body) {
        // todo Wait for KHL Official, Fix Event Data
        return this;
    }
}
