package net.deechael.khl.message;

import net.deechael.khl.api.Channel;
import net.deechael.khl.api.User;
import net.deechael.khl.restful.RestRoute;

import java.util.Arrays;

public class BotChannelMessage extends ReceivedChannelMessage {

    public BotChannelMessage(String id, long msgTimestamp, Message message, User author, Channel channel) {
        super(id, msgTimestamp, message, author, channel);
    }

    public void update(Message content) {
        if (Arrays.asList(9, 10).contains(this.getType().getType())) {
            if (content.getType() == this.getType()) {
                this.getChannel().getGateway().executeRequest(RestRoute.Message.UPDATE_CHANNEL_MESSAGE.compile()
                        .withQueryParam("msg_id", this.getId())
                        .withQueryParam("content", content.getContent())
                );
            } else {
                throw new RuntimeException("内容与原消息类型不一致");
            }
        } else {
            throw new RuntimeException("这个消息类型不支持更新内容：" + content.getType().getName());
        }
    }

    public void updateTemp(Message content, User user) {
        if (Arrays.asList(9, 10).contains(this.getType().getType())) {
            if (content.getType() == this.getType()) {
                this.getChannel().getGateway().executeRequest(RestRoute.Message.UPDATE_CHANNEL_MESSAGE.compile()
                        .withQueryParam("msg_id", this.getId())
                        .withQueryParam("content", content.getContent())
                        .withQueryParam("temp_target_id", user.getId())
                );
            } else {
                throw new RuntimeException("内容与原消息类型不一致");
            }
        } else {
            throw new RuntimeException("这个消息类型不支持更新内容：" + content.getType().getName());
        }
    }

}
