package net.deechael.khl.message.cardmessage.element;

import net.deechael.khl.message.MessageTypes;
import net.deechael.khl.message.cardmessage.Contentable;

public abstract class Text extends Element implements Contentable {

    private final MessageTypes type;

    private String content;

    public Text(MessageTypes type) {
        if (type == MessageTypes.KMD) {
            this.type = MessageTypes.KMD;
        } else {
            this.type = MessageTypes.TEXT;
        }
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public MessageTypes getType() {
        return type;
    }

}
