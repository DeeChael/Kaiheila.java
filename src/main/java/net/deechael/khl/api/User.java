package net.deechael.khl.api;

/**
 * 开黑啦用户，每一个用户为独立实体
 */
public interface User {

    /**
     * 开黑啦唯一表示符 用户 Id
     *
     * @return 用户 Id
     */
    String getId();

    /**
     * 当前用户名，不带标识数字
     *
     * @return 用户名
     * @see User#getIdentifyNum()
     * @see User#getFullName()
     */
    String getUsername();

    /**
     * 当前用户名，带标识数字
     *
     * @return 带标识数字的用户名
     * @see User#getIdentifyNum()
     * @see User#getUsername()
     */
    String getFullName();

    /**
     * 当前标识数字
     *
     * @return 用户标识数字
     * @see User#getUsername()
     * @see User#getFullName()
     */
    String getIdentifyNum();

    /**
     * 当前用户是否在线
     *
     * @return true 当前用户在线，否则用户当前离线
     */
    boolean isOnline();

    /**
     * 当前用户是否机器人
     *
     * @return true 机器人用户，否则为普通用户
     */
    boolean isBot();

    /**
     * 当前用户是否被开黑啦封禁
     *
     * @return true 用户被平台封禁，否则为正常用户
     */
    boolean isBanned();

    /**
     * 当前用户Buff的个人横幅资源路径
     *
     * @return 个人横幅资源路径
     * @see User#getAvatarUrl()
     * @see User#getVipAvatarUrl()
     */
    String getBanner();

    /**
     * 当前用户普通头像资源路径
     *
     * @return 头像资源路径
     * @see User#getVipAvatarUrl()
     * @see User#getBanner()
     */
    String getAvatarUrl();

    /**
     * 当前用户Buff头像资源路径
     *
     * @return Buff头像资源路径
     * @see User#getAvatarUrl()
     * @see User#getBanner()
     */
    String getVipAvatarUrl();

    /**
     * 当前用户是否已验证手机号码
     *
     * @return true 用户已经通过手机验证，否则为未验证用户
     */
    boolean isMobileVerified();
}
