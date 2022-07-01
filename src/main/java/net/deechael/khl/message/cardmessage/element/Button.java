package net.deechael.khl.message.cardmessage.element;

import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.Accessoriable;
import net.deechael.khl.message.cardmessage.Theme;

public class Button extends Element implements Accessoriable {
    private Text text;
    private Theme theme;
    private Click click;
    private String value;

    public Button() {
        super("button");
    }

    public Button(Text text) {
        super("button");
        this.setText(text);
    }

    public void setText(Text text) {
        this.text = text;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public void setClick(Click click) {
        this.click = click;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = super.asJson();
        if (text != null) {
            json.add("text", text.asJson());
        }
        if (theme != null) {
            json.addProperty("theme", theme.toString());
        }
        if (click != null) {
            json.addProperty("click", click.toString());
        }
        if (value != null) {
            json.addProperty("value", value);
        }
        return json;
    }

    public enum Click {

        LINK,
        RETURN_VAL;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }

    }

}
