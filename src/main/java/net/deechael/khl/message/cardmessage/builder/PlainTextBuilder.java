package net.deechael.khl.message.cardmessage.builder;

import net.deechael.khl.message.cardmessage.element.PlainText;
import net.deechael.khl.message.cardmessage.element.Text;

public abstract class PlainTextBuilder extends TextBuilder {

    private final PlainText text = new PlainText();

    PlainTextBuilder(ModuleBuilder parent) {
        super(parent);
    }

    public PlainTextBuilder emoji(boolean isEmoji) {
        this.text.setEmoji(isEmoji);
        return this;
    }

    @Override
    Text text() {
        return text;
    }

}
