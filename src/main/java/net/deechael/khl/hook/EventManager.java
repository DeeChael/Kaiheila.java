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

package net.deechael.khl.hook;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.deechael.khl.configurer.event.EventSourceConfigurer;
import net.deechael.khl.configurer.event.WebhookEventSourceConfigurer;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.event.EventHandler;
import net.deechael.khl.event.IEvent;
import net.deechael.khl.event.Listener;
import net.deechael.khl.event.MessageHandler;
import net.deechael.khl.gate.Gateway;
import net.deechael.khl.hook.queue.SequenceMessageQueue;
import net.deechael.khl.hook.source.webhook.WebhookEventSource;
import net.deechael.khl.hook.source.websocket.WebSocketEventSource;
import net.deechael.khl.hook.source.websocket.session.storage.WebSocketSessionStorage;
import net.deechael.khl.message.*;
import net.deechael.khl.message.cardmessage.CardMessage;
import net.deechael.khl.message.kmarkdown.KMarkdownMessage;
import net.deechael.khl.util.compression.Compression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class EventManager extends KaiheilaObject implements EventManagerReceiver {
    protected static final Logger Log = LoggerFactory.getLogger(EventManager.class);
    private final Set<MessageHandler> messageHandlers = new HashSet<>();
    private final Map<Class<? extends IEvent>, Map<Method, Object>> handlers = new HashMap<>();
    private SequenceMessageQueue<String> messageQueue;
    private EventParser eventParser;
    private EventSource eventSource;

    public EventManager(Gateway gateway) {
        super(gateway);
    }

    @Override
    public void initialSn(int sn) {
        messageQueue = new SequenceMessageQueue<>(sn);
        this.shutdownEventParser();
        this.eventParser = new EventParser(getGateway(), messageQueue, this);
    }

    @Override
    public int process(int sn, String data) {
        messageQueue.push(sn, data);
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        JsonElement element = jsonObject.get("type");
        if (element.isJsonPrimitive()) {
            if (element.getAsJsonPrimitive().isNumber()) {
                if (jsonObject.getAsJsonObject("extra").get("type").isJsonPrimitive()
                        && jsonObject.getAsJsonObject("extra").getAsJsonPrimitive("type").isNumber()
                        && jsonObject.get("type").getAsInt() == jsonObject.getAsJsonObject("extra").get("type").getAsInt()
                ) {
                    switch (element.getAsInt()) {
                        case 1:
                            for (MessageHandler handler : this.messageHandlers) {
                                handler
                                        .onText(
                                                new ReceivedChannelMessage(
                                                        jsonObject.get("msg_id").getAsString(),
                                                        jsonObject.get("msg_timestamp").getAsLong(),
                                                        new TextMessage(jsonObject.get("content").getAsString()),
                                                        getGateway().getKaiheilaBot().getCacheManager().getUserCache().getElementById(jsonObject.get("author_id").getAsString()),
                                                        getGateway().getKaiheilaBot().getCacheManager().getChannelCache().getElementById(jsonObject.get("target_id").getAsString())
                                                )
                                        );
                            }
                            break;
                        case 2:
                            for (MessageHandler handler : this.messageHandlers) {
                                handler
                                        .onImage(
                                                new ReceivedChannelMessage(
                                                        jsonObject.get("msg_id").getAsString(),
                                                        jsonObject.get("msg_timestamp").getAsLong(),
                                                        new ImageMessage(jsonObject.get("content").getAsString()),
                                                        getGateway().getKaiheilaBot().getCacheManager().getUserCache().getElementById(jsonObject.get("author_id").getAsString()),
                                                        getGateway().getKaiheilaBot().getCacheManager().getChannelCache().getElementById(jsonObject.get("target_id").getAsString())
                                                )
                                        );
                            }
                            break;
                        case 3:
                            for (MessageHandler handler : this.messageHandlers) {
                                handler
                                        .onVideo(
                                                new ReceivedChannelMessage(
                                                        jsonObject.get("msg_id").getAsString(),
                                                        jsonObject.get("msg_timestamp").getAsLong(),
                                                        new VideoMessage(jsonObject.get("content").getAsString()),
                                                        getGateway().getKaiheilaBot().getCacheManager().getUserCache().getElementById(jsonObject.get("author_id").getAsString()),
                                                        getGateway().getKaiheilaBot().getCacheManager().getChannelCache().getElementById(jsonObject.get("target_id").getAsString())
                                                )
                                        );
                            }
                            break;
                        case 4:
                            for (MessageHandler handler : this.messageHandlers) {
                                handler
                                        .onFile(
                                                new ReceivedChannelMessage(
                                                        jsonObject.get("msg_id").getAsString(),
                                                        jsonObject.get("msg_timestamp").getAsLong(),
                                                        new FileMessage(jsonObject.get("content").getAsString()),
                                                        getGateway().getKaiheilaBot().getCacheManager().getUserCache().getElementById(jsonObject.get("author_id").getAsString()),
                                                        getGateway().getKaiheilaBot().getCacheManager().getChannelCache().getElementById(jsonObject.get("target_id").getAsString())
                                                )
                                        );
                            }
                            break;
                        case 8:
                            for (MessageHandler handler : this.messageHandlers) {
                                handler
                                        .onAudio(
                                                new ReceivedChannelMessage(
                                                        jsonObject.get("msg_id").getAsString(),
                                                        jsonObject.get("msg_timestamp").getAsLong(),
                                                        new AudioMessage(jsonObject.get("content").getAsString()),
                                                        getGateway().getKaiheilaBot().getCacheManager().getUserCache().getElementById(jsonObject.get("author_id").getAsString()),
                                                        getGateway().getKaiheilaBot().getCacheManager().getChannelCache().getElementById(jsonObject.get("target_id").getAsString())
                                                )
                                        );
                            }
                            break;
                        case 9:
                            for (MessageHandler handler : this.messageHandlers) {
                                handler
                                        .onKMarkdown(
                                                new ReceivedChannelMessage(
                                                        jsonObject.get("msg_id").getAsString(),
                                                        jsonObject.get("msg_timestamp").getAsLong(),
                                                        KMarkdownMessage.create(jsonObject.get("content").getAsString()),
                                                        getGateway().getKaiheilaBot().getCacheManager().getUserCache().getElementById(jsonObject.get("author_id").getAsString()),
                                                        getGateway().getKaiheilaBot().getCacheManager().getChannelCache().getElementById(jsonObject.get("target_id").getAsString())
                                                )
                                        );
                            }
                            break;
                        case 10:
                            for (MessageHandler handler : this.messageHandlers) {
                                handler
                                        .onCardMessage(
                                                new ReceivedChannelMessage(
                                                        jsonObject.get("msg_id").getAsString(),
                                                        jsonObject.get("msg_timestamp").getAsLong(),
                                                        CardMessage.parse(jsonObject.get("content").getAsString()),
                                                        getGateway().getKaiheilaBot().getCacheManager().getUserCache().getElementById(jsonObject.get("author_id").getAsString()),
                                                        getGateway().getKaiheilaBot().getCacheManager().getChannelCache().getElementById(jsonObject.get("target_id").getAsString())
                                                )
                                        );
                            }
                            break;
                    }
                }
            }
        }
        return messageQueue.getLatestSn();
    }

    public void registerMessageHandler(MessageHandler handler) {
        this.messageHandlers.add(handler);
    }

    @Override
    public void resetMessageQueue() {
        messageQueue.clear();
    }

    public void enableEventSource() {
        if (eventSource != null) {
            eventSource.enableEventPipe();
        }
    }

    public void disableEventSource() {
        if (eventSource != null) {
            eventSource.disableEventPipe();
        }
    }

    public void shutdownEventSource() {
        if (eventSource != null) {
            eventSource.shutdown();
        }
    }

    public void shutdownEventParser() {
        if (eventParser != null) {
            eventParser.shutdown();
        }
    }

    public void shutdown() {
        this.shutdownEventSource();
        this.shutdownEventParser();
    }

    public void initializeEventSource() {
        EventSourceConfigurer configurer = getGateway().getKaiheilaBot().getConfiguration().getEventSourceConfigurer();
        if (configurer.getEventSourceType() == EventSourceConfigurer.EventSourceType.CUSTOM) {
            if (configurer.getEventSource() == null) {
                throw new NullPointerException("自定义事件实例不能未空");
            }
            this.eventSource = configurer.getEventSource();
        } else if (configurer.getEventSourceType() == EventSourceConfigurer.EventSourceType.WEBHOOK) {
            WebhookEventSourceConfigurer instanceConfigurer = (WebhookEventSourceConfigurer) configurer.getInstanceConfigurer();
            this.eventSource = new WebhookEventSource(this, Compression.DEFAULT_ZLIB_STREAM, getGateway().getKaiheilaBot().getJsonEngine(), instanceConfigurer.getVerifyToken(), instanceConfigurer.getEncryptKey());
        } else if (configurer.getEventSourceType() == EventSourceConfigurer.EventSourceType.WEBSOCKET) {
            this.eventSource = new WebSocketEventSource(this, getGateway().getKaiheilaBot(), getGateway().getKaiheilaBot().getHttpClient(), getGateway().getKaiheilaBot().getWebsocketClient(), getGateway().getKaiheilaBot().getJsonEngine(), Compression.DEFAULT_ZLIB_STREAM, WebSocketSessionStorage.DEFAULT_SESSION_STORAGE);
        }
        this.eventSource.enableEventPipe();
        this.eventSource.start();
    }

    public void addListener(Listener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.getAnnotation(EventHandler.class) == null) continue;
            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length == 1) {
                Class<?> clazz = parameters[0];
                Class<?> superClass = clazz;
                while (superClass != null) {
                    if (Arrays.asList(superClass.getInterfaces()).contains(IEvent.class)) {
                        if (!Modifier.isAbstract(clazz.getModifiers())) {
                            if (!this.handlers.containsKey(superClass))
                                this.handlers.put((Class<? extends IEvent>) clazz, new HashMap<>());
                            this.handlers.get(clazz).put(method, Modifier.isStatic(method.getModifiers()) ? null : listener);
                        }
                        break;
                    }
                    superClass = superClass.getSuperclass();
                }
            }
        }
    }

    public void removeListener(Listener listener) {
        for (Class<?> clazz : this.handlers.keySet()) {
            Set<Method> methods = this.handlers.get(clazz).keySet();
            for (Method method : methods) {
                if (method.getDeclaringClass().equals(listener.getClass())) {
                    this.handlers.get(clazz).remove(method);
                }
            }
        }
    }

    public void execute(IEvent event) {
        if (this.handlers.containsKey(event.getClass())) {
            for (Map.Entry<Method, Object> entry : this.handlers.get(event.getClass()).entrySet()) {
                try {
                    entry.getKey().invoke(entry.getValue(), event);
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                }
            }
        }
    }

}
