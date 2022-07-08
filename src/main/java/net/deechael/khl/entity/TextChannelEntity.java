package net.deechael.khl.entity;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.TextChannel;
import net.deechael.khl.api.User;
import net.deechael.khl.gate.Gateway;
import net.deechael.khl.message.BotChannelMessage;
import net.deechael.khl.message.Message;
import net.deechael.khl.message.ReceivedMessage;
import net.deechael.khl.message.TextMessage;
import net.deechael.khl.message.kmarkdown.KMarkdownMessage;
import net.deechael.khl.restful.RestRoute;
import net.deechael.khl.type.ChannelTypes;

public class TextChannelEntity extends ChannelEntity implements TextChannel {

    public TextChannelEntity(Gateway gateway, JsonNode node) {
        super(gateway, node);
    }

    public TextChannelEntity(Gateway gateway, JsonNode node, boolean isEvent) {
        super(gateway, node, isEvent);
    }

    public ReceivedMessage sendMessage(String message, boolean isKMarkdown) {
        return this.sendMessage(isKMarkdown ? KMarkdownMessage.create(message) : new TextMessage(message));
    }

    public ReceivedMessage sendMessage(Message message) {
        JsonNode data = this.getGateway().executeRequest(RestRoute.Message.SEND_CHANNEL_MESSAGE.compile()
                .withQueryParam("target_id", this.getId())
                .withQueryParam("nonce", "bot-message")
                .withQueryParam("content", message.getContent())
                .withQueryParam("type", message.getType().getType())
        );
        return new BotChannelMessage(data.get("msg_id").asText(), data.get("msg_timestamp").asInt(), message, getGateway().getKaiheilaBot().getSelf(), this);
    }

    public ReceivedMessage sendTempMessage(String message, String uid, boolean isKMarkdown) {
        return this.sendTempMessage(isKMarkdown ? KMarkdownMessage.create(message) : new TextMessage(message), uid);
    }

    public ReceivedMessage sendTempMessage(String message, User user, boolean isKMarkdown) {
        return this.sendTempMessage(isKMarkdown ? KMarkdownMessage.create(message) : new TextMessage(message), user.getId());
    }

    public ReceivedMessage sendTempMessage(Message message, User user) {
        return this.sendTempMessage(message, user.getId());
    }

    public ReceivedMessage sendTempMessage(Message message, String uid) {
        JsonNode data = this.getGateway().executeRequest(RestRoute.Message.SEND_CHANNEL_MESSAGE.compile()
                .withQueryParam("target_id", this.getId())
                .withQueryParam("nonce", "bot-message")
                .withQueryParam("content", message.getContent())
                .withQueryParam("type", message.getType().getType())
                .withQueryParam("temp_target_id", uid)
        );
        return new BotChannelMessage(data.get("msg_id").asText(), data.get("msg_timestamp").asInt(), message, getGateway().getKaiheilaBot().getSelf(), this);
    }

    public ReceivedMessage reply(Message message, String msgId) {
        JsonNode data = this.getGateway().executeRequest(RestRoute.Message.SEND_CHANNEL_MESSAGE.compile()
                .withQueryParam("target_id", this.getId())
                .withQueryParam("nonce", "bot-message")
                .withQueryParam("type", message.getType().getType())
                .withQueryParam("content", message.getContent())
                .withQueryParam("quote", msgId)
        );
        return new BotChannelMessage(data.get("msg_id").asText(), data.get("msg_timestamp").asInt(), message, getGateway().getKaiheilaBot().getSelf(), this);
    }

    public ReceivedMessage replyTemp(Message message, User user, String msgId) {
        return this.replyTemp(message, user.getId(), msgId);
    }

    public ReceivedMessage replyTemp(Message message, String uid, String msgId) {
        JsonNode data = this.getGateway().executeRequest(RestRoute.Message.SEND_CHANNEL_MESSAGE.compile()
                .withQueryParam("target_id", this.getId())
                .withQueryParam("nonce", "bot-message")
                .withQueryParam("type", message.getType().getType())
                .withQueryParam("content", message.getContent())
                .withQueryParam("quote", msgId)
                .withQueryParam("temp_target_id", uid)
        );
        return new BotChannelMessage(data.get("msg_id").asText(), data.get("msg_timestamp").asInt(), message, getGateway().getKaiheilaBot().getSelf(), this);
    }

    @Override
    public ChannelTypes getChannelType() {
        return ChannelTypes.TEXT;
    }

    @Override
    public void updateSlowMode(SlowMode slowMode) {
        this.getGateway().executeRequest(RestRoute.Channel.UPDATE_CHANNEL.compile()
                .withQueryParam("channel_id", this.getId())
                .withQueryParam("slow_mode", slowMode.getTime())
        );
    }

    @Override
    public void updateTopic(String topic) {
        this.getGateway().executeRequest(RestRoute.Channel.UPDATE_CHANNEL.compile()
                .withQueryParam("channel_id", this.getId())
                .withQueryParam("topic", topic)
        );
    }

}
