package net.deechael.khl.message.cardmessage;

public enum Theme {
    Primary,
    Secondary,
    Success,
    Warning,
    Danger,
    Info,
    None("");
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
