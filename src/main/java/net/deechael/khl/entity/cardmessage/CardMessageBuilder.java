package net.deechael.khl.entity.cardmessage;

import org.json.JSONArray;

import java.util.ArrayList;

public class CardMessageBuilder {
    ArrayList<CardMessage> cardMessages = new ArrayList<>();

    public static CardMessageBuilder start() {
        return new CardMessageBuilder();
    }

    public JSONArray build() {
        return new JSONArray(cardMessages);
    }

   public CardMessage current(){
        if (cardMessages.size() == 0) {
            cardMessages.add(new CardMessage());
        }
        return cardMessages.get(cardMessages.size() - 1);
   }

    public CardMessage append(){
        cardMessages.add(new CardMessage());
        return current();
    }

    @Override
    public String toString() {
        return build().toString();
    }
}
