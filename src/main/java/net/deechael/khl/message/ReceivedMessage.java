package net.deechael.khl.message;

import net.deechael.khl.api.Channel;
import net.deechael.khl.api.User;
import net.deechael.khl.message.cardmessage.CardMessage;
import net.deechael.khl.message.kmarkdown.KMarkdownMessage;

public class ReceivedMessage implements Message {

    private final String id;
    private final long msgTimestamp;
    private final Message message;
    private final User author;
    private final Channel channel;

    public ReceivedMessage(String id, long msgTimestamp, Message message, User author, Channel channel) {
        this.id = id;
        this.msgTimestamp = msgTimestamp;
        this.message = message;
        this.author = author;
        this.channel = channel;
    }

    public ReceivedMessage(String id, long msgTimestamp, String content, MessageTypes type, User author, Channel channel) {
        this.id = id;
        this.msgTimestamp = msgTimestamp;
        this.message = parseMessage(type, content);
        this.author = author;
        this.channel = channel;
    }

    @Override
    public String getContent() {
        return this.message.getContent();
    }

    @Override
    public MessageTypes getType() {
        return this.message.getType();
    }

    public Message getAsMessage() {
        return this.message;
    }

    private static Message parseMessage(MessageTypes type, String content) {
        if (type == MessageTypes.KMD) {
            return KMarkdownMessage.create(content);
        } else if (type == MessageTypes.CARD) {
            return CardMessage.parse(content);
        } else {
            return new TextMessage(content);
        }
    }

}
