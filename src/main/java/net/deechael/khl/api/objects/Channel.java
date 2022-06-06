package net.deechael.khl.api.objects;

/**
 * 服务器表情，用户可以创建文本频道或语言频道
 */
public interface Channel {

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

}
