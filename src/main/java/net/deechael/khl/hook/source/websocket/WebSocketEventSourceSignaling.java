package net.deechael.khl.hook.source.websocket;

import java.util.Arrays;

public enum WebSocketEventSourceSignaling {

    EVENT(0),
    HELLO(1),
    PING(2),
    PONG(3),
    @Deprecated
    RESUME(4),
    RECONNECT(5),
    RESUME_ACK(6),
    UNKNOWN(-1);

    private final int type;

    WebSocketEventSourceSignaling(int type) {
        this.type = type;
    }

    public static WebSocketEventSourceSignaling typeOf(int type) {
        return Arrays.stream(WebSocketEventSourceSignaling.values()).filter(s -> s.type == type).findFirst().orElse(UNKNOWN);
    }

    public int getType() {
        return type;
    }
}
