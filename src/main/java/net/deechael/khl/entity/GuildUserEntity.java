package net.deechael.khl.entity;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.Guild;
import net.deechael.khl.api.GuildUser;
import net.deechael.khl.api.Role;
import net.deechael.khl.gate.Gateway;
import net.deechael.khl.util.TimeUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GuildUserEntity extends UserEntity implements GuildUser {

    private String userId;
    private String nickname;
    private List<Role> roles;
    private LocalDateTime joinedAt;
    private LocalDateTime activeTime;

    private Guild guild;

    public GuildUserEntity(Gateway gateway, JsonNode node) {
        super(gateway, node);
        this.setId(node.get("id").asText());
        this.setNickname(node.get("nickname").asText());
        this.setJoinedAt(TimeUtil.convertUnixTimeMillisecondLocalDateTime(node.get("joined_at").asLong()));
        this.setActiveTime(TimeUtil.convertUnixTimeMillisecondLocalDateTime(node.get("active_time").asLong()));
        List<Role> roles = new ArrayList<>();
        node.get("roles").forEach(r -> {
            roles.add(this.getGateway().getKaiheilaBot().getCacheManager().getRoleCache().getElementById(r.asInt()));
        });
        this.setRoles(roles);
    }

    public String getId() {
        return userId;
    }

    public void setId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public LocalDateTime getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(LocalDateTime activeTime) {
        this.activeTime = activeTime;
    }

    @Override
    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

}
