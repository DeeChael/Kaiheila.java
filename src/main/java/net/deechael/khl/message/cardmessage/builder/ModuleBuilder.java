package net.deechael.khl.message.cardmessage.builder;

import net.deechael.khl.message.cardmessage.module.Module;

public abstract class ModuleBuilder {

    private final CardBuilder parent;

    ModuleBuilder(CardBuilder parent) {
        this.parent = parent;
    }

    abstract Module module();


    public CardBuilder back() {
        this.parent.card.append(module());
        return this.parent;
    }

}
