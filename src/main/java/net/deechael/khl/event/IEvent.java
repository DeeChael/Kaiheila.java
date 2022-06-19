package net.deechael.khl.event;

import com.fasterxml.jackson.databind.JsonNode;

public interface IEvent {

    IEvent handleSystemEvent(JsonNode body);

}
