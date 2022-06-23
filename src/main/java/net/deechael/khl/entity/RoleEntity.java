package net.deechael.khl.entity;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.Guild;
import net.deechael.khl.api.User;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.api.Role;
import net.deechael.khl.client.http.HttpCall;
import net.deechael.khl.client.http.RequestBuilder;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.core.OperationResult;
import net.deechael.khl.restful.RestRoute;

public class RoleEntity extends KaiheilaObject implements Role {

    private Guild guild;
    private int roleId;
    private String name;
    private int color;
    private int position;
    private int hoist;
    private int mentionable;
    private int permissions;

    public RoleEntity(KaiheilaBot rabbit) {
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

    @Override
    public Guild getGuild() {
        return this.guild;
    }

    @Override
    public String getGuildId() {
        return this.guild.getId();
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
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

    public OperationResult grantUser(User user) {
        return this.grantUser(user.getId());
    }

    public OperationResult grantUser(String uid){
        HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.GuildRole.GRANT_GUILD_ROLE)
                .withData("guild_id", guild.getId())
                .withData("user_id", uid)
                .withData("role_id", String.valueOf(this.getId()))
                .build();
        try{
            JsonNode data = callRestApi(req);
            if (handleResult(data))
                return OperationResult.success(data.get("data"));
            else
                return OperationResult.failed();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return OperationResult.failed();
        }
    }

    public OperationResult revokeUser(User user) {
        return this.revokeUser(user.getId());
    }

    public OperationResult revokeUser(String uid){
        HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.GuildRole.REVOKE_GUILD_ROLE)
                .withData("guild_id", guild.getId())
                .withData("user_id", uid)
                .withData("role_id", String.valueOf(this.getId()))
                .build();
        try{
            JsonNode data = callRestApi(req);
            if (handleResult(data))
                return OperationResult.success(data.get("data"));
            else
                return OperationResult.failed();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return OperationResult.failed();
        }
    }

}
