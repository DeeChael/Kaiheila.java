package net.deechael.khl.message.cardmessage;

public enum Theme {
    PRIMARY,
    SECONDARY,
    SUCCESS,
    WARNING,
    DANGER,
    INFO,
    NONE("");
    public final String value;

    Theme() {
        this.value = this.name().toLowerCase();
    }

    Theme(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
