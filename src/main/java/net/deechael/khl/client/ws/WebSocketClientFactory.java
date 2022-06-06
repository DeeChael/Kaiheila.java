package net.deechael.khl.client.ws;

import net.deechael.khl.client.ws.impl.OkHttpWebSocketClientImpl;
import net.deechael.khl.util.OkHttpClientSingleton;

@FunctionalInterface
public interface WebSocketClientFactory {

    WebSocketClientFactory DEFAULT_WEBSOCKET_CLIENT = () -> new OkHttpWebSocketClientImpl(OkHttpClientSingleton.getInstance());

    IWebSocketClient buildWebSocketClient();

}
