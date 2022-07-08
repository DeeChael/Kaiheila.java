package net.deechael.khl.entity;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.*;
import net.deechael.khl.gate.Gateway;
import net.deechael.khl.message.Message;
import net.deechael.khl.message.ReceivedMessage;
import net.deechael.khl.restful.RestRoute;
import net.deechael.khl.type.ChannelTypes;

public class CategoryEntity extends ChannelEntity implements Category {

    public CategoryEntity(Gateway gateway, JsonNode node) {
        super(gateway, node);
    }

    public CategoryEntity(Gateway gateway, JsonNode node, boolean isEvent) {
        super(gateway, node, isEvent);
    }

    @Override
    public ReceivedMessage sendMessage(String message, boolean isKMarkdown) {
        throw new RuntimeException("你不能在一个频道分组中发送消息");
    }

    @Override
    public ReceivedMessage sendMessage(Message message) {
        throw new RuntimeException("你不能在一个频道分组中发送消息");
    }

    @Override
    public ReceivedMessage sendTempMessage(String message, String uid, boolean isKMarkdown) {
        throw new RuntimeException("你不能在一个频道分组中发送消息");
    }

    @Override
    public ReceivedMessage sendTempMessage(String message, User user, boolean isKMarkdown) {
        throw new RuntimeException("你不能在一个频道分组中发送消息");
    }

    @Override
    public ReceivedMessage sendTempMessage(Message message, User user) {
        throw new RuntimeException("你不能在一个频道分组中发送消息");
    }

    @Override
    public ReceivedMessage sendTempMessage(Message message, String uid) {
        throw new RuntimeException("你不能在一个频道分组中发送消息");
    }

    @Override
    public ReceivedMessage reply(Message message, String msgId) {
        throw new RuntimeException("你不能在一个频道分组中发送消息");
    }

    @Override
    public ReceivedMessage replyTemp(Message message, User user, String msgId) {
        throw new RuntimeException("你不能在一个频道分组中发送消息");
    }

    @Override
    public ReceivedMessage replyTemp(Message message, String uid, String msgId) {
        throw new RuntimeException("你不能在一个频道分组中发送消息");
    }

    @Override
    public ChannelTypes getChannelType() {
        return ChannelTypes.CATEGORY;
    }

    @Override
    public Channel createChannel(ChannelTypes type, String name) {
        if (type == ChannelTypes.CATEGORY) {
            throw new RuntimeException("你不能将分组放在分组内");
        }
        JsonNode data = this.getGateway().executeRequest(RestRoute.Channel.CREATE_CHANNEL.compile()
                .withQueryParam("guild_id", this.getGuildId())
                .withQueryParam("parent_id", this.getId())
                .withQueryParam("name", name)
                .withQueryParam("type", type.getType())
        );
        ChannelEntity channel;
        if (type == ChannelTypes.VOICE) {
            channel = new VoiceChannelEntity(this.getGateway(), data);
        } else {
            channel = new TextChannelEntity(this.getGateway(), data);
        }
        channel.setGuild(this.getGuild());
        this.getGateway().getKaiheilaBot().getCacheManager().getChannelCache().updateElementById(channel.getId(), channel);
        this.getGuild().getChannelIDs().add(channel.getId());
        return channel;
    }

    @Override
    public TextChannel createTextChannel(String name) {
        return (TextChannel) this.createChannel(ChannelTypes.TEXT, name);
    }

    @Override
    public VoiceChannel createVoiceChannel(String name, int limit, VoiceChannel.Quality quality) {
        JsonNode data = this.getGateway().executeRequest(RestRoute.Channel.CREATE_CHANNEL.compile()
                .withQueryParam("guild_id", this.getGuildId())
                .withQueryParam("parent_id", this.getId())
                .withQueryParam("name", name)
                .withQueryParam("type", 2)
                .withQueryParam("limit_amount", limit)
                .withQueryParam("voice_quality", quality.getCode())
        );
        VoiceChannelEntity channel = new VoiceChannelEntity(this.getGateway(), data);
        channel.setGuild(this.getGuild());
        this.getGateway().getKaiheilaBot().getCacheManager().getChannelCache().updateElementById(channel.getId(), channel);
        this.getGuild().getChannelIDs().add(channel.getId());
        return channel;
    }

}
