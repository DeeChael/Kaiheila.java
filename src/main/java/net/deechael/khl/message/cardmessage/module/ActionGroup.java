package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.element.Button;
import net.deechael.khl.message.cardmessage.element.Element;

import java.util.ArrayList;
import java.util.List;

public class ActionGroup extends Module {

    private final List<Button> elements = new ArrayList<>();

    public ActionGroup() {
        super("action-group");
    }

    public void add(Button button) {
        elements.add(button);
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
