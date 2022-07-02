package net.deechael.khl.api;

public interface VoiceChannel extends Channel {

    void moveTo(User user);

    void moveTo(User[] user);

    enum Quality {
        LOW(1),
        NORMAL(2),
        HIGH(3);

        private final int code;

        Quality(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

    }

}
