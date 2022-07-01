package net.deechael.khl.message;

public class ImageMessage implements Message {

    private final String url;

    public ImageMessage(String imageUrl) {
        this.url = imageUrl;
    }

    @Override
    public String getContent() {
        return url;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.IMG;
    }

}
