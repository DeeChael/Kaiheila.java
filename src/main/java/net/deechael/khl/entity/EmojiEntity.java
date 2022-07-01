package net.deechael.khl.entity;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.Emoji;
import net.deechael.khl.api.User;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.gate.Gateway;

public class EmojiEntity extends KaiheilaObject implements Emoji {

    private String id;
    private int type;
    private String name;
    private String userId;

    public EmojiEntity(Gateway gateway, JsonNode node) {
        super(gateway);
        this.setId(node.get("id").asText());
        this.setName(node.get("name").asText());
        this.setType(node.get("emoji_type").asInt());
        this.setUserId(node.get("user_info").get("id").asText());
    }

    /**
     * 开黑啦唯一标识符 Emoji Id
     *
     * @return Emoji Id
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 获取表情上传用户
     *
     * @return 上传用户
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取表情上传用户
     *
     * @return 上传用户
     */
    @Override
    public User getUploader() {
        return getGateway().getKaiheilaBot().getCacheManager().getUserCache().getElementById(userId);
    }
}
