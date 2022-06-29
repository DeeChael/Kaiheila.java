package net.deechael.khl.core;

import net.deechael.khl.api.KHLObject;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.gate.Gateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class KaiheilaObject implements KHLObject {

    protected static final Logger Log = LoggerFactory.getLogger(KaiheilaObject.class);

    private final Gateway gateway;

    public KaiheilaObject(Gateway gateway) {
        this.gateway = gateway;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public KaiheilaBot getKaiheilaBot() {
        return getGateway().getKaiheilaBot();
    }

}
