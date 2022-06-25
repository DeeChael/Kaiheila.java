package net.deechael.khl.api;

import java.time.LocalDateTime;
import java.util.List;

public interface GuildUser extends User {

    String getNickname();

    List<Role> getRoles();

    LocalDateTime getJoinedAt();

    LocalDateTime getActiveTime();

    Guild getGuild();

}
