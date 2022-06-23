package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.Accessoriable;
import net.deechael.khl.message.cardmessage.element.Text;

public class Section extends Module {
    private static final String type = "section";
    private Mode mode;
    private Text text;
    private Accessoriable accessory;

    public net.deechael.khl.message.cardmessage.module.Section setMode(Mode mode) {
        this.mode = mode;
        return this;
    }

    public net.deechael.khl.message.cardmessage.module.Section setText(Text text) {
        this.text = text;
        return this;
    }

    public net.deechael.khl.message.cardmessage.module.Section setAccessory(Accessoriable accessory) {
        this.accessory = accessory;
        return this;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.addProperty("mode", mode.toString());
        json.add("text", text.asJson());
        json.add("accessory", accessory.asJson());
        return json;
    }

    public enum Mode {
        Left,
        Right;
        public final String value;

        Mode() {
            this.value = this.name().toLowerCase();
        }
    }

}
