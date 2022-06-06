package net.deechael.khl.entity;

import net.deechael.khl.RabbitImpl;
import net.deechael.khl.api.objects.Role;
import net.deechael.khl.core.RabbitObject;

public class RoleEntity extends RabbitObject implements Role {

    private int roleId;
    private String name;
    private int color;
    private int position;
    private int hoist;
    private int mentionable;
    private int permissions;

    public RoleEntity(RabbitImpl rabbit) {
        super(rabbit);
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    /**
     * 开黑啦唯一角色Id
     *
     * @return 角色 Id
     */
    @Override
    public int getId() {
        return roleId;
    }

    /**
     * 服务器内的角色名称
     *
     * @return 角色名称
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 角色 RGB 颜色代码
     * <ul>
     *     <li>使用 LSB 字节序排序</li>
     *     <li>颜色代码范围 [0, 16777215]</li>
     * </ul>
     *
     * @return 原始RGB
     */
    @Override
    public int getColorRaw() {
        return color;
    }

    /**
     * 角色在服务器中排序
     *
     * @return 服务器内排序
     */
    @Override
    public int getOrderPosition() {
        return position;
    }

    /**
     * 角色在服务器中是否能被单独提及(@)
     *
     * @return true 可以被提及(@)，否则不能被单独提及(@)
     */
    @Override
    public boolean isMentionable() {
        return mentionable == 1;
    }

    /**
     * 拥有当前角色的用户是否在用户栏靠前区域分组显示
     *
     * @return true 在用户栏单独分组显示，否则在默认列表内
     */
    @Override
    public boolean isHoist() {
        return hoist == 1;
    }

    /**
     * 角色在服务器中拥有的默认权限
     * <a href="https://developer.kaiheila.cn/doc/http/guild-role">查看权限文档</a>
     *
     * @return 原始权限值
     */
    @Override
    public int getPermissionsRaw() {
        return permissions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getHoist() {
        return hoist;
    }

    public void setHoist(int hoist) {
        this.hoist = hoist;
    }

    public int getMentionable() {
        return mentionable;
    }

    public void setMentionable(int mentionable) {
        this.mentionable = mentionable;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }
}
