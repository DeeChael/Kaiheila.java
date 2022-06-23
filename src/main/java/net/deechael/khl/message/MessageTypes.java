package net.deechael.khl.message;

public enum MessageTypes {

    TEXT(1, "plain-text"),
    IMG(2, "image"),
    VIDEO(3, "video"),
    FILE(4, "file"),
    AUDIO(8, "audio"),
    KMD(9, "kmarkdown"),
    CARD(10, "cardmessage"),
    SYS(255, "system");

    private final int type;
    private final String name;

    MessageTypes(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
