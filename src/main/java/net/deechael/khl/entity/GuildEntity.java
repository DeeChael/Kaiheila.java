package net.deechael.khl.entity;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.*;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.gate.Gateway;
import net.deechael.khl.restful.RestRoute;
import net.deechael.khl.type.ChannelTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GuildEntity extends KaiheilaObject implements Guild {

    private String id;
    private String name;
    private String topic;
    private String masterId;
    private String icon;
    private int notifyType;
    private String region;
    private boolean enableOpen;
    private String openId;
    private String defaultChannelId;
    private String welcomeChannelId;
    private RoleEntity defaultRole;
    private List<Integer> roles;
    private List<String> channels;
    private List<String> emojis;
    private int userCount;
    private int onlineCount;
    private int offlineCount;

    private GuildStatusEntity status = null;

    private final List<GuildUser> users = new ArrayList<>();

    public GuildEntity(Gateway gateway, JsonNode node) {
        super(gateway);
        this.setId(node.get("id").asText());
        this.setTopic(node.get("topic").asText());
        this.setMasterId(node.get("master_id").asText());
        this.setName(node.get("name").asText());
        this.setIcon(node.get("icon").asText());
        this.setNotifyType(node.get("notify_type").asInt());
        this.setRegion(node.get("region").asText());
        this.setEnableOpen(node.get("enable_open").asBoolean());
        this.setOpenId(node.get("open_id").asText());
        this.setDefaultChannelId(node.get("default_channel_id").asText());
        this.setWelcomeChannelId(node.get("welcome_channel_id").asText());
    }

    /**
     * 开黑啦唯一标识符 服务器 Id
     * 不可用作为 OpenId
     *
     * @return 频道 Id
     * @see Guild#getGuildOpenId()
     */
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 服务器名称
     *
     * @return 服务器名称
     */
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 服务器主题
     *
     * @return 服务器主题
     */
    @Override
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * 服务器主用户
     *
     * @return 服务器主用户
     */
    @Override
    public User getCreator() {
        return getGateway().getKaiheilaBot().getCacheManager().getUserCache().getElementById(masterId);
    }

    /**
     * 当前服务器图标唯一资源路径
     *
     * @return 图标唯一资源路径
     */
    @Override
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 当前服务器语音服务器区域
     *
     * @return 语音服务器地区
     */
    @Override
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * 当前服务器是否为公开服务器
     *
     * @return true 当前为公开服务器，否则为非公开服务器
     * @see Guild#getGuildOpenId()
     */
    @Override
    public boolean isPublicServer() {
        return enableOpen;
    }

    /**
     * 当前服务器公开编号
     *
     * @return 服务器公开编号
     * @see Guild#isPublicServer()
     */
    @Override
    public String getGuildOpenId() {
        return openId;
    }

    /**
     * 当前服务器默认频道
     *
     * @return 默认频道
     */
    @Override
    public Channel getDefaultChannel() {
        return getGateway().getKaiheilaBot().getCacheManager().getChannelCache().getElementById(defaultChannelId);
    }

    /**
     * 当前服务器欢迎频道
     *
     * @return 欢迎频道
     */
    @Override
    public Channel getWelcomeChannel() {
        return getGateway().getKaiheilaBot().getCacheManager().getChannelCache().getElementById(welcomeChannelId);
    }

    /**
     * 当前服务器默认角色
     *
     * @return 服务器默认角色
     */
    @Override
    public Role getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(RoleEntity defaultRole) {
        this.defaultRole = defaultRole;
    }

    /**
     * 当前服务器总用户数
     *
     * @return 服务器总用户数
     */
    @Override
    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    /**
     * 当前服务器总在在线用户数
     *
     * @return 服务器在线用户数
     */
    @Override
    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    /**
     * 当前服务器总离线用户数
     *
     * @return 服务器离线用户数
     */
    @Override
    public int getOfflineCount() {
        return offlineCount;
    }

    public void setOfflineCount(int offlineCount) {
        this.offlineCount = offlineCount;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public int getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(int notifyType) {
        this.notifyType = notifyType;
    }

    public boolean isEnableOpen() {
        return enableOpen;
    }

    public void setEnableOpen(boolean enableOpen) {
        this.enableOpen = enableOpen;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getDefaultChannelId() {
        return defaultChannelId;
    }

    public void setDefaultChannelId(String defaultChannelId) {
        this.defaultChannelId = defaultChannelId;
    }

    public String getWelcomeChannelId() {
        return welcomeChannelId;
    }

    public void setWelcomeChannelId(String welcomeChannelId) {
        this.welcomeChannelId = welcomeChannelId;
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    public List<Channel> getChannels() {
        return channels.stream().map(id -> (Channel) getGateway().getKaiheilaBot().getCacheManager().getChannelCache().getElementById(id)).collect(Collectors.toList());
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    @Override
    public List<GuildUserEntity> getMembers() {
        return new ArrayList<>(getGateway().getKaiheilaBot().getCacheManager().getGuildUsersCache().get(this.id).values());
    }

    public List<String> getChannelIDs() {
        return channels;
    }

    public List<String> getEmojis() {
        return emojis;
    }

    public void setEmojis(List<String> emojis) {
        this.emojis = emojis;
    }

    public String createServerInvite(Channel.InviteDuration duration, Channel.InviteTimes times) {
        JsonNode data = this.getGateway().executeRequest(RestRoute.Invite.CREATE_INVITE.compile()
                .withQueryParam("guild_id", this.getId())
                .withQueryParam("duration", duration)
                .withQueryParam("setting_times", times)
        );
        return data.get("url").asText();
    }

    public List<Channel> getJoinedChannel(User user) {
        List<Channel> channels = new ArrayList<>();
        List<JsonNode> data = this.getGateway().executePaginationRequest(RestRoute.ChannelUser.GET_JOINED_CHANNEL.compile()
                .withQueryParam("guild_id", this.getId())
                .withQueryParam("user_id", user.getId())
        );
        for (JsonNode channelData : data) {
            channels.add(getGateway().getKaiheilaBot().getCacheManager().getChannelCache().getElementById(channelData.get("id").asText()));
        }
        return channels;
    }

    public void addUser(GuildUser user) {
        this.users.add(user);
    }

    @Override
    public List<GuildUser> getUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public GuildStatus getStatus() {
        JsonNode node = getGateway().executeRequest(RestRoute.Guild.GET_GUILD_USER_LIST.compile().withQueryParam("guild_id", this.getId()));
        GuildStatusEntity status = this.status != null ? this.status : new GuildStatusEntity(this.getGateway(), this);
        status.setUsers(node.get("user_count").asInt());
        status.setOnlineUsers(node.get("online_count").asInt());
        status.setOfflineUsers(node.get("offline_count").asInt());
        return this.status = status;
    }

    @Override
    public Channel createChannel(Category parent, ChannelTypes type, String name) {
        return parent.createChannel(type, name);
    }

    @Override
    public TextChannel createTextChannel(Category parent, String name) {
        return parent.createTextChannel(name);
    }

    @Override
    public VoiceChannel createVoiceChannel(Category parent, String name, int limit, VoiceChannel.Quality quality) {
        return parent.createVoiceChannel(name, limit, quality);
    }

    private static class GuildStatusEntity implements GuildStatus {

        private final Gateway gateway;
        private final Guild guild;

        private int users = 0;
        private int onlineUsers = 0;
        private int offlineUsers = 0;

        public GuildStatusEntity(Gateway gateway, Guild guild) {
            this.gateway = gateway;
            this.guild = guild;
        }

        @Override
        public int getUsers() {
            return this.users;
        }

        public void setUsers(int users) {
            this.users = users;
        }

        @Override
        public int getOnlineUsers() {
            return this.onlineUsers;
        }

        public void setOnlineUsers(int onlineUsers) {
            this.onlineUsers = onlineUsers;
        }

        @Override
        public int getOfflineUsers() {
            return this.offlineUsers;
        }

        @Override
        public void update() {
            JsonNode node = gateway.executeRequest(RestRoute.Guild.GET_GUILD_USER_LIST.compile().withQueryParam("guild_id", this.getGuild().getId()));
            this.setUsers(node.get("user_count").asInt());
            this.setOnlineUsers(node.get("online_count").asInt());
            this.setOfflineUsers(node.get("offline_count").asInt());
        }

        @Override
        public Guild getGuild() {
            return guild;
        }

        public void setOfflineUsers(int offlineUsers) {
            this.offlineUsers = offlineUsers;
        }

    }

}
