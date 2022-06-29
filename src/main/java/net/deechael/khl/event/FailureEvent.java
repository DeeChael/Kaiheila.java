package net.deechael.khl.event;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.gate.Gateway;

public class FailureEvent extends AbstractEvent {
    private final Throwable throwable;
    private final String rawEventData;

    public FailureEvent(Gateway gateway, JsonNode node, Throwable t) {
        super(gateway, node);
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
    public IEvent handleSystemEvent(JsonNode body) {
        return this;
    }

}
