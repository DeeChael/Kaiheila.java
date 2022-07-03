package net.deechael.khl.message.cardmessage.builder;

import net.deechael.khl.message.cardmessage.Theme;
import net.deechael.khl.message.cardmessage.element.Button;
import net.deechael.khl.message.cardmessage.element.Element;
import net.deechael.khl.message.cardmessage.element.Text;

public abstract class ButtonBuilder extends ElementBuilder {

    protected final Button button = new Button();

    ButtonBuilder(ModuleBuilder parent) {
        super(parent);
    }

    public ButtonBuilder text(Text text) {
        this.button.setText(text);
        return this;
    }

    public ButtonBuilder theme(Theme theme) {
        this.button.setTheme(theme);
        return this;
    }

    public ButtonBuilder click(Button.Click click) {
        this.button.setClick(click);
        return this;
    }

    public ButtonBuilder value(String value) {
        this.button.setValue(value);
        return this;
    }

    @Override
    Element element() {
        return this.button;
    }

}
