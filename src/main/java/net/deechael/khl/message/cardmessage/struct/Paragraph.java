package net.deechael.khl.message.cardmessage.struct;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.Serializable;
import net.deechael.khl.message.cardmessage.Structable;
import net.deechael.khl.message.cardmessage.Textable;

import java.util.ArrayList;
import java.util.List;

public class Paragraph implements Textable {
    private static final String type = "paragraph";
    private final List<Structable> fields = new ArrayList<>();
    private int cols = 0;

    public Paragraph addCol(Structable fields) {
        this.fields.add(fields);
        cols++;
        return this;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.addProperty("cols", cols);
        JsonArray array = new JsonArray();
        for (JsonObject object : fields.stream().map(Serializable::asJson).toList()) {
            array.add(object);
        }
        json.add("fields", array);
        return json;
    }

}
