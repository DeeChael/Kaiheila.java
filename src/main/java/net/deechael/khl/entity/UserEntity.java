package net.deechael.khl.entity;

import net.deechael.khl.RabbitImpl;
import net.deechael.khl.api.objects.User;
import net.deechael.khl.core.RabbitObject;

public class UserEntity extends RabbitObject implements User {

    private String id;
    private String username;
    private String identifyNum;
    private boolean online;
    private boolean bot;
    private int status;
    private String banner;
    private String avatar;
    private String vipAvatar;
    private boolean mobileVerified;

    public UserEntity(RabbitImpl rabbit) {
        super(rabbit);
    }

    public String getName() {
        return username + "#" + identifyNum;
    }

    /**
     * 开黑啦唯一表示符 用户 Id
     *
     * @return 用户 Id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * 当前用户名，不带标识数字
     *
     * @return 用户名
     * @see User#getIdentifyNum()
     * @see User#getFullName()
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 当前用户名，带标识数字
     *
     * @return 带标识数字的用户名
     * @see User#getIdentifyNum()
     * @see User#getUsername()
     */
    @Override
    public String getFullName() {
        return getName();
    }

    /**
     * 当前标识数字
     *
     * @return 用户标识数字
     * @see User#getUsername()
     * @see User#getFullName()
     */
    @Override
    public String getIdentifyNum() {
        return identifyNum;
    }

    /**
     * 当前用户是否在线
     *
     * @return true 当前用户在线，否则用户当前离线
     */
    @Override
    public boolean isOnline() {
        return online;
    }

    /**
     * 当前用户是否机器人
     *
     * @return true 机器人用户，否则为普通用户
     */
    @Override
    public boolean isBot() {
        return bot;
    }

    /**
     * 当前用户是否被开黑啦封禁
     *
     * @return true 用户被平台封禁，否则为正常用户
     */
    @Override
    public boolean isBanned() {
        return status == 0 || status == 1;
    }

    /**
     * 当前用户Buff的个人横幅资源路径
     *
     * @return 个人横幅资源路径
     */
    @Override
    public String getBanner() {
        return banner;
    }

    /**
     * 当前用户普通头像资源路径
     *
     * @return 头像资源路径
     */
    @Override
    public String getAvatarUrl() {
        return avatar;
    }

    /**
     * 当前用户Buff头像资源路径
     *
     * @return Buff头像资源路径
     */
    @Override
    public String getVipAvatarUrl() {
        return vipAvatar;
    }

    /**
     * 当前用户是否已验证手机号码
     *
     * @return true 用户已经通过手机验证，否则为未验证用户
     */
    @Override
    public boolean isMobileVerified() {
        return mobileVerified;
    }

    // getter / setter

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIdentifyNum(String identifyNum) {
        this.identifyNum = identifyNum;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getVipAvatar() {
        return vipAvatar;
    }

    public void setVipAvatar(String vipAvatar) {
        this.vipAvatar = vipAvatar;
    }

    public void setMobileVerified(boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }
}
