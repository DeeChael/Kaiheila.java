package net.deechael.khl.event.message;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;
import net.deechael.khl.gate.Gateway;

public class BotMessageEvent extends AbstractEvent {

    public BotMessageEvent(Gateway gateway, JsonNode node) {
        super(gateway, node);
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        return this;
    }
}
