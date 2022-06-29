package net.deechael.khl.cache;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.entity.*;
import net.deechael.khl.gate.Gateway;
import net.deechael.khl.restful.RestRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager extends KaiheilaObject {
    protected static final Logger Log = LoggerFactory.getLogger(CacheManager.class);

    private SelfUserEntity selfUserCache;
    private final BaseCache<String, GuildEntity> guildCache = new BaseCache<>(GuildEntity::getName);
    private final BaseCache<Integer, RoleEntity> roleCache = new BaseCache<>(RoleEntity::getName);
    private final BaseCache<String, ChannelEntity> channelCache = new BaseCache<>(ChannelEntity::getName);
    private final BaseCache<String, UserEntity> userCache = new BaseCache<>(UserEntity::getName);
    private final BaseCache<String, EmojiEntity> guildEmojisCache = new BaseCache<>(EmojiEntity::getName);
    private final ConcurrentHashMap<String, Map<String, GuildUserEntity>> guildUsersCache = new ConcurrentHashMap<>();
//    private final ConcurrentHashMap<String, Map<String, MemberEntity>> guildInvites = new ConcurrentHashMap<>();

    public CacheManager(Gateway gateway) {
        super(gateway);
    }

    public void unloadCache() {
        this.selfUserCache = null;
        this.guildCache.unloadAll();
        this.roleCache.unloadAll();
        this.channelCache.unloadAll();
        this.userCache.unloadAll();
        this.guildUsersCache.clear();
        this.guildEmojisCache.unloadAll();
    }

    public boolean checkTokenAvailable() {
        if (getGateway().getKaiheilaBot().getConfiguration().getApiConfigurer().getToken().isEmpty()) return false;
        try {
            fetchSelfUser();
            if (this.selfUserCache != null) {
                return true;
            }
        } catch (InterruptedException e) {
            unloadCache();
        }
        return false;
    }

    public void updateCache() {
        unloadCache();
        try {
            fetchSelfUser();
            fetchGuildBaseData();
        } catch (InterruptedException e) {
            unloadCache();
        }
    }

    private void fetchSelfUser() throws InterruptedException {
        JsonNode data = getGateway().executeRequest(RestRoute.Misc.SELF_USER.compile());
        this.selfUserCache = getGateway().getKaiheilaBot().getEntitiesBuilder().buildSelfUserEntity(data);
    }

    private void fetchGuildBaseData() throws InterruptedException {
        List<JsonNode> data = getGateway().executePaginationRequest(RestRoute.Guild.GET_GUILD_LIST.compile());
        for (JsonNode item : data) {
            String guildId = item.get("id").asText();
            fetchGuildData(guildId);
            fetchGuildUserData(guildId);
            fetchGuildEmojiData(guildId);
        }
    }

    private void fetchGuildData(String guildId) {
        JsonNode node = getGateway().executeRequest(RestRoute.Guild.GET_GUILD_INFO.compile().withQueryParam("guild_id", guildId));
        GuildEntity guild = getGateway().getKaiheilaBot().getEntitiesBuilder().buildGuild(node);
        ArrayList<String> channelId = new ArrayList<>();
        for (JsonNode channel : node.get("channels")) {
            ChannelEntity entity = getGateway().getKaiheilaBot().getEntitiesBuilder().buildChannelEntity(channel);
            entity.setGuild(guild);
            this.channelCache.updateElementById(entity.getId(), entity);
            channelId.add(entity.getId());
        }
        guild.setChannels(channelId);
        ArrayList<Integer> roleId = new ArrayList<>();
        for (JsonNode role : node.get("roles")) {
            RoleEntity entity = getGateway().getKaiheilaBot().getEntitiesBuilder().buildRoleEntity(role);
            entity.setGuild(guild);
            if (entity.getRoleId() != 0) {
                this.roleCache.updateElementById(entity.getRoleId(), entity);
                roleId.add(entity.getRoleId());
            } else {
                guild.setDefaultRole(entity);
            }
        }
        guild.setRoles(roleId);
        this.guildCache.updateElementById(guild.getId(), guild);
    }

    private void fetchGuildUserData(String guildId) {
        List<JsonNode> members = getGateway().executePaginationRequest(RestRoute.Guild.GET_GUILE_MEMBER_LIST.compile().withQueryParam("guild_id", guildId));
        HashMap<String, GuildUserEntity> guildUserEntities = new HashMap<>();
        for (JsonNode items : members) {
            UserEntity userEntity = getGateway().getKaiheilaBot().getEntitiesBuilder().buildUserEntity(items);
            this.userCache.updateElementById(userEntity.getId(), userEntity);
            GuildUserEntity guildUserEntity = getGateway().getKaiheilaBot().getEntitiesBuilder().buildGuildUserEntity(items);
            guildUserEntity.setGuild(this.getGuildCache().getElementById(guildId));
            guildUserEntities.put(guildUserEntity.getId(), guildUserEntity);
        }
        this.guildUsersCache.put(guildId, guildUserEntities);
    }

    private void fetchGuildEmojiData(String guildId) {
        List<JsonNode> emojis = getGateway().executePaginationRequest(RestRoute.GuildEmoji.GUILD_EMOJI_LIST.compile().withQueryParam("guild_id", guildId));
        List<String> emojiList = new ArrayList<>();
        emojis.forEach(node -> {
            for (JsonNode items : emojis) {
                EmojiEntity emojiEntity = getGateway().getKaiheilaBot().getEntitiesBuilder().buildGuildEmojiEntity(items);
                emojiList.add(emojiEntity.getId());
                this.guildEmojisCache.updateElementById(guildId, emojiEntity);
            }
        });
        this.guildCache.getElementById(guildId).setEmojis(emojiList);
    }

    public SelfUserEntity getSelfUserCache() {
        return selfUserCache;
    }

    public ICacheView<String, GuildEntity> getGuildCache() {
        return guildCache;
    }

    public ICacheView<Integer, RoleEntity> getRoleCache() {
        return roleCache;
    }

    public ICacheView<String, ChannelEntity> getChannelCache() {
        return channelCache;
    }

    public ICacheView<String, UserEntity> getUserCache() {
        return userCache;
    }

    public ConcurrentHashMap<String, Map<String, GuildUserEntity>> getGuildUsersCache() {
        return guildUsersCache;
    }

    public ICacheView<String, EmojiEntity> getGuildEmojisCache() {
        return guildEmojisCache;
    }
}
