package net.deechael.khl.entity;

import net.deechael.khl.RabbitImpl;
import net.deechael.khl.core.RabbitObject;

import java.time.LocalDateTime;
import java.util.List;

public class MemberEntity extends RabbitObject {

    private String userId;
    private String nickname;
    private List<Integer> roles;
    private LocalDateTime joinedAt;
    private LocalDateTime activeTime;

    public MemberEntity(RabbitImpl rabbit) {
        super(rabbit);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
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
}
