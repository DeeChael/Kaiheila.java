package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.Contentable;
import net.deechael.khl.message.cardmessage.Structable;

import java.util.ArrayList;
import java.util.List;

public class Content extends Module implements Structable {
    private static final String type = "context";
    private final List<Contentable> elements = new ArrayList<>();

    public net.deechael.khl.message.cardmessage.module.Content add(Contentable content) {
        elements.add(content);
        return this;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        JsonArray array = new JsonArray();
        for (Contentable contentable : this.elements) {
            array.add(contentable.asJson());
        }
        json.add("elements", array);
        return json;
    }

}
