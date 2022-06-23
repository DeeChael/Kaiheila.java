package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonObject;

public class Divider extends Module {

    private static final String type = "divider";

    @Override
    public JsonObject asJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        return json;
    }

}
