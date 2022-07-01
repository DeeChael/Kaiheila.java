package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.element.Text;

public class Header extends Module {
    private Text text;

    public Header() {
        super("header");
    }

    public void setText(Text text) {
        this.text = text;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = super.asJson();
        json.add("text", text.asJson());
        return json;
    }

}
