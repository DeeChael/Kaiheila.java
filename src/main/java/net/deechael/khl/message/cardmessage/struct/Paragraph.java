package net.deechael.khl.message.cardmessage.struct;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.deechael.khl.message.cardmessage.Serializable;
import net.deechael.khl.message.cardmessage.Structable;
import net.deechael.khl.message.cardmessage.Textable;

import java.util.ArrayList;
import java.util.List;

public class Paragraph extends Struct implements Textable {

    private final List<Structable> fields = new ArrayList<>();
    private int cols = 0;

    public Paragraph() {
        super("paragraph");
    }

    public void addCol(Structable fields) {
        this.fields.add(fields);
        cols++;
    }

    public int getCols() {
        return cols;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = super.asJson();
        json.addProperty("cols", cols);
        JsonArray array = new JsonArray();
        for (JsonObject object : fields.stream().map(Serializable::asJson).map(JsonElement::getAsJsonObject).toList()) {
            array.add(object);
        }
        json.add("fields", array);
        return json;
    }

}
