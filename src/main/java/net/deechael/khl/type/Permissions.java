package net.deechael.khl.type;

public enum Permissions {

    ADMINISTRATOR(1, "管理员", "拥有此权限会获得完整的管理权，包括绕开所有其他权限（包括频道权限）限制，属于危险权限。"),
    MANAGE_GUILD(2, "管理服务器", "拥有此权限的成员可以修改服务器名称和更换区域。"),
    VISIT_MANAGEMENT_JOURNAL(4, "查看管理日志", "拥有此权限的成员可以查看服务器的管理日志。"),
    CREATE_INVITATION(8, "创建服务器邀请", "能否创建服务器邀请链接"),
    MANAGE_INVITATION(16, "管理邀请", "拥有该权限可以管理服务器的邀请"),
    MANAGE_CHANNEL(32, "频道管理", "拥有此权限的成员可以创建新的频道以及编辑或删除已存在的频道。"),
    KICK_USER(64, "踢出用户", ""),
    BAN_USER(128, "封禁用户", ""),
    MANAGE_CUSTOM_EMOJI(256, "管理自定义表情", ""),
    MODIFY_NICKNAME(512, "修改服务器昵称", "拥有此权限的用户可以更改他们的昵称。"),
    MANAGE_ROLE(1024, "管理角色权限", "拥有此权限成员可以创建新的角色和编辑删除低于该角色的身份。"),
    VISIT_CHANNEL(2048, "查看文字、语音频道", ""),
    SEND_MESSAGE(4096, "发布消息", ""),
    MANAGE_MESSAGE(8192, "管理消息", "拥有此权限的成员可以删除其他成员发出的消息和置顶消息。"),
    POST_FILE(16384, "上传文件", ""),
    JOIN_VOICE_CHAT(32768, "语音链接", ""),
    MANAGE_VOICE_CHAT(65536, "语音管理", "拥有此权限的成员可以把其他成员移动和踢出频道；但此类移动仅限于在该成员和被移动成员均有权限的频道之间进行。"),
    MENTION_ALL(131072, "提及@全体成员", "拥有此权限的成员可使用@全体成员以提及该频道中所有成员。"),
    ADD_REACTION(262144, "添加反应", "拥有此权限的成员可以对消息添加新的反应。"),
    FOLLOW_REACTION(524288, "跟随添加反应", "拥有此权限的成员可以跟随使用已经添加的反应。"),
    CONNECT_VOICE_CHAT_PASSIVELY(1048576, "被动连接语音频道", "拥有此限制的成员无法主动连接语音频道，只能在被动邀请或被人移动时，才可以进入语音频道。"),
    PRESS_TO_TALK_ONLY(2097152, "仅使用按键说话", "拥有此限制的成员加入语音频道后，只能使用按键说话。"),
    ALWAYS_MICRO_PHONE(4194304, "使用自由麦", "没有此权限的成员，必须在频道内使用按键说话。"),
    SPEAK(8388608, "说话", ""),
    SILENCE_GUILD(16777216, "服务器静音", ""),
    MUTE_GUILD(33554432, "服务器闭麦", ""),
    MODIFY_OTHERS_NICKNAME(67108864, "修改他人昵称", "拥有此权限的用户可以更改他人的昵称"),
    PLAY_ACCOMPANIMENT(134217728, "播放伴奏", "拥有此权限的成员可在语音频道中播放音乐伴奏");

    private final int value;
    private final String name;
    private final String description;

    Permissions(int value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static int mix(Permissions... permissions) {
        int permissionInteger = 0;
        for (Permissions permission : permissions) {
            permissionInteger |= permission.getValue();
        }
        return permissionInteger;
    }

}
