package net.deechael.khl.message.cardmessage;

public enum Size {
    SM,
    LG;
    public final String value;

    Size() {
        this.value = this.name().toLowerCase();
    }

    @Override
    public String toString() {
        return value;
    }

}
