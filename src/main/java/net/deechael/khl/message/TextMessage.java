package net.deechael.khl.message;

public class TextMessage implements Message {

    private final String content;

    public TextMessage(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.TEXT;
    }

}
