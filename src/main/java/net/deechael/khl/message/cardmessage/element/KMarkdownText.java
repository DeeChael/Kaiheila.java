package net.deechael.khl.message.cardmessage.element;

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

}
