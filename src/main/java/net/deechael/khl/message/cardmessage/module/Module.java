package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.Serializable;

public abstract class Module implements Serializable {

    private final String type;

    public Module(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        return json;
    }

}
