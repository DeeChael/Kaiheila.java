package net.deechael.khl.message.cardmessage.builder;

import net.deechael.khl.message.cardmessage.element.Text;

public abstract class TextBuilder extends ElementBuilder {

    TextBuilder(ModuleBuilder parent) {
        super(parent);
    }

    public TextBuilder content(String content) {
        this.text().setContent(content);
        return this;
    }

    abstract Text text();

}
