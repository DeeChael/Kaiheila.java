package net.deechael.khl.api;

/**
 * 服务器角色，用户自定义创建的角色（用户标签）
 */
public interface Role {

    /**
     * 开黑啦唯一标识符 角色 Id
     *
     * @return 角色 Id
     */
    int getId();

    /**
     * 服务器内的角色名称
     *
     * @return 角色名称
     */
    String getName();

    /**
     * 角色 RGB 颜色代码
     * <ul>
     *     <li>使用 LSB 字节序排序</li>
     *     <li>颜色代码范围 [0, 16777215]</li>
     * </ul>
     *
     * @return 原始 RGB 数值
     */
    int getColorRaw();

    /**
     * 角色在服务器中排序
     *
     * @return 服务器内排序
     */
    int getOrderPosition();

    /**
     * 拥有当前角色的用户是否在用户栏靠前区域分组显示
     *
     * @return true 在用户栏单独分组显示，否则在默认列表内
     */
    boolean isHoist();

    /**
     * 角色在服务器中是否能被单独提及(@)
     *
     * @return true 可以被提及(@)，否则不能被单独提及(@)
     */
    boolean isMentionable();

    /**
     * 角色在服务器中拥有的默认权限
     * <a href="https://developer.kaiheila.cn/doc/http/guild-role">查看权限文档</a>
     *
     * @return 原始权限值
     */
    int getPermissionsRaw();

}
