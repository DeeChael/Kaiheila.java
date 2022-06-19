package net.deechael.khl.configurer.client;

import net.deechael.khl.bot.Bot;
import net.deechael.khl.bot.KaiheilaBotBuilder;
import net.deechael.khl.client.http.HttpClientFactory;
import net.deechael.khl.client.ws.WebSocketClientFactory;
import net.deechael.khl.configurer.AbstractConfigurer;

import java.util.Objects;

public class ClientConfigurer extends AbstractConfigurer<KaiheilaBotBuilder, Bot> {

    private HttpClientFactory httpClientFactory = HttpClientFactory.DEFAULT_HTTP_CLIENT;
    private WebSocketClientFactory webSocketClientFactory = WebSocketClientFactory.DEFAULT_WEBSOCKET_CLIENT;

    public ClientConfigurer(KaiheilaBotBuilder kaiheilaBotBuilder) {
        super(kaiheilaBotBuilder);
    }

    public ClientConfigurer httpClientFactory(HttpClientFactory httpClientFactory) {
        Objects.requireNonNull(httpClientFactory, "httpClientFactory == null");
        this.httpClientFactory = httpClientFactory;
        return this;
    }

    public ClientConfigurer webSocketClientFactory(WebSocketClientFactory webSocketClientFactory) {
        Objects.requireNonNull(webSocketClientFactory, "webSocketClientFactory == null");
        this.webSocketClientFactory = webSocketClientFactory;
        return this;
    }

    public HttpClientFactory getHttpClientFactory() {
        return httpClientFactory;
    }

    public WebSocketClientFactory getWebSocketClientFactory() {
        return webSocketClientFactory;
    }

}
