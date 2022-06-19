package net.deechael.khl.command;

import net.deechael.khl.api.Channel;
import net.deechael.khl.api.User;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.core.KaiheilaObject;

public class CommandSender extends KaiheilaObject {

    private final Channel channel;
    private final User user;

    public CommandSender(KaiheilaBot kaiheilaBot, Channel channel, User user) {
        super(kaiheilaBot);
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
