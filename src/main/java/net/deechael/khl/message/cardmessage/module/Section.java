package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.Accessoriable;
import net.deechael.khl.message.cardmessage.element.Text;

public class Section extends Module {
    private Mode mode = Mode.RIGHT;
    private Text text;
    private Accessoriable accessory;

    public Section() {
        super("section");
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public void setAccessory(Accessoriable accessory) {
        this.accessory = accessory;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = super.asJson();
        if (mode != null) {
            json.addProperty("mode", mode.toString());
        }
        if (text != null) {
            json.add("text", text.asJson());
        }
        if (accessory != null) {
            json.add("accessory", accessory.asJson());
        }
        return json;
    }

    public enum Mode {
        LEFT,
        RIGHT;
        public final String value;

        Mode() {
            this.value = this.name().toLowerCase();
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

}
