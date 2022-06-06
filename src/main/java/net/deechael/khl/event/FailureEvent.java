package net.deechael.khl.event;

import com.google.gson.JsonObject;
import net.deechael.khl.RabbitImpl;
import com.fasterxml.jackson.databind.JsonNode;

public class FailureEvent extends AbstractEvent {
    private final Throwable throwable;
    private final String rawEventData;

    public FailureEvent(RabbitImpl rabbit, JsonNode node, Throwable t) {
        super(rabbit, node);
        this.throwable = t;
        this.rawEventData = node.toString();
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String getRawEventData() {
        return rawEventData;
    }

    @Override
    public IEvent handleSystemEvent(JsonObject body) {
        return this;
    }
}
