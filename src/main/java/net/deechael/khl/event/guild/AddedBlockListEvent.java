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
import net.deechael.khl.api.User;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;

import java.util.ArrayList;
import java.util.List;

public class AddedBlockListEvent extends AbstractEvent {

    public static final String _AcceptType = "added_block_list";

    private final String operatorId;
    private final String remark;
    private final List<String> userId;

    public AddedBlockListEvent(KaiheilaBot rabbit, JsonNode node) {
        super(rabbit, node);
        JsonNode body = super.getEventExtraBody(node);
        operatorId = body.get("operator_id").asText();
        remark = body.get("remark").asText();
        ArrayList<String> users = new ArrayList<>();
        body.get("user_id").iterator().forEachRemaining(r -> users.add(r.asText()));
        userId = users;
    }

    public User getOperator() {
        return getKaiheilaBot().getCacheManager().getUserCache().getElementById(operatorId);
    }

    public String getRemark() {
        return remark;
    }

    public List<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        userId.forEach(s -> users.add(getKaiheilaBot().getCacheManager().getUserCache().getElementById(operatorId)));
        return users;
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        // todo Wait for KHL Official, Fix Event Data
        return this;
    }
}
