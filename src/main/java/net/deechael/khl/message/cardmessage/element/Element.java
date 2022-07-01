package net.deechael.khl.message.cardmessage.element;

import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.Serializable;

public abstract class Element implements Serializable {

    private final String type;

    public Element(String type) {
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
