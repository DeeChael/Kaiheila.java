package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.element.Image;

import java.util.ArrayList;
import java.util.List;

public class Container extends Module {

    private final List<Image> elements = new ArrayList<>();

    public Container() {
        super("container");
    }

    public void add(Image image) {
        elements.add(image);
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = super.asJson();
        JsonArray array = new JsonArray();
        for (Image image : this.elements) {
            array.add(image.asJson());
        }
        json.add("elements", array);
        return json;
    }

}
