package net.deechael.khl.entity;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.User;
import net.deechael.khl.api.VoiceChannel;
import net.deechael.khl.gate.Gateway;
import net.deechael.khl.message.Message;
import net.deechael.khl.message.ReceivedMessage;
import net.deechael.khl.restful.RestRoute;
import net.deechael.khl.type.ChannelTypes;

import java.util.Arrays;

public class VoiceChannelEntity extends ChannelEntity implements VoiceChannel {

    public VoiceChannelEntity(Gateway gateway, JsonNode node) {
        super(gateway, node);
    }

    public VoiceChannelEntity(Gateway gateway, JsonNode node, boolean isEvent) {
        super(gateway, node, isEvent);
    }


    @Override
    public ReceivedMessage sendMessage(String message, boolean isKMarkdown) {
        throw new RuntimeException("你不能在一个语音频道中发送消息");
    }

    @Override
    public ReceivedMessage sendMessage(Message message) {
        throw new RuntimeException("你不能在一个语音频道中发送消息");
    }

    @Override
    public ReceivedMessage sendTempMessage(String message, String uid, boolean isKMarkdown) {
        throw new RuntimeException("你不能在一个语音频道中发送消息");
    }

    @Override
    public ReceivedMessage sendTempMessage(String message, User user, boolean isKMarkdown) {
        throw new RuntimeException("你不能在一个语音频道中发送消息");
    }

    @Override
    public ReceivedMessage sendTempMessage(Message message, User user) {
        throw new RuntimeException("你不能在一个语音频道中发送消息");
    }

    @Override
    public ReceivedMessage sendTempMessage(Message message, String uid) {
        throw new RuntimeException("你不能在一个语音频道中发送消息");
    }

    @Override
    public ReceivedMessage reply(Message message, String msgId) {
        throw new RuntimeException("你不能在一个语音频道中发送消息");
    }

    @Override
    public ReceivedMessage replyTemp(Message message, User user, String msgId) {
        throw new RuntimeException("你不能在一个语音频道中发送消息");
    }

    @Override
    public ReceivedMessage replyTemp(Message message, String uid, String msgId) {
        throw new RuntimeException("你不能在一个语音频道中发送消息");
    }

    @Override
    public void moveTo(User user) {
        this.moveTo(new User[]{user});
    }

    @Override
    public void moveTo(User[] user) {
        this.getGateway().executeRequest(RestRoute.Channel.CHANNEL_MOVE_USER.compile()
                .withQueryParam("target_id", this.getId())
                .withQueryParam("user_ids", Arrays.stream(user).map(User::getId).toArray())
        );
    }

    @Override
    public ChannelTypes getChannelType() {
        return ChannelTypes.VOICE;
    }

}
