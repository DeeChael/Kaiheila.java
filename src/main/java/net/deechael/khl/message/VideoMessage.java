package net.deechael.khl.message;

public class VideoMessage implements Message {

    private final String url;

    public VideoMessage(String videoUrl) {
        this.url = videoUrl;
    }

    @Override
    public String getContent() {
        return url;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.VIDEO;
    }

}
