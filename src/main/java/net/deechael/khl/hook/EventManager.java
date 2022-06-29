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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.deechael.khl.configurer.event.EventSourceConfigurer;
import net.deechael.khl.configurer.event.WebhookEventSourceConfigurer;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.event.MessageHandler;
import net.deechael.khl.gate.Gateway;
import net.deechael.khl.hook.queue.SequenceMessageQueue;
import net.deechael.khl.hook.source.webhook.WebhookEventSource;
import net.deechael.khl.hook.source.websocket.WebSocketEventSource;
import net.deechael.khl.hook.source.websocket.session.storage.WebSocketSessionStorage;
import net.deechael.khl.util.compression.Compression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class EventManager extends KaiheilaObject implements EventManagerReceiver {
    protected static final Logger Log = LoggerFactory.getLogger(EventManager.class);

    private final List<EventListener> listeners = new LinkedList<>();
    private SequenceMessageQueue<String> messageQueue;
    private EventParser eventParser;
    private EventSource eventSource;

    private final Set<MessageHandler> messageHandlers = new HashSet<>();

    public EventManager(Gateway gateway) {
        super(gateway);
    }

    @Override
    public void initialSn(int sn) {
        messageQueue = new SequenceMessageQueue<>(sn);
        this.shutdownEventParser();
        this.eventParser = new EventParser(getGateway(), messageQueue, listeners);
    }

    @Override
    public int process(int sn, String data) {
        messageQueue.push(sn, data);
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        if (jsonObject.getAsJsonObject("extra").get("type").isJsonPrimitive()
                && jsonObject.getAsJsonObject("extra").getAsJsonPrimitive("type").isNumber()
                && jsonObject.get("type").getAsInt() == jsonObject.getAsJsonObject("extra").get("type").getAsInt()
        ) {
            for (MessageHandler handler : this.messageHandlers) {
                if (handler.getIntTypesList().contains(jsonObject.get("type").getAsInt())) {
                    handler.onMessage(getGateway().getKaiheilaBot().getCacheManager().getChannelCache().getElementById(jsonObject.get("target_id").getAsString()), getGateway().getKaiheilaBot().getCacheManager().getUserCache().getElementById(jsonObject.get("author_id").getAsString()), jsonObject.get("content").getAsString());
                }
            }
        }
        return messageQueue.getLatestSn();
    }

    public void register(MessageHandler handler) {
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

    public List<EventListener> getListeners() {
        return listeners;
    }
}
