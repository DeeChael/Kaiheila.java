package net.deechael.khl.message.cardmessage.builder;

import net.deechael.khl.message.cardmessage.Card;

public class CardBuilder {

    private final CardMessageBuilder parent;

    final Card card = new Card();

    CardBuilder(CardMessageBuilder parent) {
        this.parent = parent;
    }

    public ActionGroupBuilder addActionGroup() {
        return new ActionGroupBuilder(this);
    }

    public CardMessageBuilder back() {
        this.parent.message.append(this.card);
        return this.parent;
    }

}
