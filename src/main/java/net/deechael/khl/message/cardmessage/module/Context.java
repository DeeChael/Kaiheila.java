package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.Contentable;
import net.deechael.khl.message.cardmessage.Structable;

import java.util.ArrayList;
import java.util.List;

public class Context extends Module implements Structable {
    private final List<Contentable> elements = new ArrayList<>();

    public Context() {
        super("context");
    }

    public void add(Contentable content) {
        elements.add(content);
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = super.asJson();
        JsonArray array = new JsonArray();
        for (Contentable contentable : this.elements) {
            array.add(contentable.asJson());
        }
        json.add("elements", array);
        return json;
    }

}
