package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.element.Image;

import java.util.ArrayList;
import java.util.List;

public class Container extends Module {
    private static final String type = "container";
    private final List<Image> elements = new ArrayList<>();

    public net.deechael.khl.message.cardmessage.module.Container add(Image image) {
        elements.add(image);
        return this;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        JsonArray array = new JsonArray();
        for (Image image : this.elements) {
            array.add(image.asJson());
        }
        json.add("elements", array);
        return json;
    }

}
