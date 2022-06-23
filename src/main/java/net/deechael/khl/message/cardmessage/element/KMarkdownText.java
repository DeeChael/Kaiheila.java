package net.deechael.khl.message.cardmessage.element;

import com.google.gson.JsonObject;
import net.deechael.khl.message.MessageTypes;
import net.deechael.khl.message.cardmessage.Contentable;
import net.deechael.khl.message.cardmessage.Structable;
import net.deechael.khl.message.cardmessage.Textable;
import net.deechael.khl.message.kmarkdown.KMarkdownMessage;

public class KMarkdownText extends Text implements Structable, Textable, Contentable {

    public KMarkdownText() {
        super(MessageTypes.KMD);
    }

    public void setContent(KMarkdownMessage content) {
        this.setContent(content.getContent());
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", getType().getName());
        json.addProperty("content", getContent());
        return json;
    }

}
