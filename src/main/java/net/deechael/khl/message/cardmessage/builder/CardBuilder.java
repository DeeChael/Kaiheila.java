package net.deechael.khl.message.cardmessage.builder;

import net.deechael.khl.message.cardmessage.Card;

public class CardBuilder {

    final Card card = new Card();
    private final CardMessageBuilder parent;

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
