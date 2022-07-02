package net.deechael.khl.command;

import net.deechael.khl.api.Channel;
import net.deechael.khl.api.Guild;
import net.deechael.khl.api.User;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.gate.Gateway;
import net.deechael.khl.message.Message;
import net.deechael.khl.message.ReceivedChannelMessage;

public class CommandSender extends KaiheilaObject {

    private final ReceivedChannelMessage message;

    public CommandSender(Gateway gateway, ReceivedChannelMessage message) {
        super(gateway);
        this.message = message;
    }

    public Guild getGuild() {
        return message.getGuild();
    }

    public Channel getChannel() {
        return message.getChannel();
    }

    public User getUser() {
        return message.getAuthor();
    }

    public ReceivedChannelMessage getReceivedMessage() {
        return message;
    }

    public void reply(Message message) {
        this.message.reply(message);
    }

    public void replyTemp(Message message) {
        this.message.replyTemp(message);
    }

}
