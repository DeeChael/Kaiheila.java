package net.deechael.khl.entity;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.*;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.gate.Gateway;
import net.deechael.khl.restful.RestRoute;

import java.util.HashMap;
import java.util.Map;

public abstract class ChannelEntity extends KaiheilaObject implements Channel {

    private String id;
    private String name;
    private String masterId;
    private String guildId;
    private String topic;
    private boolean category;
    private String parentId;
    private int level;
    private int slowMode;
    private int type;
    private int limitAmount;
    private Map<Integer, PermissionOverwriteEntity> permissionOverwrites;
    private Map<String, PermissionOverwriteEntity> permissionUsers;
    private boolean permissionSync;
    private Guild guild;

    public ChannelEntity(Gateway gateway, JsonNode node) {
        this(gateway, node, false);
    }

    public ChannelEntity(Gateway gateway, JsonNode node, boolean isEvent) {
        super(gateway);
        if (!isEvent) {
            this.setId(node.get("id").asText());
            this.setType(node.get("type").asInt());
            this.setName(node.get("name").asText());
            this.setMasterId(node.get("master_id").asText());
            this.setGuildId(node.get("guild_id").asText());
            this.setTopic(node.get("topic").asText());
            this.setCategory(node.get("is_category").asBoolean());
            this.setParentId(node.get("parent_id").asText());
            this.setLevel(node.get("level").asInt());
            this.setSlowMode(node.get("slow_mode").asInt());
            this.setLimitAmount(node.get("limit_amount").asInt());
            this.permissionOverwrites = new HashMap<>();
            this.permissionUsers = new HashMap<>();
            node.get("permission_overwrites").forEach(item -> {
                PermissionOverwriteEntity overwrite = new PermissionOverwriteEntity(this);
                overwrite.setOld();
                overwrite.setAllow(item.get("allow").asInt());
                overwrite.setDeny(item.get("deny").asInt());
                overwrite.setTargetRoleId(item.get("role_id").asInt());
                ChannelEntity.this.permissionOverwrites.put(item.get("role_id").asInt(), overwrite);
            });
            node.get("permission_users").forEach(item -> {
                PermissionOverwriteEntity overwrite = new PermissionOverwriteEntity(this);
                overwrite.setOld();
                overwrite.setAllow(item.get("allow").asInt());
                overwrite.setDeny(item.get("deny").asInt());
                overwrite.setTargetUserId(item.get("user").get("id").asText());
                ChannelEntity.this.permissionUsers.put(item.get("user").get("id").asText(), overwrite);
            });
            this.setPermissionSync(node.get("permission_sync").asInt() == 1);
        } else {
            this.setId(node.get("id").asText());
            this.setType(node.get("type").asInt());
            this.setName(node.get("name").asText());
            this.setGuildId(node.get("guild_id").asText());
            this.setTopic(node.get("topic").asText());
            this.setCategory(node.get("is_category").asBoolean());
            this.setParentId(node.get("parent_id").asText());
            this.setLevel(node.get("level").asInt());
            this.setSlowMode(node.get("slow_mode").asInt());
            this.permissionOverwrites = new HashMap<>();
            this.permissionUsers = new HashMap<>();
            this.setPermissionSync(true);
        }
    }

    /**
     * 开黑啦唯一标识符 频道 Id
     *
     * @return 频道 Id
     */
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 在服务器里的频道名称
     *
     * @return 频道名称
     */
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取频道创建者用户
     *
     * @return 创建者用户
     */
    @Override
    public User getCreator() {
        return getGateway().getKaiheilaBot().getCacheManager().getUserCache().getElementById(masterId);
    }

    /**
     * 当前频道是否为分组用途
     *
     * @return true 为分组特殊频道，否则为其他频道
     * @see Channel#getParentId()
     */
    @Override
    public boolean isCategory() {
        return category;
    }

    public void setCategory(boolean category) {
        this.category = category;
    }

    /**
     * 当前频道的上级频道 Id
     *
     * @return 上级频道 Id
     * @see Channel#isCategory()
     */
    @Override
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * 当前频道排列级别
     * <ul>
     *     <li>越大越靠后</li>
     *     <li>相对于其他频道级别</li>
     * </ul>
     *
     * @return 排列等级
     */
    @Override
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * 当前频道原始类型编号
     * <ul>
     *     <li>1 文字频道</li>
     *     <li>2 语音频道</li>
     * </ul>
     *
     * @return 原始频道类型编号
     */
    @Override
    public int getChannelTypeRaw() {
        return type;
    }

    /**
     * 当前频道的所属服务器
     *
     * @return 服务器实例
     */
    @Override
    public Guild getGuild() {
        return this.guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getSlowMode() {
        return slowMode;
    }

    public void setSlowMode(int slowMode) {
        this.slowMode = slowMode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(int limitAmount) {
        this.limitAmount = limitAmount;
    }

    public boolean isPermissionSync() {
        return permissionSync;
    }

    public void setPermissionSync(boolean permissionSync) {
        this.permissionSync = permissionSync;
    }

    public Map<String, PermissionOverwriteEntity> getPermissionUsers() {
        return permissionUsers;
    }

    public void setPermissionUsers(Map<String, PermissionOverwriteEntity> permissionUsers) {
        this.permissionUsers = permissionUsers;
    }

    public String createChannelInvite(InviteDuration duration, InviteTimes times) {
        JsonNode data = this.getGateway().executeRequest(RestRoute.Invite.CREATE_INVITE.compile()
                .withQueryParam("channel_id", this.getId())
                .withQueryParam("duration", duration)
                .withQueryParam("setting_times", times)
        );
        return data.get("url").asText();
    }

    @Override
    public void updateName(String name) {
        this.getGateway().executeRequest(RestRoute.Channel.UPDATE_CHANNEL.compile()
                .withQueryParam("channel_id", this.getId())
                .withQueryParam("name", name)
        );
    }

    @Override
    public void delete() {
        this.getGateway().executeRequest(RestRoute.Channel.DELETE_CHANNEL.compile()
                .withQueryParam("channel_id", this.getId())
        );
    }

    @Override
    public PermissionOverwrite getPermissionOverwrite(User user) {
        PermissionOverwriteEntity permissionOverwrite = this.permissionUsers.get(user.getId());
        return permissionOverwrite != null ? permissionOverwrite : new PermissionOverwriteEntity(this, user);
    }

    @Override
    public PermissionOverwrite getPermissionOverwrite(Role role) {
        PermissionOverwriteEntity permissionOverwrite = this.permissionOverwrites.get(role.getId());
        return permissionOverwrite != null ? permissionOverwrite : new PermissionOverwriteEntity(this, role);
    }

    public Map<Integer, PermissionOverwriteEntity> getPermissionOverwrites() {
        return this.permissionOverwrites;
    }

    public void setPermissionOverwrites(Map<Integer, PermissionOverwriteEntity> permissionOverwrites) {
        this.permissionOverwrites = permissionOverwrites;
    }

    public Map<String, PermissionOverwriteEntity> GETPermissionUsers() {
        return this.permissionUsers;
    }

}
