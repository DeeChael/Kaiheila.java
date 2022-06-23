package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.element.Button;
import net.deechael.khl.message.cardmessage.element.Element;

import java.util.ArrayList;
import java.util.List;

public class ActionGroup extends Module {
    private static final String type = "action-group";
    private final List<Button> elements = new ArrayList<>();

    public net.deechael.khl.message.cardmessage.module.ActionGroup add(Button button) {
        elements.add(button);
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
