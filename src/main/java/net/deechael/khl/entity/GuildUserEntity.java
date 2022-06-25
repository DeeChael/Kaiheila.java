package net.deechael.khl.entity;

import net.deechael.khl.api.Guild;
import net.deechael.khl.api.GuildUser;
import net.deechael.khl.api.Role;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.core.KaiheilaObject;

import java.time.LocalDateTime;
import java.util.List;

public class GuildUserEntity extends UserEntity implements GuildUser {

    private String userId;
    private String nickname;
    private List<Role> roles;
    private LocalDateTime joinedAt;
    private LocalDateTime activeTime;

    private Guild guild;

    public GuildUserEntity(KaiheilaBot rabbit) {
        super(rabbit);
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
