package net.deechael.khl.message.cardmessage;

import com.google.gson.*;
import net.deechael.khl.message.Message;
import net.deechael.khl.message.MessageTypes;
import net.deechael.khl.message.cardmessage.element.Button;
import net.deechael.khl.message.cardmessage.module.ActionGroup;

import java.awt.*;

public class CardMessage implements Serializable, Message {


    private final JsonArray cards = new JsonArray();

    public CardMessage append(Card card) {
        this.cards.add(card.asJson());
        return this;
    }

    @Override
    public String getContent() {
        return new Gson().toJson(this.asJson());
    }


    @Override
    public MessageTypes getType() {
        return MessageTypes.CARD;
    }

    @Override
    public JsonArray asJson() {
        return this.cards.deepCopy();
    }

    public static CardMessage parse(String json) {
        JsonElement base = JsonParser.parseString(json);
        if (base.isJsonArray()) {
            CardMessage message = new CardMessage();
            JsonArray cardMessageArray = base.getAsJsonArray();
            for (JsonElement element : cardMessageArray) {
                if (element.isJsonObject()) {
                    JsonObject object = element.getAsJsonObject();
                    Card card = new Card();
                    card.setType("card");
                    card.setTheme(Theme.valueOf(object.get("theme").getAsString().toUpperCase()));
                    card.setColor(new Color(object.get("color").getAsInt()));
                    card.setSize(Size.valueOf(object.get("size").getAsString().toUpperCase()));
                    for (JsonElement moduleElement : object.getAsJsonArray("modules")) {
                        JsonObject moduleObject = moduleElement.getAsJsonObject();
                        String type = moduleObject.get("type").getAsString();
                        if (type.equals("action-group")) {
                            ActionGroup group = new ActionGroup();
                            for (JsonElement buttonElement : moduleObject.getAsJsonArray("elements")) {
                                JsonObject buttonObject = buttonElement.getAsJsonObject();
                                Button button = new Button();
                            }
                        }
                    }
                }
            }
            return message;
        }
        //TODO haven't finished yet
        throw new RuntimeException("Failed to parse card message");
    }

}
