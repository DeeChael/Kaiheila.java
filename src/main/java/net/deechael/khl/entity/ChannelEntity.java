package net.deechael.khl.entity;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.api.Channel;
import net.deechael.khl.api.Guild;
import net.deechael.khl.api.User;
import net.deechael.khl.client.http.HttpCall;
import net.deechael.khl.client.http.RequestBuilder;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.core.OperationResult;
import net.deechael.khl.message.Message;
import net.deechael.khl.message.TextMessage;
import net.deechael.khl.message.kmarkdown.KMarkdownMessage;
import net.deechael.khl.restful.RestRoute;

import java.util.List;

public class ChannelEntity extends KaiheilaObject implements Channel {

    private String id;
    private String name;
    private String masterId;
    private String guildId;
    private String topic;
    private boolean category;
    private String parentId;
    private int level;
    private int slowMode;
    private int type;
    private int limitAmount;
    private List<PermissionOverwrite> permissionOverwrites;
    private List<PermissionOverwrite> permissionUsers;
    private boolean permissionSync;

    public ChannelEntity(KaiheilaBot kaiheilaBot) {
        super(kaiheilaBot);
    }

    /**
     * 开黑啦唯一标识符 频道 Id
     *
     * @return 频道 Id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * 在服务器里的频道名称
     *
     * @return 频道名称
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 获取频道创建者用户
     *
     * @return 创建者用户
     */
    @Override
    public User getCreator() {
        return getKaiheilaBot().getCacheManager().getUserCache().getElementById(masterId);
    }

    /**
     * 当前频道是否为分组用途
     *
     * @return true 为分组特殊频道，否则为其他频道
     * @see Channel#getParentId()
     */
    @Override
    public boolean isCategory() {
        return category;
    }

    /**
     * 当前频道的上级频道 Id
     *
     * @return 上级频道 Id
     * @see Channel#isCategory()
     */
    @Override
    public String getParentId() {
        return parentId;
    }

    /**
     * 当前频道排列级别
     * <ul>
     *     <li>越大越靠后</li>
     *     <li>相对于其他频道级别</li>
     * </ul>
     *
     * @return 排列等级
     */
    @Override
    public int getLevel() {
        return level;
    }

    /**
     * 当前频道原始类型编号
     * <ul>
     *     <li>1 文字频道</li>
     *     <li>2 语音频道</li>
     * </ul>
     *
     * @return 原始频道类型编号
     */
    @Override
    public int getChannelTypeRaw() {
        return type;
    }

    /**
     * 当前频道的所属服务器
     *
     * @return 服务器实例
     */
    @Override
    public Guild getGuild() {
        return getKaiheilaBot().getCacheManager().getGuildCache().getElementById(guildId);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setCategory(boolean category) {
        this.category = category;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSlowMode() {
        return slowMode;
    }

    public void setSlowMode(int slowMode) {
        this.slowMode = slowMode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(int limitAmount) {
        this.limitAmount = limitAmount;
    }

    public List<PermissionOverwrite> getPermissionOverwrites() {
        return permissionOverwrites;
    }

    public void setPermissionOverwrites(List<PermissionOverwrite> permissionOverwrites) {
        this.permissionOverwrites = permissionOverwrites;
    }

    public List<PermissionOverwrite> getPermissionUsers() {
        return permissionUsers;
    }

    public void setPermissionUsers(List<PermissionOverwrite> permissionUsers) {
        this.permissionUsers = permissionUsers;
    }

    public boolean isPermissionSync() {
        return permissionSync;
    }

    public void setPermissionSync(boolean permissionSync) {
        this.permissionSync = permissionSync;
    }

    public OperationResult sendMessage(String message, boolean isKMarkdown) {
        return this.sendMessage(isKMarkdown ? KMarkdownMessage.create(message) : new TextMessage(message));
    }

    public OperationResult sendMessage(Message message) {
        HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
                .withData("target_id", this.getId())
                .withData("nonce", "bot-message")
                .withData("content", message.asString())
                .withData("type", message.getType().getType())
                .build();
        try{
            JsonNode data = callRestApi(req);
            if (handleResult(data))
                return OperationResult.success(data.get("data"));
            else
                return OperationResult.failed();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return OperationResult.failed();
        }
    }

    public OperationResult sendTempMessage(String message, String uid, boolean isKMarkdown) {
        return this.sendTempMessage(isKMarkdown ? KMarkdownMessage.create(message) : new TextMessage(message), uid);
    }

    public OperationResult sendTempMessage(String message, User user, boolean isKMarkdown) {
        return this.sendTempMessage(isKMarkdown ? KMarkdownMessage.create(message) : new TextMessage(message), user.getId());
    }

    public OperationResult sendTempMessage(Message message, User user) {
        return this.sendTempMessage(message, user.getId());
    }

    public OperationResult sendTempMessage(Message message, String uid) {
        HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
                .withData("target_id", this.getId())
                .withData("nonce", "bot-message")
                .withData("content", message.asString())
                .withData("type", message.getType().getType())
                .withData("temp_target_id", uid)
                .build();
        try{
            JsonNode data = callRestApi(req);
            if (handleResult(data))
                return OperationResult.success(data.get("data"));
            else
                return OperationResult.failed();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return OperationResult.failed();
        }
    }

    public OperationResult reply(Message message, String msgId) {
        HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
                .withData("channel_id", this.getId())
                .withData("nonce", "bot-message")
                .withData("type", 9)
                .withData("content", message.asString())
                .withData("quote", msgId)
                .build();
        try{
            JsonNode data = callRestApi(req);
            if (handleResult(data))
                return OperationResult.success(data.get("data"));
            else
                return OperationResult.failed();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return OperationResult.failed();
        }
    }

    public OperationResult replyTemp(Message message, User user, String msgId) {
        return this.replyTemp(message, user.getId(), msgId);
    }

    public OperationResult replyTemp(Message message, String uid, String msgId) {
        HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
                .withData("channel_id", this.getId())
                .withData("nonce", "bot-message")
                .withData("type", 9)
                .withData("content", message.asString())
                .withData("quote", msgId)
                .withData("temp_target_id", uid)
                .build();
        try{
            JsonNode data = callRestApi(req);
            if (handleResult(data))
                return OperationResult.success(data.get("data"));
            else
                return OperationResult.failed();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return OperationResult.failed();
        }
    }

    public String createChannelInvite(InviteDuration duration, InviteTimes times) {
        HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.Invite.CREATE_INVITE)
                .withData("channel_id", this.getId())
                .withData("duration", duration)
                .withData("setting_times", times)
                .build();
        try{
            JsonNode data = callRestApi(req);
            if (handleResult(data)) {
                return data.get("url").asText();
            }else{
                Log.error("Failed to create server invite! Reason: {}",data.get("message").asText());
            }
        } catch (InterruptedException e) {
            Log.error("Failed to create server invite! Reason: {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
