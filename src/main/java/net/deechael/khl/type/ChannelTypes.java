package net.deechael.khl.type;

public enum ChannelTypes {

    TEXT(1),
    VOICE(2),
    CATEGORY(0);

    private final int type;

    ChannelTypes(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
