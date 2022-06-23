package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.element.Text;

public class Header extends Module {
    private static final String type = "header";
    private Text text;

    public net.deechael.khl.message.cardmessage.module.Header setText(Text text) {
        this.text = text;
        return this;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.add("text", text.asJson());
        return json;
    }
}
