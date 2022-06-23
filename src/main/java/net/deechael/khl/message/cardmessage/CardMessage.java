package net.deechael.khl.message.cardmessage;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.deechael.khl.message.Message;
import net.deechael.khl.message.MessageTypes;
import net.deechael.khl.message.cardmessage.module.Module;

import java.awt.*;

public class CardMessage implements Serializable, Message {
    private final JsonArray modules = new JsonArray();
    private String type = "card";
    private Theme theme = Theme.Secondary;
    private Color color = Color.WHITE;
    private Size size = Size.LG;

    public static CardMessage create() {
        return new CardMessage();
    }

    public CardMessage setSize(Size size) {
        this.size = size;
        return this;
    }

    public CardMessage setTheme(Theme theme) {
        this.theme = theme;
        return this;
    }

    public CardMessage setColor(Color color) {
        this.theme = Theme.None;
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

    public CardMessage setType(String type) {
        this.type = type;
        return this;
    }

    public CardMessage append(Module module) {
        this.modules.add(module.asJson());
        return this;
    }

    @Override
    public String getContent() {
        return "";
    }

    @Override
    public String asString() {
        return new Gson().toJson(this.asJson());
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.CARD;
    }

    @Override
    public JsonObject asJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("theme", theme.toString());
        if (theme == Theme.None) jsonObject.addProperty("color", color.getRGB());
        jsonObject.addProperty("size", size.toString());
        jsonObject.add("modules", modules);
        return jsonObject;
    }

}
