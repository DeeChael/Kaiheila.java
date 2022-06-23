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

import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.api.User;
import net.deechael.khl.cache.BaseCache;
import net.deechael.khl.entity.UserEntity;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;
import com.fasterxml.jackson.databind.JsonNode;

import java.net.MalformedURLException;
import java.net.URL;

public class UserUpdatedEvent extends AbstractEvent {

    public static final String _AcceptType = "user_updated";

    private final String userId;
    private final String username;
    private final String avatar;

    public UserUpdatedEvent(KaiheilaBot rabbit, JsonNode node) {
        super(rabbit, node);
        JsonNode body = super.getEventExtraBody(node);
        userId = body.get("user_id").asText();
        username = body.get("username").asText();
        avatar = body.get("avatar").asText();
    }

    public User getUser() {
        return getKaiheilaBot().getCacheManager().getUserCache().getElementById(userId);
    }

    public String getUsername() {
        return username;
    }

    public URL getAvatar() {
        try {
            return new URL(avatar);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        BaseCache<String, UserEntity> userCache = (BaseCache<String, UserEntity>) getKaiheilaBot().getCacheManager().getUserCache();
        UserEntity userEntity = userCache.getElementById(userId);
        userEntity = getKaiheilaBot().getEntitiesBuilder().updateUserEntityForEvent(userEntity, body);
        userCache.updateElementById(userId, userEntity);
        return this;
    }

}
