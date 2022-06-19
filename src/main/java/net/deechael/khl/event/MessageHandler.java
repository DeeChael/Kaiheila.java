package net.deechael.khl.event;

import net.deechael.khl.api.Channel;
import net.deechael.khl.api.User;
import net.deechael.khl.entity.message.MessageTypes;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MessageHandler {

    private final MessageTypes[] type;

    public MessageHandler(MessageTypes messageType) {
        this(new MessageTypes[] {messageType});
    }

    public MessageHandler(MessageTypes[] messageTypes) {
        this.type = messageTypes;
    }

    public abstract void onMessage(Channel channel, User user, String message);

    public MessageTypes[] getTypes() {
        return type;
    }

    public int[] getIntTypes() {
        return ArrayUtils.toPrimitive(Arrays.stream(this.getTypes()).map(MessageTypes::getType).toList().toArray(new Integer[0]));
    }

    public List<Integer> getIntTypesList() {
        return Arrays.stream(this.getTypes()).map(MessageTypes::getType).toList();
    }

}
