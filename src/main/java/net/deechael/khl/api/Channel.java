package net.deechael.khl.api;

import net.deechael.khl.message.Message;
import net.deechael.khl.message.ReceivedMessage;
import net.deechael.khl.type.ChannelTypes;

/**
 * 服务器表情，用户可以创建文本频道或语言频道
 */
public interface Channel extends KHLObject {

    /**
     * 开黑啦唯一标识符 频道 Id
     *
     * @return 频道 Id
     */
    String getId();

    /**
     * 在服务器里的频道名称
     *
     * @return 频道名称
     */
    String getName();

    /**
     * 获取频道创建者用户
     *
     * @return 创建者用户
     */
    User getCreator();

    /**
     * 当前频道是否为分组用途
     *
     * @return true 为分组特殊频道，否则为其他频道
     * @see Channel#getParentId()
     */
    boolean isCategory();

    /**
     * 当前频道的上级频道 Id
     *
     * @return 上级频道 Id
     * @see Channel#isCategory()
     */
    String getParentId();

    /**
     * 当前频道排列级别
     * <ul>
     *     <li>越大越靠后</li>
     *     <li>相对于其他频道级别</li>
     * </ul>
     *
     * @return 排列等级
     */
    int getLevel();

    /**
     * 当前频道原始类型编号
     * <ul>
     *     <li>1 文字频道</li>
     *     <li>2 语音频道</li>
     * </ul>
     *
     * @return 原始频道类型编号
     */
    int getChannelTypeRaw();

    /**
     * 当前频道的所属服务器
     *
     * @return 服务器实例
     */
    Guild getGuild();

    ReceivedMessage sendMessage(String message, boolean isKMarkdown);

    ReceivedMessage sendMessage(Message message);

    ReceivedMessage sendTempMessage(String message, String uid, boolean isKMarkdown);

    ReceivedMessage sendTempMessage(String message, User user, boolean isKMarkdown);

    ReceivedMessage sendTempMessage(Message message, User user);

    ReceivedMessage sendTempMessage(Message message, String uid);

    ReceivedMessage reply(Message message, String msgId);

    ReceivedMessage replyTemp(Message message, User user, String msgId);

    ReceivedMessage replyTemp(Message message, String uid, String msgId);

    String createChannelInvite(InviteDuration duration, InviteTimes times);

    ChannelTypes getChannelType();

    void updateName(String name);

    void delete();

    PermissionOverwrite getPermissionOverwrite(User user);

    PermissionOverwrite getPermissionOverwrite(Role role);

    enum InviteDuration {
        HALF_HOUR(1800),
        ONE_HOUR(3600),
        SIX_HOURS(21600),
        TWELVE_HOURS(43200),
        ONE_DAY(86400),
        ONE_WEEK(604800),
        NEVER(0);

        public final int value;

        InviteDuration(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    enum InviteTimes {
        ONE_TIME(1),
        FIVE_TIMES(5),
        TEN_TIMES(10),
        TWENTY_FIVE_TIMES(25),
        FIFTY_TIMES(50),
        HUNDRED_TIMES(100),
        UNLIMITED(-1);

        public final int value;

        InviteTimes(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    enum SlowMode {
        FIVE_SEC(5000),
        TEN_SEC(10000),
        FIFTEEN_SEC(15000),
        THIRTY_SEC(30000),
        ONE_MIN(60000),
        TWO_MIN(120000),
        FIVE_MIN(300000),
        TEN_MIN(600000),
        FIFTEEN_MIN(900000),
        THIRTY_MIN(1800000),
        ONE_HOUR(3600000),
        TWO_HOUR(7200000),
        SIX_HOUR(21600000);

        private final int time;

        SlowMode(int time) {
            this.time = time;
        }

        public int getTime() {
            return time;
        }

    }

}
