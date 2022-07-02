package net.deechael.khl.entity;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.PermissionOverwrite;
import net.deechael.khl.api.Role;
import net.deechael.khl.api.User;
import net.deechael.khl.restful.RestRoute;
import net.deechael.khl.type.Permissions;

public class PermissionOverwriteEntity implements PermissionOverwrite {

    private final ChannelEntity channel;

    private int targetRoleId;
    private String targetUserId;
    private int allow = 0;
    private int deny = 0;

    private boolean isNew = true;

    public PermissionOverwriteEntity(ChannelEntity channel) {
        this.channel = channel;
    }

    public PermissionOverwriteEntity(ChannelEntity channel, User user) {
        this(channel);
        this.targetUserId = user.getId();
    }

    public PermissionOverwriteEntity(ChannelEntity channel, Role role) {
        this(channel);
        this.targetRoleId = role.getId();
    }

    public int getTargetRoleId() {
        return targetRoleId;
    }

    public void setTargetRoleId(int targetRoleId) {
        this.targetRoleId = targetRoleId;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public int getAllow() {
        return allow;
    }

    public void setAllow(int allow) {
        this.allow = allow;
    }

    @Override
    public void addAllow(Permissions permission) {
        this.allow |= permission.getValue();
    }

    @Override
    public void addDeny(Permissions permission) {
        this.deny |= permission.getValue();
    }

    public void setOld() {
        isNew = false;
    }

    public int getDeny() {
        return deny;
    }

    public void setDeny(int deny) {
        this.deny = deny;
    }

    @Override
    public void update() {
        if (targetUserId != null) {
            if (this.isNew) {
                JsonNode node = channel.getGateway().executeRequest(RestRoute.Channel.CREATE_CHANNEL_ROLE.compile()
                        .withQueryParam("channel_id", channel.getId())
                        .withQueryParam("type", "user_id")
                        .withQueryParam("value", this.targetUserId)
                );
                this.setAllow(node.get("allow").asInt());
                this.setDeny(node.get("deny").asInt());
                this.setOld();
                this.channel.getPermissionUsers().put(this.targetUserId, this);
            } else {
                channel.getGateway().executeRequest(RestRoute.Channel.UPDATE_CHANNEL_ROLE.compile()
                        .withQueryParam("channel_id", channel.getId())
                        .withQueryParam("type", "user_id")
                        .withQueryParam("value", this.targetUserId)
                        .withQueryParam("allow", this.allow)
                        .withQueryParam("deny", this.deny)
                );
            }
        } else {
            if (this.isNew) {
                JsonNode node = channel.getGateway().executeRequest(RestRoute.Channel.CREATE_CHANNEL_ROLE.compile()
                        .withQueryParam("channel_id", channel.getId())
                        .withQueryParam("type", "role_id")
                        .withQueryParam("value", this.targetRoleId)
                );
                this.setAllow(node.get("allow").asInt());
                this.setDeny(node.get("deny").asInt());
                this.setOld();
                this.channel.getPermissionUsers().put(this.targetUserId, this);
            } else {
                channel.getGateway().executeRequest(RestRoute.Channel.UPDATE_CHANNEL_ROLE.compile()
                        .withQueryParam("channel_id", channel.getId())
                        .withQueryParam("type", "role_id")
                        .withQueryParam("value", this.targetRoleId)
                        .withQueryParam("allow", this.allow)
                        .withQueryParam("deny", this.deny)
                );
            }
        }
    }

}
