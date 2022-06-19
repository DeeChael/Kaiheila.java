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

package net.deechael.khl.api;

import net.deechael.khl.hook.EventListener;

import java.util.List;

/**
 * 开黑啦开放平台控制接口
 */
public interface Bot {

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

    Guild getGuild(String id);

    List<Guild> getGuilds();
}
