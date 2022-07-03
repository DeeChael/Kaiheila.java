package net.deechael.khl.message.cardmessage.builder;

import net.deechael.khl.message.cardmessage.Size;
import net.deechael.khl.message.cardmessage.element.Element;
import net.deechael.khl.message.cardmessage.element.Image;

public abstract class ImageBuilder extends ElementBuilder {

    private final Image image = new Image();

    ImageBuilder(ModuleBuilder parent) {
        super(parent);
    }

    public ImageBuilder src(String src) {
        this.image.setSrc(src);
        return this;
    }

    public ImageBuilder alt(String alt) {
        this.image.setAlt(alt);
        return this;
    }

    public ImageBuilder size(Size size) {
        this.image.setSize(size);
        return this;
    }

    public ImageBuilder circle(boolean circle) {
        this.image.setCircle(circle);
        return this;
    }

    @Override
    Element element() {
        return this.image;
    }

}
