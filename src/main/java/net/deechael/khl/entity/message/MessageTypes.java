package net.deechael.khl.entity.message;

public enum MessageTypes {

    TEXT(1),
    IMG(2),
    VIDEO(3),
    FILE(4),
    AUDIO(8),
    KMD(9),
    CARD(10),
    SYS(255);

    private final int type;

    MessageTypes(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
