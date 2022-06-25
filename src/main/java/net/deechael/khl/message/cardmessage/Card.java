package net.deechael.khl.message.cardmessage;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.module.Module;

import java.awt.*;

public class Card implements Serializable {
    private final JsonArray modules = new JsonArray();
    private String type = "card";
    private Theme theme = Theme.SECONDARY;
    private Color color = Color.WHITE;
    private Size size = Size.LG;

    public static Card create() {
        return new Card();
    }

    public Card setSize(Size size) {
        this.size = size;
        return this;
    }

    public Card setTheme(Theme theme) {
        this.theme = theme;
        return this;
    }

    public Card setColor(Color color) {
        this.theme = Theme.NONE;
        this.color = color;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public Theme getTheme() {
        return theme;
    }

    public Size getSize() {
        return size;
    }

    public Card setType(String type) {
        this.type = type;
        return this;
    }

    public Card append(Module module) {
        this.modules.add(module.asJson());
        return this;
    }

    @Override
    public JsonObject asJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("theme", theme.toString());
        if (theme == Theme.NONE) jsonObject.addProperty("color", color.getRGB());
        jsonObject.addProperty("size", size.toString());
        jsonObject.add("modules", modules);
        return jsonObject;
    }

}
