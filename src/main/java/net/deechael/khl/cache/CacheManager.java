package net.deechael.khl.cache;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.client.http.HttpCall;
import net.deechael.khl.client.http.HttpHeaders;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.entity.*;
import net.deechael.khl.restful.RestPageable;
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
    private final ConcurrentHashMap<String, Map<String, MemberEntity>> guildMembersCache = new ConcurrentHashMap<>();
//    private final ConcurrentHashMap<String, Map<String, MemberEntity>> guildInvites = new ConcurrentHashMap<>();

    final HttpHeaders defaultHeaders = new HttpHeaders();

    public CacheManager(KaiheilaBot rabbit) {
        super(rabbit);
        defaultHeaders.addHeader("Authorization", "Bot " + getKaiheilaBot().getConfiguration().getApiConfigurer().getToken());
    }

    public void unloadCache() {
        this.selfUserCache = null;
        this.guildCache.unloadAll();
        this.roleCache.unloadAll();
        this.channelCache.unloadAll();
        this.userCache.unloadAll();
        this.guildMembersCache.clear();
        this.guildEmojisCache.unloadAll();
    }

    public boolean checkTokenAvailable() {
        if (getKaiheilaBot().getConfiguration().getApiConfigurer().getToken().isEmpty()) return false;
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
        RestRoute.CompiledRoute route = RestRoute.Misc.SELF_USER.compile();
        HttpCall request = HttpCall.createRequest(route.getMethod(), getCompleteUrl(route), this.defaultHeaders);
        List<JsonNode> data = getRestJsonResponse(route, request);
        if (data != null) {
            this.selfUserCache = getKaiheilaBot().getEntitiesBuilder().buildSelfUserEntity(getRestApiData(data.get(0)));
        }
    }

    private void fetchGuildBaseData() throws InterruptedException {
        RestRoute.CompiledRoute guildListRoute = RestRoute.Guild.GET_GUILD_LIST.compile();
        HttpCall guildListRequest = HttpCall.createRequest(guildListRoute.getMethod(), getCompleteUrl(guildListRoute), this.defaultHeaders);
        List<JsonNode> guildList = getRestJsonResponse(guildListRoute, guildListRequest);
        if (guildList == null) {
            Log.error("Failed to fetch guild list."); //todo i18n
            return;
        }
        for (JsonNode list : guildList) {
            for (JsonNode item : getRestApiData(list).get("items")) {
                String guildId = item.get("id").asText();
                fetchGuildData(guildId);
                fetchGuildMemberData(guildId);
                fetchGuildEmojiData(guildId);
            }
        }
    }

    private void fetchGuildData(String guildId) throws InterruptedException {
        RestRoute.CompiledRoute guildRoute = RestRoute.Guild.GET_GUILD_INFO.compile().withQueryParam("guild_id", guildId);
        HttpCall guildIdRequest = HttpCall.createRequest(guildRoute.getMethod(), getCompleteUrl(guildRoute), this.defaultHeaders);
        List<JsonNode> guilds = getRestJsonResponse(guildRoute, guildIdRequest);
        if (guilds == null) {
            Log.error("Failed to fetch guild data."); //todo i18n
            return;
        }
        JsonNode node = getRestApiData(guilds.get(0));
        GuildEntity guild = getKaiheilaBot().getEntitiesBuilder().buildGuild(node);
        ArrayList<String> channelId = new ArrayList<>();
        for (JsonNode channels : node.get("channels")) {
            ChannelEntity entity = getKaiheilaBot().getEntitiesBuilder().buildChannelEntity(channels);
            this.channelCache.updateElementById(entity.getId(), entity);
            channelId.add(entity.getId());
        }
        guild.setChannels(channelId);
        ArrayList<Integer> roleId = new ArrayList<>();
        for (JsonNode role : node.get("roles")) {
            RoleEntity entity = getKaiheilaBot().getEntitiesBuilder().buildRoleEntity(role);
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

    private void fetchGuildMemberData(String guildId) throws InterruptedException {
        RestRoute.CompiledRoute guildMemberRoute = RestRoute.Guild.GET_GUILE_MEMBER_LIST.compile().withQueryParam("guild_id", guildId);
        HttpCall guildIdRequest = HttpCall.createRequest(guildMemberRoute.getMethod(), getCompleteUrl(guildMemberRoute), this.defaultHeaders);
        List<JsonNode> members = getRestJsonResponse(guildMemberRoute, guildIdRequest);
        if (members == null) {
            Log.error("Failed to fetch guild member list."); //todo i18n
            return;
        }
        HashMap<String, MemberEntity> memberEntities = new HashMap<>();
        members.forEach(node -> {
            JsonNode data = getRestApiData(node);
            for (JsonNode items : data.get("items")) {
                UserEntity userEntity = getKaiheilaBot().getEntitiesBuilder().buildUserEntity(items);
                this.userCache.updateElementById(userEntity.getId(), userEntity);
                MemberEntity memberEntity = getKaiheilaBot().getEntitiesBuilder().buildMemberEntity(items);
                memberEntities.put(memberEntity.getUserId(), memberEntity);
            }
            this.updateGuildUserCount(guildId, data);
        });
        this.guildMembersCache.put(guildId, memberEntities);
    }

    private void fetchGuildEmojiData(String guildId) throws InterruptedException {
        RestRoute.CompiledRoute guildEmojiRoute = RestRoute.GuildEmoji.GUILD_EMOJI_LIST.compile().withQueryParam("guild_id", guildId);
        HttpCall guildEmojiRequest = HttpCall.createRequest(guildEmojiRoute.getMethod(), getCompleteUrl(guildEmojiRoute), this.defaultHeaders);
        List<JsonNode> emojis = getRestJsonResponse(guildEmojiRoute, guildEmojiRequest);
        if (emojis == null) {
            Log.error("Failed to fetch guild emojis for guild " + guildId);
            return;
        }
        List<String> emojiList = new ArrayList<>();
        emojis.forEach(node -> {
            for (JsonNode items : getRestApiData(node).get("items")) {
                EmojiEntity emojiEntity = getKaiheilaBot().getEntitiesBuilder().buildGuildEmojiEntity(items);
                emojiList.add(emojiEntity.getId());
                this.guildEmojisCache.updateElementById(guildId, emojiEntity);
            }
        });
        this.guildCache.getElementById(guildId).setEmojis(emojiList);
    }

    private void updateGuildUserCount(String guildId, JsonNode data) {
        GuildEntity guild = this.getGuildCache().getElementById(guildId);
        guild.setUserCount(data.get("user_count").asInt());
        guild.setOnlineCount(data.get("online_count").asInt());
        guild.setOfflineCount(data.get("offline_count").asInt());
    }

    private List<JsonNode> getRemainPageRestData(RestRoute.CompiledRoute compiledRoute, JsonNode data) throws InterruptedException {
        ArrayList<JsonNode> result = new ArrayList<>();
        RestPageable pageable = RestPageable.of(getKaiheilaBot(), compiledRoute, data);
        while (pageable.hasNext()) {
            RestRoute.CompiledRoute nextRoute = pageable.next();
            HttpCall nextCall = HttpCall.createRequest(nextRoute.getMethod(), getCompleteUrl(nextRoute), this.defaultHeaders);
            JsonNode next = callRestApi(nextCall);
            if (next == null) {
                continue;
            }
            result.add(next);
        }
        return result;
    }

    private JsonNode getRestApiData(JsonNode node) {
        return node.get("data");
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

    public ConcurrentHashMap<String, Map<String, MemberEntity>> getGuildMembersCache() {
        return guildMembersCache;
    }

    public ICacheView<String, EmojiEntity> getGuildEmojisCache() {
        return guildEmojisCache;
    }
}
