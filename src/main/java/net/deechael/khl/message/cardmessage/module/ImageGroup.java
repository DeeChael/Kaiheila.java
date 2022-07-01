package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.element.Element;
import net.deechael.khl.message.cardmessage.element.Image;

import java.util.ArrayList;
import java.util.List;

public class ImageGroup extends Module {

    private final List<Image> elements = new ArrayList<>();

    public ImageGroup() {
        super("image-group");
    }

    public void add(Image image) {
        elements.add(image);
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = super.asJson();
        JsonArray array = new JsonArray();
        for (Element element : this.elements) {
            array.add(element.asJson());
        }
        json.add("elements", array);
        return json;
    }

}
