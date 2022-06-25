package net.deechael.khl.util;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.Role;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.entity.*;

import java.util.ArrayList;
import java.util.List;

public class EntitiesBuilder extends KaiheilaObject {

    public EntitiesBuilder(KaiheilaBot rabbit) {
        super(rabbit);
    }

    public SelfUserEntity buildSelfUserEntity(JsonNode node) {
        UserEntity user = buildUserEntity(node);
        return new SelfUserEntity(user);
    }

    public UserEntity buildUserEntity(JsonNode node) {
        UserEntity user = new UserEntity(getKaiheilaBot());
        user.setId(node.get("id").asText());
        user.setUsername(node.get("username").asText());
        user.setIdentifyNum(node.get("identify_num").asText());
        user.setOnline(node.get("online").asBoolean());
        user.setBanner(node.get("banner").asText());
        user.setBot(node.get("bot").asBoolean());
        user.setStatus(node.get("status").asInt());
        user.setAvatar(node.get("avatar").asText());
        user.setMobileVerified(node.get("mobile_verified").asBoolean());
        return user;
    }

    public GuildUserEntity buildGuildUserEntity(JsonNode node) {
        GuildUserEntity member = new GuildUserEntity(getKaiheilaBot());
        member.setId(node.get("id").asText());
        member.setNickname(node.get("nickname").asText());
        member.setJoinedAt(TimeUtil.convertUnixTimeMillisecondLocalDateTime(node.get("joined_at").asLong()));
        member.setActiveTime(TimeUtil.convertUnixTimeMillisecondLocalDateTime(node.get("active_time").asLong()));
        List<Role> roles = new ArrayList<>();
        node.get("roles").forEach(r -> {
            roles.add(this.getKaiheilaBot().getCacheManager().getRoleCache().getElementById(r.asInt()));
        });
        member.setRoles(roles);
        return member;
    }

    public GuildEntity buildGuild(JsonNode node) {
        GuildEntity guild = new GuildEntity(getKaiheilaBot());
        guild.setId(node.get("id").asText());
        guild.setTopic(node.get("topic").asText());
        guild.setMasterId(node.get("master_id").asText());
        this.updateGuildEntityForEvent(guild, node);
        return guild;
    }

    public RoleEntity buildRoleEntity(JsonNode node) {
        RoleEntity role = new RoleEntity(getKaiheilaBot());
        role.setRoleId(node.get("role_id").asInt());
        role.setName(node.get("name").asText());
        role.setColor(node.get("color").asInt());
        role.setPosition(node.get("position").asInt());
        role.setHoist(node.get("hoist").asInt());
        role.setMentionable(node.get("mentionable").asInt());
        role.setPermissions(node.get("permissions").asInt());
        return role;
    }

    private List<PermissionOverwrite> buildPermissionOverwrites(JsonNode node, boolean userBase) {
        ArrayList<PermissionOverwrite> overwrites = new ArrayList<>();
        node.forEach(item -> {
            PermissionOverwrite overwrite = new PermissionOverwrite();
            overwrite.setAllow(item.get("allow").asInt());
            overwrite.setDeny(item.get("deny").asInt());
            if (userBase) {
                overwrite.setTargetUserId(item.get("user").get("id").asText());
            } else {
                overwrite.setTargetRoleId(item.get("role_id").asInt());
            }
            overwrites.add(overwrite);
        });
        return overwrites;
    }

    private ChannelEntity buildChannelEntityBase(JsonNode node, boolean isUpdate) {
        ChannelEntity channel = new ChannelEntity(getKaiheilaBot());
        channel.setId(node.get("id").asText());
        channel.setType(node.get("type").asInt());
        channel.setName(node.get("name").asText());
        if (!isUpdate) channel.setMasterId(node.get("master_id").asText());
        channel.setGuildId(node.get("guild_id").asText());
        channel.setTopic(node.get("topic").asText());
        channel.setCategory(node.get("is_category").asBoolean());
        channel.setParentId(node.get("parent_id").asText());
        channel.setLevel(node.get("level").asInt());
        channel.setSlowMode(node.get("slow_mode").asInt());
        if (!isUpdate) channel.setLimitAmount(node.get("limit_amount").asInt());
        return channel;
    }

    public ChannelEntity buildChannelEntity(JsonNode node) {
        ChannelEntity channel = buildChannelEntityBase(node, false);
        channel.setPermissionOverwrites(buildPermissionOverwrites(node.get("permission_overwrites"), false));
        channel.setPermissionUsers(buildPermissionOverwrites(node.get("permission_users"), true));
        channel.setPermissionSync(node.get("permission_sync").asInt() == 1);
        return channel;
    }


    public EmojiEntity buildGuildEmojiEntity(JsonNode node) {
        EmojiEntity emoji = new EmojiEntity(getKaiheilaBot());
        emoji.setId(node.get("id").asText());
        emoji.setName(node.get("name").asText());
        emoji.setType(node.get("emoji_type").asInt());
        emoji.setUserId(node.get("user_info").get("id").asText());
        return emoji;
    }

    public ChannelEntity buildChannelEntityForEvent(JsonNode node) {
        ChannelEntity channel = buildChannelEntityBase(node, true);
        channel.setPermissionOverwrites(new ArrayList<>());
        channel.setPermissionUsers(new ArrayList<>());
        channel.setPermissionSync(true);
        return channel;
    }

    public GuildEntity updateGuildEntityForEvent(GuildEntity guild, JsonNode node) {
        guild.setName(node.get("name").asText());
        guild.setIcon(node.get("icon").asText());
        guild.setNotifyType(node.get("notify_type").asInt());
        guild.setRegion(node.get("region").asText());
        guild.setEnableOpen(node.get("enable_open").asBoolean());
        guild.setOpenId(node.get("open_id").asText());
        guild.setDefaultChannelId(node.get("default_channel_id").asText());
        guild.setWelcomeChannelId(node.get("welcome_channel_id").asText());
        return guild;
    }

    public UserEntity updateUserEntityForEvent(UserEntity user, JsonNode node) {
        user.setUsername(node.get("username").asText());
        user.setAvatar(node.get("avatar").asText());
        return user;
    }

}
