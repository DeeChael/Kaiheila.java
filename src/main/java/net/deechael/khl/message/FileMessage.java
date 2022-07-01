package net.deechael.khl.message;

public class FileMessage implements Message {

    private final String url;

    public FileMessage(String fileUrl) {
        this.url = fileUrl;
    }

    @Override
    public String getContent() {
        return url;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.FILE;
    }

}
