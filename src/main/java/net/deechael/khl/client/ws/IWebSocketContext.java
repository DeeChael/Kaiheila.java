package net.deechael.khl.client.ws;

import java.nio.ByteBuffer;

public interface IWebSocketContext {

    void sendTextMessage(String message);

    void sendBinaryMessage(ByteBuffer buffer);

    void closeWebSocket(int code, String reason);

    boolean isClosed();

    void await();

}
