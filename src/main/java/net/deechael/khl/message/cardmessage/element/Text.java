package net.deechael.khl.message.cardmessage.element;

import com.google.gson.JsonObject;
import net.deechael.khl.message.MessageTypes;
import net.deechael.khl.message.cardmessage.Contentable;

public abstract class Text extends Element implements Contentable {

    private final MessageTypes type;

    private String content;

    public Text(MessageTypes type) {
        super(type.getName());
        if (type == MessageTypes.KMD) {
            this.type = MessageTypes.KMD;
        } else {
            this.type = MessageTypes.TEXT;
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageTypes getMessageType() {
        return type;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = super.asJson();
        if (content != null) {
            json.addProperty("content", getContent());
        }
        return json;
    }

}
