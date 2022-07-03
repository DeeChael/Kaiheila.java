package net.deechael.khl.message.cardmessage.builder;

import net.deechael.khl.message.cardmessage.module.Media;

public abstract class MediaBuilder extends ModuleBuilder {

    MediaBuilder(CardBuilder parent) {
        super(parent);
    }

    abstract Media media();

    public MediaBuilder src(String src) {
        this.media().setSrc(src);
        return this;
    }

    public MediaBuilder title(String title) {
        this.media().setTitle(title);
        return this;
    }

}
