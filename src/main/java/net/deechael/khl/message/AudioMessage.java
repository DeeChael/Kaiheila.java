package net.deechael.khl.message;

public class AudioMessage implements Message {

    private final String url;

    public AudioMessage(String audioUrl) {
        this.url = audioUrl;
    }

    @Override
    public String getContent() {
        return url;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.AUDIO;
    }

}
