package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.element.Element;
import net.deechael.khl.message.cardmessage.element.Image;

import java.util.ArrayList;
import java.util.List;

public class ImageGroup extends Module {
    private static final String type = "image-group";
    private final List<Image> elements = new ArrayList<>();

    public net.deechael.khl.message.cardmessage.module.ImageGroup add(Image image) {
        elements.add(image);
        return this;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        JsonArray array = new JsonArray();
        for (Element element : this.elements) {
            array.add(element.asJson());
        }
        json.add("elements", array);
        return json;
    }
}
