package net.deechael.khl.restful;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.deechael.khl.annotation.NonParameters;
import net.deechael.khl.annotation.OptionalParameters;
import net.deechael.khl.annotation.Parameter;
import net.deechael.khl.annotation.RequiredParameters;
import net.deechael.khl.client.http.HttpMethod;

import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@SuppressWarnings("unused")
public class RestRoute {

    private final String method;
    private final String path;
    private final boolean pageable;
    private final int paramCount;

    public RestRoute(HttpMethod method, String path, boolean pageable) {
        this.path = path;
        this.method = method.name();
        this.pageable = pageable;
        this.paramCount = getConstructionPathParamCount(path);
    }

    private static JsonArray dealArray(Object object) {
        JsonArray array = new JsonArray();
        int length = Array.getLength(object);
        for (int i = 0; i < length; i++) {
            Object v = Array.get(object, i);
            if (v.getClass().isArray()) {
                array.add(dealArray(v));
            } else {
                if (v instanceof JsonElement) {
                    array.add((JsonElement) v);
                } else {
                    JsonObject jo = tryToDealObject(v.toString());
                    if (jo != null) {
                        array.add(jo);
                    } else {
                        array.add(v.toString());
                    }
                }
            }
        }
        return array;
    }

    private static JsonObject tryToDealObject(String string) {
        try {
            return JsonParser.parseString(string).getAsJsonObject();
        } catch (Exception ignored) {
            return null;
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public boolean isPageable() {
        return pageable;
    }

    public int getParamCount() {
        return paramCount;
    }

    private int getConstructionPathParamCount(String path) {
        final int len = path.length();
        int count = 0;
        int p = 0;
        do {
            if ((p = path.indexOf('{', p)) == -1) {
                break;
            }
            if ((p = path.indexOf('}', p)) == -1) {
                throw new IllegalArgumentException("An argument does not have both {}'s for route: " + path);
            } else {
                count++;
            }
        } while (len > p);
        return count;
    }

    public CompiledRoute compile(String... params) {
        if (params.length != this.paramCount) {
            throw new IllegalArgumentException("[" + this.path + "] route.param != param.length");
        }

        StringBuilder compiledRoute = new StringBuilder(path);
        for (String param : params) {
            int ps = compiledRoute.indexOf("{");
            int pe = compiledRoute.indexOf("}");
            compiledRoute.replace(ps, ++pe, param);
        }

        return new CompiledRoute(this, compiledRoute.toString());
    }

    @Override
    public String toString() {
        return method + " /" + path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestRoute restRoute = (RestRoute) o;
        return pageable == restRoute.pageable && paramCount == restRoute.paramCount && method.equals(restRoute.method) && path.equals(restRoute.path);
    }

    /**
     * 服务器操作
     */
    public static class Guild {

        /**
         * 获取当前用户加入的服务器列表
         */
        @OptionalParameters({
                @Parameter(name = "page", location = "query", type = int.class),
                @Parameter(name = "page_size", location = "query", type = int.class),
                @Parameter(name = "sort", location = "query", type = String.class)
        })
        public static final RestRoute GET_GUILD_LIST = new RestRoute(HttpMethod.GET, "guild/list", true);

        /**
         * 获取服务器详情
         */
        @RequiredParameters({
                @Parameter(name = "guild_id", type = String.class)
        })
        public static final RestRoute GET_GUILD_INFO = new RestRoute(HttpMethod.GET, "guild/view", false);

        /**
         * 获取服务器中的用户列表
         */
        @RequiredParameters({
                @Parameter(name = "guild_id", location = "query", type = String.class)
        })
        @OptionalParameters({
                @Parameter(name = "sort", location = "query", type = String.class),
                @Parameter(name = "channel_id", location = "query", type = String.class),
                @Parameter(name = "search", location = "query", type = String.class),
                @Parameter(name = "role_id", location = "query", type = int.class),
                @Parameter(name = "mobile_verified", location = "query", type = int.class),
                @Parameter(name = "active_time", location = "query", type = int.class),
                @Parameter(name = "joined_at", location = "query", type = int.class),
                @Parameter(name = "page", location = "query", type = int.class),
                @Parameter(name = "page_size", location = "query", type = int.class),
                @Parameter(name = "filter_user_id", location = "query", type = String.class)
        })
        public static final RestRoute GET_GUILD_USER_LIST = new RestRoute(HttpMethod.GET, "guild/user-list", true);

        /**
         * 修改服务器中用户的昵称
         */
        public static final RestRoute MODIFY_NICKNAME = new RestRoute(HttpMethod.POST, "guild/nickname", false);

        /**
         * 离开服务器
         */
        public static final RestRoute LEAVE_GUILD = new RestRoute(HttpMethod.POST, "guild/leave", false);

        /**
         * 踢出服务器
         */
        public static final RestRoute KICK_OUT = new RestRoute(HttpMethod.POST, "guild/kickout", false);

        /**
         * 服务器静音闭麦列表
         */
        public static final RestRoute GET_MUTED_LIST = new RestRoute(HttpMethod.GET, "guild-mute/list", false);

        /**
         * 添加服务器静音或闭麦
         */
        public static final RestRoute ADD_MUTED_USER = new RestRoute(HttpMethod.POST, "guild-mute/create", false);

        /**
         * 删除服务器静音或闭麦
         */
        public static final RestRoute DEL_MUTED_USER = new RestRoute(HttpMethod.POST, "guild-mute/delete", false);
    }

    /**
     * 频道
     */
    public static class Channel {
        /**
         * 获取频道列表
         */
        public static final RestRoute CHANNEL_LIST = new RestRoute(HttpMethod.GET, "channel/list", true);

        /**
         * 获取频道详情
         */
        public static final RestRoute CHANNEL_DETAILS = new RestRoute(HttpMethod.GET, "channel/view", false);

        /**
         * 更新频道信息
         */
        public static final RestRoute UPDATE_CHANNEL = new RestRoute(HttpMethod.POST, "channel/update", false);

        /**
         * 创建频道
         */
        public static final RestRoute CREATE_CHANNEL = new RestRoute(HttpMethod.POST, "channel/create", false);

        /**
         * 删除频道
         */
        public static final RestRoute DELETE_CHANNEL = new RestRoute(HttpMethod.POST, "channel/delete", false);

        /**
         * 列出语音频道内用户
         */
        public static final RestRoute CHANNEL_USER_LIST = new RestRoute(HttpMethod.GET, "channel/user-list", false);

        /**
         * 语音频道之间移动用户
         */
        public static final RestRoute CHANNEL_MOVE_USER = new RestRoute(HttpMethod.POST, "channel/move-user", false);

        /**
         * 获取频道角色权限详情
         */
        public static final RestRoute CHANNEL_ROLE_LIST = new RestRoute(HttpMethod.GET, "channel-role/index", false);

        /**
         * 创建频道角色权限
         */
        public static final RestRoute CREATE_CHANNEL_ROLE = new RestRoute(HttpMethod.POST, "channel-role/create", false);

        /**
         * 更新频道角色权限
         */
        public static final RestRoute UPDATE_CHANNEL_ROLE = new RestRoute(HttpMethod.POST, "channel-role/update", false);

        /**
         * 删除频道角色权限
         */
        public static final RestRoute DELETE_CHANNEL_ROLE = new RestRoute(HttpMethod.POST, "channel-role/delete", false);

    }

    /**
     * 频道消息
     */
    public static class Message {

        /**
         * 获取频道聊天消息列表
         */
        public static final RestRoute CHANNEL_MESSAGE_LIST = new RestRoute(HttpMethod.GET, "message/list", false);

        /**
         * 发送频道聊天消息
         */
        public static final RestRoute SEND_CHANNEL_MESSAGE = new RestRoute(HttpMethod.POST, "message/create", false);

        /**
         * 更新频道聊天消息
         */
        public static final RestRoute UPDATE_CHANNEL_MESSAGE = new RestRoute(HttpMethod.POST, "message/update", false);

        /**
         * 删除频道聊天消息
         */
        public static final RestRoute DELETE_CHANNEL_MESSAGE = new RestRoute(HttpMethod.POST, "message/delete", false);

        /**
         * 获取频道消息某个回应的用户列表
         */
        public static final RestRoute CHANNEL_MESSAGE_REACTION_LIST = new RestRoute(HttpMethod.GET, "message/reaction-list", false);

        /**
         * 给某个消息添加回应
         */
        public static final RestRoute ADD_REACTION = new RestRoute(HttpMethod.POST, "message/add-reaction", false);

        /**
         * 删除消息的某个回应
         */
        public static final RestRoute DELETE_REACTION = new RestRoute(HttpMethod.POST, "message/delete-reaction", false);

    }

    /**
     * 频道用户
     */
    public static class ChannelUser {

        /**
         * 获取用户所在的语音频道
         */
        public static final RestRoute GET_JOINED_CHANNEL = new RestRoute(HttpMethod.GET, "channel-user/get-joined-channel", true);

    }

    /**
     * 私聊会话
     */
    public static class DirectMessageSession {

        /**
         * 获取私信聊天会话列表
         */
        public static final RestRoute USER_CHAT_SESSION_LIST = new RestRoute(HttpMethod.GET, "user-chat/list", true);

        /**
         * 获取私信聊天会话详情
         */
        public static final RestRoute GET_USER_CHAT_SESSION = new RestRoute(HttpMethod.GET, "user-chat/view", false);

        /**
         * 创建私信聊天会话
         */
        public static final RestRoute CREATE_USER_CHAT_SESSION = new RestRoute(HttpMethod.POST, "user-chat/create", false);

        /**
         * 删除私信聊天会话
         */
        public static final RestRoute DELETE_USER_CHAT_SESSION = new RestRoute(HttpMethod.POST, "user-chat/delete", false);
    }

    /**
     * 私聊
     */
    public static class DirectMessage {

        /**
         * 获取私信聊天消息列表
         */
        public static final RestRoute DIRECT_MESSAGE_LIST = new RestRoute(HttpMethod.GET, "direct-message/list", false);

        /**
         * 发送私信聊天消息
         */
        public static final RestRoute SEND_DIRECT_MESSAGE = new RestRoute(HttpMethod.POST, "direct-message/create", false);

        /**
         * 更新私信聊天消息
         */
        public static final RestRoute UPDATE_DIRECT_MESSAGE = new RestRoute(HttpMethod.POST, "direct-message/update", false);

        /**
         * 删除私信聊天消息
         */
        public static final RestRoute DELETE_DIRECT_MESSAGE = new RestRoute(HttpMethod.POST, "direct-message/delete", false);

        /**
         * 获取频道消息某个回应的用户列表
         */
        public static final RestRoute DIRECT_MESSAGE_REACTION_LIST = new RestRoute(HttpMethod.GET, "direct-message/reaction-list", false);

        /**
         * 给某个消息添加回应
         */
        public static final RestRoute ADD_DIRECT_MESSAGE_REACTION = new RestRoute(HttpMethod.POST, "direct-message/add-reaction", false);

        /**
         * 删除消息的某个回应
         */
        public static final RestRoute DELETE_DIRECT_MESSAGE_REACTION = new RestRoute(HttpMethod.POST, "direct-message/delete-reaction", false);
    }

    /**
     * 服务器角色
     */
    public static class GuildRole {
        /**
         * 获取服务器角色列表
         */
        public static final RestRoute GUILD_ROLE_LIST = new RestRoute(HttpMethod.GET, "guild-role/list", true);

        /**
         * 创建服务器角色
         */
        public static final RestRoute CREATE_GUILD_ROLE = new RestRoute(HttpMethod.POST, "guild-role/create", false);

        /**
         * 更新服务器角色
         */
        public static final RestRoute UPDATE_GUILD_ROLE = new RestRoute(HttpMethod.POST, "guild-role/update", false);

        /**
         * 删除服务器角色
         */
        public static final RestRoute DELETE_GUILD_ROLE = new RestRoute(HttpMethod.POST, "guild-role/delete", false);

        /**
         * 赋予用户角色
         */
        public static final RestRoute GRANT_GUILD_ROLE = new RestRoute(HttpMethod.POST, "guild-role/grant", false);

        /**
         * 删除用户角色
         */
        public static final RestRoute REVOKE_GUILD_ROLE = new RestRoute(HttpMethod.POST, "guild-role/revoke", false);
    }

    /**
     * 亲密度
     */
    public static class Intimacy {
        /**
         * 获取用户的亲密度
         */
        public static final RestRoute INTIMACY_LIST = new RestRoute(HttpMethod.GET, "intimacy/index", false);

        /**
         * 更新用户的亲密度
         */
        public static final RestRoute UPDATE_USER_INTIMACY = new RestRoute(HttpMethod.POST, "intimacy/update", false);
    }

    /**
     * 服务器表情
     */
    public static class GuildEmoji {

        /**
         * 获取服务器表情列表
         */
        public static final RestRoute GUILD_EMOJI_LIST = new RestRoute(HttpMethod.GET, "guild-emoji/list", true);

        /**
         * 创建服务器表情
         */
        public static final RestRoute CREATE_GUILD_EMOJI = new RestRoute(HttpMethod.POST, "guild-emoji/create", false);

        /**
         * 更新服务器表情
         */
        public static final RestRoute UPDATE_GUILD_EMOJI = new RestRoute(HttpMethod.POST, "guild-emoji/update", false);

        /**
         * 删除服务器表情
         */
        public static final RestRoute DELETE_GUILD_EMOJI = new RestRoute(HttpMethod.POST, "guild-emoji/delete", false);
    }

    /**
     * 服务器邀请
     */
    public static class Invite {

        /**
         * 获取邀请列表
         */
        public static final RestRoute INVITE_LIST = new RestRoute(HttpMethod.GET, "invite/list", true);

        /**
         * 创建邀请链接
         */
        public static final RestRoute CREATE_INVITE = new RestRoute(HttpMethod.POST, "invite/create", false);

        /**
         * 删除邀请链接
         */
        public static final RestRoute DELETE_INVITE = new RestRoute(HttpMethod.POST, "invite/delete", false);

    }

    /**
     * 大杂烩
     */
    public static class Misc {

        /**
         * 获取网关连接地址
         */
        public static final RestRoute GATEWAY = new RestRoute(HttpMethod.GET, "gateway/index", false);

        /**
         * 上传文件/图片
         */
        public static final RestRoute UPLOAD_ASSET = new RestRoute(HttpMethod.POST, "asset/create", false);

        /**
         * 获取当前用户信息
         */
        public static final RestRoute SELF_USER = new RestRoute(HttpMethod.GET, "user/me", false);

        /**
         * 获取目标用户信息
         */
        public static final RestRoute GET_USER_INFO = new RestRoute(HttpMethod.GET, "user/view", false);

        /**
         * 机器人下线
         */
        public static final RestRoute BOT_OFFLINE = new RestRoute(HttpMethod.POST, "user/offline", false);

    }

    /**
     * 游戏
     */
    public static class Game {

        /**
         * 获取游戏列表
         */
        @NonParameters
        public static final RestRoute GAME_LIST = new RestRoute(HttpMethod.GET, "game", true);

        /**
         * 添加新游戏
         * 一天最多添加5个
         */
        @RequiredParameters(@Parameter(name = "name", type = String.class))
        @OptionalParameters(@Parameter(name = "icon", type = String.class))
        public static final RestRoute CREATE_GAME = new RestRoute(HttpMethod.POST, "game/create", false);

        /**
         * 更新游戏数据
         */
        @RequiredParameters(@Parameter(name = "id", type = int.class))
        @OptionalParameters({
                @Parameter(name = "name", type = String.class),
                @Parameter(name = "icon", type = String.class)
        })
        public static final RestRoute UPDATE_GAME = new RestRoute(HttpMethod.POST, "game/update", false);

        /**
         * 删除游戏
         */
        @RequiredParameters(@Parameter(name = "id", type = int.class))
        public static final RestRoute DELETE_GAME = new RestRoute(HttpMethod.POST, "game/delete", false);

        /**
         * 开始玩
         */
        @RequiredParameters({
                @Parameter(name = "id", type = int.class),
                @Parameter(name = "data_type", type = int.class, mustBe = "int:1")
        })
        public static final RestRoute SET_ACTIVITY = new RestRoute(HttpMethod.POST, "game/activity", false);

        /**
         * 停止玩
         */
        @RequiredParameters(@Parameter(name = "data_type", type = int.class, mustBe = "int:1"))
        public static final RestRoute DELETE_ACTIVITY = new RestRoute(HttpMethod.POST, "game/delete-activity", false);

    }

    public static class CompiledRoute {

        private final RestRoute restRoute;
        private final String compiledRoute;
        private final HashMap<String, Object> queryParams = new HashMap<>();

        public CompiledRoute(RestRoute restRoute, String compiledRoute) {
            this.restRoute = restRoute;
            this.compiledRoute = compiledRoute;
        }

        public CompiledRoute clearQueryParam() {
            queryParams.clear();
            return this;
        }

        public CompiledRoute withQueryParam(String k, Object v) {
            queryParams.put(k, v);
            return this;
        }

        public RestRoute getRoute() {
            return restRoute;
        }

        public String getMethod() {
            return restRoute.getMethod();
        }

        public String getCompiledRoute() {
            return compiledRoute;
        }

        public String getQueryStringCompleteRoute() {
            if (queryParams.size() == 0) {
                return compiledRoute;
            }
            StringBuilder queryString = new StringBuilder();
            queryParams.forEach((k, v) -> {
                String keyEnc = null;
                String valEnc = null;
                keyEnc = URLEncoder.encode(k, StandardCharsets.UTF_8);
                valEnc = URLEncoder.encode(String.valueOf(v), StandardCharsets.UTF_8);
                queryString.append('&').append(keyEnc).append('=').append(valEnc);
            });
            queryString.deleteCharAt(0);
            return compiledRoute + "?" + queryString;
        }

        public JsonObject getQueryJson() {
            JsonObject object = new JsonObject();
            queryParams.forEach((k, v) -> {
                if (v.getClass().isArray()) {
                    object.add(k, dealArray(v));
                } else {
                    if (v instanceof JsonElement) {
                        object.add(k, (JsonElement) v);
                    } else {
                        JsonObject jo = tryToDealObject(v.toString());
                        if (jo != null) {
                            object.add(k, jo);
                        } else {
                            object.addProperty(k, v.toString());
                        }
                    }
                }
            });
            return object;
        }

        @Override
        public String toString() {
            return getMethod() + " /" + compiledRoute;
        }

    }

}
