package net.deechael.khl.message.cardmessage.builder;

import net.deechael.khl.message.cardmessage.module.ActionGroup;

public class ActionGroupBuilder extends ModuleBuilder {

    private final ActionGroup actionGroup = new ActionGroup();

    ActionGroupBuilder(CardBuilder parent) {
        super(parent);
    }

    public ButtonBuilder addButton() {
        return new ActionGroupBuilder_ButtonBuilder(this);
    }

    @Override
    ActionGroup module() {
        return this.actionGroup;
    }

    private class ActionGroupBuilder_ButtonBuilder extends ButtonBuilder {

        ActionGroupBuilder_ButtonBuilder(ModuleBuilder parent) {
            super(parent);
        }

        @Override
        public ModuleBuilder back() {
            ((ActionGroupBuilder) this.parent).module().add(this.button);
            return parent;
        }
    }

}
