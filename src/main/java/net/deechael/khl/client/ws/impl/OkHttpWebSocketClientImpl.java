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

package net.deechael.khl.client.ws.impl;

import net.deechael.khl.client.ws.IWebSocketClient;
import net.deechael.khl.client.ws.IWebSocketContext;
import net.deechael.khl.client.ws.IWebSocketListener;
import net.deechael.khl.configurer.KaiheilaConfiguration;
import okhttp3.*;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class OkHttpWebSocketClientImpl implements IWebSocketClient {
    protected final static Logger Log = LoggerFactory.getLogger(OkHttpWebSocketClientImpl.class);
    private final OkHttpClient client;

    public OkHttpWebSocketClientImpl(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public OkhttpWebSocketClientContext openWebSocket(String url, IWebSocketListener listener) {
        Request request = new Request.Builder().url(url).build();
        OkhttpWebSocketClientContext websocketContext = new OkhttpWebSocketClientContext(listener);
        WebSocket websocket = client.newWebSocket(request, websocketContext);
        websocketContext.setWebsocket(websocket);
        return websocketContext;
    }

    public static class OkhttpWebSocketClientContext extends WebSocketListener implements IWebSocketContext {
        private final IWebSocketListener listener;
        private WebSocket websocket;
        private boolean closed = true;
        private Thread receiverThread;

        public OkhttpWebSocketClientContext(IWebSocketListener listener) {
            this.listener = listener;
        }

        public void setWebsocket(WebSocket websocket) {
            this.websocket = websocket;
        }

        @Override
        public void sendTextMessage(String message) {
            websocket.send(message);
        }

        @Override
        public void sendBinaryMessage(ByteBuffer buffer) {
            websocket.send(ByteString.of(buffer));
        }

        @Override
        public void closeWebSocket(int code, String reason) {
            websocket.close(code, reason);
        }

        @Override
        public boolean isClosed() {
            return closed;
        }

        public void setClosed(boolean closed) {
            this.closed = closed;
        }

        @Override
        public void await() {
            if (this.receiverThread.isAlive()) {
                try {
                    this.receiverThread.join();
                } catch (InterruptedException ignored) {
                }
            }
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Thread.currentThread().setName("WebSocketReceiverThread");
            this.receiverThread = Thread.currentThread();
            if (KaiheilaConfiguration.isDebug) Log.trace("WebSocket onOpen");
            this.setClosed(false);
            listener.onOpen(this);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            if (KaiheilaConfiguration.isDebug) Log.trace("onMessage(plain) {}", text);
            listener.onTextMessage(this, text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            if (KaiheilaConfiguration.isDebug) Log.trace("onMessage(binary) {}", bytes.toString());
            listener.onBinaryMessage(this, ByteBuffer.wrap(bytes.toByteArray()));
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            if (KaiheilaConfiguration.isDebug) Log.trace("onClosing {} {}", code, reason);
            this.setClosed(true);
            listener.onClosing(this, code, reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            if (KaiheilaConfiguration.isDebug) Log.trace("onClosed {} {}", code, reason);
            this.setClosed(true);
            listener.onClosed(this, code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            if (KaiheilaConfiguration.isDebug) Log.trace("onFailure {}", t.getMessage());
            this.setClosed(true);
            listener.onFailure(this, t);
        }
    }
}
