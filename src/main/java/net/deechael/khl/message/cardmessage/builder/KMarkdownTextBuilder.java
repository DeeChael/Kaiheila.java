package net.deechael.khl.message.cardmessage.builder;

import net.deechael.khl.message.cardmessage.element.KMarkdownText;
import net.deechael.khl.message.cardmessage.element.Text;
import net.deechael.khl.message.kmarkdown.KMarkdownMessage;

public abstract class KMarkdownTextBuilder extends TextBuilder {

    private final KMarkdownText text = new KMarkdownText();

    KMarkdownTextBuilder(ModuleBuilder parent) {
        super(parent);
    }

    public KMarkdownTextBuilder content(KMarkdownMessage content) {
        this.text.setContent(content);
        return this;
    }

    @Override
    Text text() {
        return text;
    }

}
