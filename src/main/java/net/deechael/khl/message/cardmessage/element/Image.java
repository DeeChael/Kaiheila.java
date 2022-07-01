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
    private boolean circle = false;

    public Image() {
        super("image");
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public void setCircle(boolean circle) {
        this.circle = circle;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = super.asJson();
        if (src != null) {
            json.addProperty("src", src);
        }
        if (alt != null) {
            json.addProperty("alt", alt);
        }
        if (size != null) {
            json.addProperty("size", size.toString());
        }
        json.addProperty("circle", circle);
        return json;
    }

}
