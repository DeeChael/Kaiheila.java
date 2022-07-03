package net.deechael.khl.message.cardmessage.builder;

import net.deechael.khl.message.cardmessage.element.Element;

public abstract class ElementBuilder {

    protected final ModuleBuilder parent;

    ElementBuilder(ModuleBuilder parent) {
        this.parent = parent;
    }

    abstract Element element();

    public abstract ModuleBuilder back();

}
