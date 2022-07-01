package net.deechael.khl.message.cardmessage.element;

import com.google.gson.JsonObject;
import net.deechael.khl.message.MessageTypes;
import net.deechael.khl.message.cardmessage.Contentable;
import net.deechael.khl.message.cardmessage.Structable;
import net.deechael.khl.message.cardmessage.Textable;

public class PlainText extends Text implements Structable, Textable, Contentable {

    private boolean emoji = true;

    public PlainText() {
        super(MessageTypes.TEXT);
    }

    public boolean isEmoji() {
        return emoji;
    }

    public void setEmoji(boolean emoji) {
        this.emoji = emoji;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = super.asJson();
        json.addProperty("emoji", emoji);
        return json;
    }

}
