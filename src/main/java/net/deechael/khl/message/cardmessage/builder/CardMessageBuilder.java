package net.deechael.khl.message.cardmessage.builder;

import net.deechael.khl.message.cardmessage.CardMessage;

public class CardMessageBuilder {

    final CardMessage message = new CardMessage();

    CardMessageBuilder() {
    }

    public static CardMessageBuilder create() {
        return new CardMessageBuilder();
    }

    public CardBuilder addCard() {
        return new CardBuilder(this);
    }

    public CardMessage build() {
        return this.message;
    }

}
