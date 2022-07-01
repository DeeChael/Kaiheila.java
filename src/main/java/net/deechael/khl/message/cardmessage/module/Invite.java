package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public class Invite extends Module {

    private final String code;

    public Invite(@NotNull String code) {
        super("invite");
        this.code = code;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = super.asJson();
        json.addProperty("code", code);
        return json;
    }

}
