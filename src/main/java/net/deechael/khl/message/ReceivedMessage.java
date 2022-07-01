package net.deechael.khl.message;

import net.deechael.khl.api.User;
import net.deechael.khl.message.cardmessage.CardMessage;
import net.deechael.khl.message.kmarkdown.KMarkdownMessage;

public abstract class ReceivedMessage implements Message {
    private final String id;
    private final long msgTimestamp;
    private final Message message;
    private final User author;

    public ReceivedMessage(String id, long msgTimestamp, Message message, User author) {
        this.id = id;
        this.msgTimestamp = msgTimestamp;
        this.message = message;
        this.author = author;
    }

    public ReceivedMessage(String id, long msgTimestamp, String content, MessageTypes type, User author) {
        this.id = id;
        this.msgTimestamp = msgTimestamp;
        this.message = parseMessage(type, content);
        this.author = author;
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

    public User getAuthor() {
        return author;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return msgTimestamp;
    }

    @Override
    public String getContent() {
        return this.message.getContent();
    }

    @Override
    public MessageTypes getType() {
        return this.message.getType();
    }

    public Message getMessage() {
        return this.message;
    }

}
