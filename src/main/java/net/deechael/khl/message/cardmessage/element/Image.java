package net.deechael.khl.message.cardmessage.element;

import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.Accessoriable;
import net.deechael.khl.message.cardmessage.Contentable;
import net.deechael.khl.message.cardmessage.Size;

public class Image extends Element implements Accessoriable, Contentable {
    private static final String type = "image";
    private String src;
    private String alt;
    private Size size;
    private boolean circle;

    public Image setSrc(String src) {
        this.src = src;
        return this;
    }

    public Image setAlt(String alt) {
        this.alt = alt;
        return this;
    }

    public Image setSize(Size size) {
        this.size = size;
        return this;
    }

    public Image setCircle(boolean circle) {
        this.circle = circle;
        return this;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.addProperty("src", src);
        json.addProperty("alt", alt);
        json.addProperty("size", size.value);
        json.addProperty("circle", circle);
        return json;
    }

}
