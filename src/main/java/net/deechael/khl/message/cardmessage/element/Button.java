package net.deechael.khl.message.cardmessage.element;

import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.Accessoriable;
import net.deechael.khl.message.cardmessage.Theme;

public class Button extends Element implements Accessoriable {
    private static final String type = "button";
    private String text;
    private Theme theme;
    private String click;
    private String value;

    public Button setText(String text) {
        this.text = text;
        return this;
    }

    public Button setTheme(Theme theme) {
        this.theme = theme;
        return this;
    }

    public Button setClick(String click) {
        this.click = click;
        return this;
    }

    public Button setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.addProperty("text", text);
        json.addProperty("theme", theme.value);
        json.addProperty("click", click);
        json.addProperty("value", value);
        return json;
    }

}
