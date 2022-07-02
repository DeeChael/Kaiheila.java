package net.deechael.khl.api;

public interface Game {

    int getId();

    String getName();

    Type getType();

    String getOptions();

    String[] getProcessName();

    String[] getProductName();

    String getIcon();

    enum Type {
        GAME(0),
        VUP(1),
        PROCESS(2);

        private final int type;

        Type(int type) {
            this.type = type;
        }

        public static Type valueOf(int type) {
            return switch (type) {
                case 1 -> VUP;
                case 2 -> PROCESS;
                default -> GAME;
            };
        }

        public int getType() {
            return type;
        }

    }

}
