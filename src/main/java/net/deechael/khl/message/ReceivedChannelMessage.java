package net.deechael.khl.message;

import net.deechael.khl.api.Channel;
import net.deechael.khl.api.Guild;
import net.deechael.khl.api.User;

public class ReceivedChannelMessage extends ReceivedMessage {

    private final Channel channel;

    public ReceivedChannelMessage(String id, long msgTimestamp, Message message, User author, Channel channel) {
        super(id, msgTimestamp, message, author);
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public Guild getGuild() {
        return getChannel().getGuild();
    }

    public void reply(Message message) {
        getChannel().reply(message, this.getId());
    }

    public void replyTemp(Message message) {
        getChannel().replyTemp(message, this.getAuthor(), this.getId());
    }

}
