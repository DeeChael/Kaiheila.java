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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import net.deechael.khl.api.Bot;
import net.deechael.khl.api.Channel;
import net.deechael.khl.api.Guild;
import net.deechael.khl.api.User;
import net.deechael.khl.cache.CacheManager;
import net.deechael.khl.client.http.HttpCall;
import net.deechael.khl.client.http.HttpHeaders;
import net.deechael.khl.client.http.IHttpClient;
import net.deechael.khl.client.ws.IWebSocketClient;
import net.deechael.khl.command.CommandManager;
import net.deechael.khl.command.KaiheilaCommandBuilder;
import net.deechael.khl.configurer.KaiheilaConfiguration;
import net.deechael.khl.event.MessageHandler;
import net.deechael.khl.gate.Gateway;
import net.deechael.khl.hook.EventListener;
import net.deechael.khl.hook.EventManager;
import net.deechael.khl.message.ReceivedChannelMessage;
import net.deechael.khl.restful.Requester;
import net.deechael.khl.restful.RestRoute;
import net.deechael.khl.task.KaiheilaScheduler;
import net.deechael.khl.task.TaskScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class KaiheilaBot implements Bot {

    protected static final Logger Log = LoggerFactory.getLogger(KaiheilaBot.class);

    private final KaiheilaConfiguration kaiheilaConfiguration;
    private final CacheManager cacheManager;
    private final EventManager eventManager;
    private final Requester requester;
    private final IHttpClient httpClient;
    private final IWebSocketClient websocketClient;
    private final ObjectMapper jsonEngine;
    private final CommandManager commandManager;
    private final TaskScheduler scheduler;
    private final MessageHandler defaultMessageHandler;
    private final Gateway gateway;
    private boolean botLogged;

    public KaiheilaBot(KaiheilaConfiguration kaiheilaConfiguration) {
        Gateway gateway = new Gateway(this);
        this.kaiheilaConfiguration = kaiheilaConfiguration;
        this.httpClient = kaiheilaConfiguration.getClientConfigurer().getHttpClientFactory().buildHttpClient();
        this.websocketClient = kaiheilaConfiguration.getClientConfigurer().getWebSocketClientFactory().buildWebSocketClient();
        this.jsonEngine = buildJsonEngine();
        this.requester = new Requester(gateway, 4);
        this.cacheManager = new CacheManager(gateway);
        this.eventManager = new EventManager(gateway);
        this.commandManager = new CommandManager(gateway);
        this.defaultMessageHandler = new MessageHandler() {
            @Override
            public void onText(ReceivedChannelMessage message) {
                KaiheilaBot.this.getCommandManager().execute(message);
            }

            @Override
            public void onKMarkdown(ReceivedChannelMessage message) {
                KaiheilaBot.this.getCommandManager().execute(message);
            }

            @Override
            public void onCardMessage(ReceivedChannelMessage message) {
                message.reply(message.getMessage());
            }
        };
        this.getEventManager().register(defaultMessageHandler);
        this.scheduler = new KaiheilaScheduler(gateway);
        this.gateway = gateway;
        this.gateway.initialize();
    }

    private static ObjectMapper buildJsonEngine() {
        return new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    /**
     * 获取当前 Token 所登录的用户实例
     *
     * @return 当前用户
     */
    @Override
    public User getSelf() {
        return getCacheManager().getSelfCache();
    }

    /**
     * 使用当前配置登录平台
     *
     * @return 开放平台接口实例
     */
    @Override
    public boolean start() {
        if (!botLogged) {
            if (!this.cacheManager.checkTokenAvailable()) {
                Log.error("用户Token无效，请检查当前是否可用");
                return false;
            }
            this.cacheManager.updateCache();
            this.eventManager.initializeEventSource();
        }
        botLogged = true;
        return true;
    }

    /**
     * 安全退出当前平台
     */
    @Override
    public void shutdown() {
        if (botLogged) {
            this.eventManager.shutdown();
            this.cacheManager.unloadCache();
            this.botOffline();
        }
    }

    /**
     * 添加事件监听器
     *
     * @param listener 用户事件
     * @return 开放平台接口实例
     */
    @Override
    public Bot addEventListener(EventListener listener) {
        this.eventManager.getListeners().add(listener);
        return this;
    }

    /**
     * 移除事件监听器
     *
     * @param listener 用户事件
     * @return 开放平台接口实例
     */
    @Override
    public Bot removeEventListener(EventListener listener) {
        this.eventManager.getListeners().remove(listener);
        return this;
    }

    private void botOffline() {
        RestRoute.CompiledRoute route = RestRoute.Misc.BOT_OFFLINE.compile();
        HttpHeaders headers = new HttpHeaders();
        headers.addHeader("Authorization", "Bot " + this.getConfiguration().getApiConfigurer().getToken());
        String requestUrl = this.kaiheilaConfiguration.getApiConfigurer().getBaseUrl() + route.getQueryStringCompleteRoute();
        HttpCall request = HttpCall.createRequest(route.getMethod(), requestUrl, headers);
        try {
            this.getHttpClient().execute(request);
            if (request.getResponse().getCode() != 200) {
                Log.warn("安全退出失败 - 发送机器人离线请求失败");
            }
        } catch (IOException e) {
            Log.warn("安全退出失败 - 发送机器人离线请求失败", e);
        }
    }

    public Requester getRequester() {
        return requester;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public ObjectMapper getJsonEngine() {
        return jsonEngine;
    }

    public KaiheilaConfiguration getConfiguration() {
        return kaiheilaConfiguration;
    }

    public IHttpClient getHttpClient() {
        return httpClient;
    }

    public IWebSocketClient getWebsocketClient() {
        return websocketClient;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public TaskScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public Guild getGuild(String id) {
        return getCacheManager().getGuildCache().getElementById(id);
    }

    @Override
    public List<Guild> getGuilds() {
        return getCacheManager().getGuildCache().getAll().stream().map(id -> getCacheManager().getGuildCache().getElementById(id)).collect(Collectors.toList());
    }

    @Override
    public void registerCommand(KaiheilaCommandBuilder command) {
        this.getCommandManager().register(command);
    }

    @Override
    public Channel getChannel(String id) {
        return getCacheManager().getChannelCache().getElementById(id);
    }

    @Override
    public User getUser(String id) {
        return getCacheManager().getUserCache().getElementById(id);
    }

    @Override
    public Gateway getGateway() {
        return this.gateway;
    }
}
