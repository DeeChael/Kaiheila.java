package net.deechael.khl.message.cardmessage.struct;

import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.Serializable;

public abstract class Struct implements Serializable {

    private final String type;

    public Struct(String type) {
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
