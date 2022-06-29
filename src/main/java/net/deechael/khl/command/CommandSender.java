package net.deechael.khl.command;

import net.deechael.khl.api.Channel;
import net.deechael.khl.api.User;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.gate.Gateway;

public class CommandSender extends KaiheilaObject {

    private final Channel channel;
    private final User user;

    public CommandSender(Gateway gateway, Channel channel, User user) {
        super(gateway);
        this.channel = channel;
        this.user = user;
    }

    public Channel getChannel() {
        return channel;
    }

    public User getUser() {
        return user;
    }

}
