package net.deechael.khl.entity;

import net.deechael.khl.api.SelfUser;

public class SelfUserEntity implements SelfUser {
    private final UserEntity user;

    public SelfUserEntity(UserEntity user) {
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }
}
