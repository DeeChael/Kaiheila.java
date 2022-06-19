package net.deechael.khl.api;

/**
 * 当前 Token 登录用户
 */
public interface SelfUser {

    /**
     * 获取当前用户信息
     *
     * @return 用户
     */
    User getUser();
}
