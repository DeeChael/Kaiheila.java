package net.deechael.khl.entity;

import net.deechael.khl.RabbitImpl;
import cn.fightingguys.kaiheila.api.*;
import net.deechael.khl.api.objects.Role;
import net.deechael.khl.api.objects.User;
import net.deechael.khl.core.RabbitObject;
import net.deechael.khl.api.objects.Channel;
import net.deechael.khl.api.objects.Guild;

import java.util.List;

public class GuildEntity extends RabbitObject implements Guild {

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

    public GuildEntity(RabbitImpl rabbit) {
        super(rabbit);
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

    /**
     * 服务器名称
     *
     * @return 服务器名称
     */
    @Override
    public String getName() {
        return name;
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

    /**
     * 服务器主用户
     *
     * @return 服务器主用户
     */
    @Override
    public User getCreator() {
        return getRabbitImpl().getCacheManager().getUserCache().getElementById(masterId);
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

    /**
     * 当前服务器语音服务器区域
     *
     * @return 语音服务器地区
     */
    @Override
    public String getRegion() {
        return region;
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
        return getRabbitImpl().getCacheManager().getChannelCache().getElementById(defaultChannelId);
    }

    /**
     * 当前服务器欢迎频道
     *
     * @return 欢迎频道
     */
    @Override
    public Channel getWelcomeChannel() {
        return getRabbitImpl().getCacheManager().getChannelCache().getElementById(welcomeChannelId);
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

    /**
     * 当前服务器总用户数
     *
     * @return 服务器总用户数
     */
    @Override
    public int getUserCount() {
        return userCount;
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

    /**
     * 当前服务器总离线用户数
     *
     * @return 服务器离线用户数
     */
    @Override
    public int getOfflineCount() {
        return offlineCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(int notifyType) {
        this.notifyType = notifyType;
    }

    public void setRegion(String region) {
        this.region = region;
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

    public void setDefaultRole(RoleEntity defaultRole) {
        this.defaultRole = defaultRole;
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public List<String> getEmojis() {
        return emojis;
    }

    public void setEmojis(List<String> emojis) {
        this.emojis = emojis;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public void setOfflineCount(int offlineCount) {
        this.offlineCount = offlineCount;
    }
}
