/*
 *    Copyright 2020-2021 Rabbit author and contributors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.deechael.khl.bot;

import net.deechael.khl.api.Guild;
import net.deechael.khl.api.SelfUser;
import net.deechael.khl.hook.EventListener;

import java.util.List;

/**
 * 开黑啦开放平台控制接口
 */
public interface Bot {

    /**
     * 版本号
     */
    String RABBIT_NAME = "Rabbit";

    /**
     * Rabbit 版本信息
     */
    interface Version {
        /**
         * 主版本号 Major
         */
        int MAJOR_VERSION = 0;
        /**
         * 次版本号 Minor
         */
        int MINOR_VERSION = 0;
        /**
         * 修订号 Patch
         */
        int PATCH_VERSION = 1;
    }


    /**
     * 使用当前配置登录平台
     *
     * @return 开放平台接口实例
     */
    boolean login();

    /**
     * 安全退出当前平台
     */
    void shutdown();

    /**
     * 获取当前 Token 所登录的用户实例
     *
     * @return 当前用户
     */
    SelfUser getSelf();

    /**
     * 添加事件监听器
     *
     * @param listener 用户事件
     * @return 开放平台接口实例
     */
    Bot addEventListener(EventListener listener);


    /**
     * 移除事件监听器
     *
     * @param listener 用户事件
     * @return 开放平台接口实例
     */
    Bot removeEventListener(EventListener listener);

    /**
     * Rabbit 软件全名带版本号
     *
     * @return 软件全名带版本号
     * @see Bot#RABBIT_NAME
     * @see Bot#getRabbitVersion()
     * @see Bot#getRabbitVersionInteger()
     */
    static String getSoftwareFullName() {
        return RABBIT_NAME + "@" + getRabbitVersion();
    }

    /**
     * Rabbit 依赖库当前版本
     *
     * @return 版本号
     * @see Bot#RABBIT_NAME
     * @see Bot#getSoftwareFullName()
     * @see Bot#getRabbitVersionInteger()
     */
    static String getRabbitVersion() {
        return Version.MAJOR_VERSION + "." + Version.MINOR_VERSION + "." + Version.PATCH_VERSION;
    }

    /**
     * Rabbit 依赖库当前版本，数字版本号
     * <ul>
     *     <li>0.0.1 = 1</li>
     *     <li>1.10.1 = 11001</li>
     *     <li>2.8.5 = 20805</li>
     * </ul>
     * <p>
     *     MAJOR.MINOR.PATCH = MAJOR * 10000 + MINOR * 100 + PATCH
     * </p>
     *
     * @return 数字版本号
     * @see Bot#RABBIT_NAME
     * @see Bot#getSoftwareFullName()
     * @see Bot#getRabbitVersion()
     */
    static int getRabbitVersionInteger() {
        return Version.MAJOR_VERSION * 10000 + Version.MINOR_VERSION * 100 + Version.PATCH_VERSION;
    }

    Guild getGuild(String id);

    List<Guild> getGuilds();
}
