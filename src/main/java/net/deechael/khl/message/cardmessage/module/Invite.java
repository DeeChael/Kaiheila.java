package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonObject;

public class Invite extends Module {
    private static final String type = "invite";
    private String code;

    public net.deechael.khl.message.cardmessage.module.Invite setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.addProperty("code", code);
        return json;
    }
}
