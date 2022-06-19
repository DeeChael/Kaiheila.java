package net.deechael.khl.core;

import net.deechael.khl.bot.Bot;
import net.deechael.khl.bot.KaiheilaBot;

public abstract class KaiheilaObject {

    private final KaiheilaBot rabbit;

    public KaiheilaObject(KaiheilaBot rabbit) {
        this.rabbit = rabbit;
    }

    protected KaiheilaBot getKaiheilaBot() {
        return rabbit;
    }

    public Bot getBot() {
        return rabbit;
    }

}
