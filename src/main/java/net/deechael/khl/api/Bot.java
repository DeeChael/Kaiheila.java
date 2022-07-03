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

import net.deechael.khl.command.KaiheilaCommandBuilder;
import net.deechael.khl.event.Listener;

import java.io.File;
import java.util.List;

/**
 * 开黑啦开放平台控制接口
 */
public interface Bot extends KHLObject {

    /**
     * 使用当前配置登录平台
     *
     * @return 开放平台接口实例
     */
    boolean start();

    /**
     * 安全退出当前平台
     */
    void shutdown();

    /**
     * 获取当前 Token 所登录的用户实例
     *
     * @return 当前用户
     */
    User getSelf();

    /**
     * 添加事件监听器
     *
     * @param listener 用户事件
     */
    void addEventListener(Listener listener);


    /**
     * 移除事件监听器
     *
     * @param listener 用户事件
     */
    void removeEventListener(Listener listener);

    Guild getGuild(String id);

    List<Guild> getGuilds();

    void registerCommand(KaiheilaCommandBuilder command);

    Channel getChannel(String id);

    User getUser(String id);

    List<Game> getGames();

    void play(Game game);

    void stopPlaying();

    Game createGame(String name);

    void updateGameName(Game game, String name);

    void updateGameIcon(Game game, String icon);

    void deleteGame(Game game);

    String uploadAsset(File file);

}
