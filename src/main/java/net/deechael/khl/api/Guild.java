package net.deechael.khl.api;

import net.deechael.khl.entity.GuildUserEntity;
import net.deechael.khl.type.ChannelTypes;

import java.util.List;

/**
 * 服务器
 */
public interface Guild extends KHLObject {

    /**
     * 开黑啦唯一标识符 服务器 Id
     * 不可用作为 OpenId
     *
     * @return 频道 Id
     * @see Guild#getGuildOpenId()
     */
    String getId();

    /**
     * 服务器名称
     *
     * @return 服务器名称
     */
    String getName();

    /**
     * 服务器主题
     *
     * @return 服务器主题
     */
    String getTopic();

    /**
     * 服务器主用户
     *
     * @return 服务器主用户
     */
    User getCreator();

    /**
     * 当前服务器图标唯一资源路径
     *
     * @return 图标唯一资源路径
     */
    String getIcon();

    /**
     * 当前服务器语音服务器区域
     *
     * @return 语音服务器地区
     */
    String getRegion();

    /**
     * 当前服务器是否为公开服务器
     *
     * @return true 当前为公开服务器，否则为非公开服务器
     * @see Guild#getGuildOpenId()
     */
    boolean isPublicServer();

    /**
     * 当前服务器公开编号
     *
     * @return 服务器公开编号
     * @see Guild#isPublicServer()
     */
    String getGuildOpenId();

    /**
     * 当前服务器默认频道
     *
     * @return 默认频道
     */
    Channel getDefaultChannel();

    /**
     * 当前服务器欢迎频道
     *
     * @return 欢迎频道
     */
    Channel getWelcomeChannel();

    /**
     * 当前服务器默认角色
     *
     * @return 服务器默认角色
     */
    Role getDefaultRole();

    /**
     * 当前服务器总用户数
     *
     * @return 服务器总用户数
     */
    int getUserCount();

    /**
     * 当前服务器总在在线用户数
     *
     * @return 服务器在线用户数
     */
    int getOnlineCount();

    /**
     * 当前服务器总离线用户数
     *
     * @return 服务器离线用户数
     */
    int getOfflineCount();

    List<String> getChannelIDs();

    List<Channel> getChannels();

    List<GuildUserEntity> getMembers();

    String createServerInvite(Channel.InviteDuration duration, Channel.InviteTimes times);

    List<Channel> getJoinedChannel(User user);

    Channel createChannel(Category parent, ChannelTypes type, String name);

    TextChannel createTextChannel(Category parent, String name);

    VoiceChannel createVoiceChannel(Category parent, String name, int limit, VoiceChannel.Quality quality);

    List<GuildUser> getUsers();

    GuildStatus getStatus();

    GuildUser getUser(User user);

    Category createCategory(String name);

    interface GuildStatus {

        int getUsers();

        int getOnlineUsers();

        int getOfflineUsers();

        void update();

        Guild getGuild();

    }

}
