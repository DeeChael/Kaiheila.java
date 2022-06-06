package net.deechael.khl.core;

import net.deechael.khl.api.Bot;
import net.deechael.khl.RabbitImpl;

public abstract class RabbitObject {

    private final RabbitImpl rabbit;

    public RabbitObject(RabbitImpl rabbit) {
        this.rabbit = rabbit;
    }

    protected RabbitImpl getRabbitImpl() {
        return rabbit;
    }

    public Bot getRabbit() {
        return rabbit;
    }

}
