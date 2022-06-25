package net.deechael.khl.event;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.bot.KaiheilaBot;

public class FailureEvent extends AbstractEvent {
    private final Throwable throwable;
    private final String rawEventData;

    public FailureEvent(KaiheilaBot rabbit, JsonNode node, Throwable t) {
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
    public IEvent handleSystemEvent(JsonNode body) {
        return this;
    }

}
