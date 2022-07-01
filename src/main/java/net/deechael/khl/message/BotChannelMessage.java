package net.deechael.khl.message;

import net.deechael.khl.api.Channel;
import net.deechael.khl.api.User;

public class BotChannelMessage extends ReceivedChannelMessage {

    public BotChannelMessage(String id, long msgTimestamp, Message message, User author, Channel channel) {
        super(id, msgTimestamp, message, author, channel);
    }

    public void update(Message content) {

    }

}
