package net.deechael.khl.event;

import com.google.gson.JsonObject;

public interface IEvent {

    IEvent handleSystemEvent(JsonObject body);

}
