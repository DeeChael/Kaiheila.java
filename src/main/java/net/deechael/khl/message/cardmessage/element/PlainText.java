package net.deechael.khl.message.cardmessage.element;

import com.google.gson.JsonObject;
import net.deechael.khl.message.MessageTypes;
import net.deechael.khl.message.cardmessage.Contentable;
import net.deechael.khl.message.cardmessage.Structable;
import net.deechael.khl.message.cardmessage.Textable;

public class PlainText extends Text implements Structable, Textable, Contentable {
    private boolean emoji;

    public PlainText() {
        super(MessageTypes.TEXT);
    }

    public void setEmoji(boolean emoji) {
        this.emoji = emoji;
    }

    public boolean isEmoji() {
        return emoji;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", getType().getName());
        json.addProperty("content", getContent());
        json.addProperty("emoji", emoji);
        return json;
    }
}
