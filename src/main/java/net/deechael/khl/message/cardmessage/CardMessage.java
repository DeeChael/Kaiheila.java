package net.deechael.khl.message.cardmessage;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import net.deechael.khl.message.Message;
import net.deechael.khl.message.MessageTypes;

public class CardMessage implements Serializable, Message {


    private final JsonArray cards = new JsonArray();

    public CardMessage append(Card card) {
        this.cards.add(card.asJson());
        return this;
    }

    @Override
    public String getContent() {
        return "";
    }

    @Override
    public String asString() {
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

}
