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
     * ???????????????
     */
    public static class Guild {

        /**
         * ??????????????????????????????????????????
         */
        @OptionalParameters({
                @Parameter(name = "page", location = "query", type = int.class),
                @Parameter(name = "page_size", location = "query", type = int.class),
                @Parameter(name = "sort", location = "query", type = String.class)
        })
        public static final RestRoute GET_GUILD_LIST = new RestRoute(HttpMethod.GET, "guild/list", true);

        /**
         * ?????????????????????
         */
        @RequiredParameters({
                @Parameter(name = "guild_id", type = String.class)
        })
        public static final RestRoute GET_GUILD_INFO = new RestRoute(HttpMethod.GET, "guild/view", false);

        /**
         * ?????????????????????????????????
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
         * ?????????????????????????????????
         */
        public static final RestRoute MODIFY_NICKNAME = new RestRoute(HttpMethod.POST, "guild/nickname", false);

        /**
         * ???????????????
         */
        public static final RestRoute LEAVE_GUILD = new RestRoute(HttpMethod.POST, "guild/leave", false);

        /**
         * ???????????????
         */
        public static final RestRoute KICK_OUT = new RestRoute(HttpMethod.POST, "guild/kickout", false);

        /**
         * ???????????????????????????
         */
        public static final RestRoute GET_MUTED_LIST = new RestRoute(HttpMethod.GET, "guild-mute/list", false);

        /**
         * ??????????????????????????????
         */
        public static final RestRoute ADD_MUTED_USER = new RestRoute(HttpMethod.POST, "guild-mute/create", false);

        /**
         * ??????????????????????????????
         */
        public static final RestRoute DEL_MUTED_USER = new RestRoute(HttpMethod.POST, "guild-mute/delete", false);
    }

    /**
     * ??????
     */
    public static class Channel {
        /**
         * ??????????????????
         */
        public static final RestRoute CHANNEL_LIST = new RestRoute(HttpMethod.GET, "channel/list", true);

        /**
         * ??????????????????
         */
        public static final RestRoute CHANNEL_DETAILS = new RestRoute(HttpMethod.GET, "channel/view", false);

        /**
         * ??????????????????
         */
        public static final RestRoute UPDATE_CHANNEL = new RestRoute(HttpMethod.POST, "channel/update", false);

        /**
         * ????????????
         */
        public static final RestRoute CREATE_CHANNEL = new RestRoute(HttpMethod.POST, "channel/create", false);

        /**
         * ????????????
         */
        public static final RestRoute DELETE_CHANNEL = new RestRoute(HttpMethod.POST, "channel/delete", false);

        /**
         * ???????????????????????????
         */
        public static final RestRoute CHANNEL_USER_LIST = new RestRoute(HttpMethod.GET, "channel/user-list", false);

        /**
         * ??????????????????????????????
         */
        public static final RestRoute CHANNEL_MOVE_USER = new RestRoute(HttpMethod.POST, "channel/move-user", false);

        /**
         * ??????????????????????????????
         */
        public static final RestRoute CHANNEL_ROLE_LIST = new RestRoute(HttpMethod.GET, "channel-role/index", false);

        /**
         * ????????????????????????
         */
        public static final RestRoute CREATE_CHANNEL_ROLE = new RestRoute(HttpMethod.POST, "channel-role/create", false);

        /**
         * ????????????????????????
         */
        public static final RestRoute UPDATE_CHANNEL_ROLE = new RestRoute(HttpMethod.POST, "channel-role/update", false);

        /**
         * ????????????????????????
         */
        public static final RestRoute DELETE_CHANNEL_ROLE = new RestRoute(HttpMethod.POST, "channel-role/delete", false);

    }

    /**
     * ????????????
     */
    public static class Message {

        /**
         * ??????????????????????????????
         */
        public static final RestRoute CHANNEL_MESSAGE_LIST = new RestRoute(HttpMethod.GET, "message/list", false);

        /**
         * ????????????????????????
         */
        public static final RestRoute SEND_CHANNEL_MESSAGE = new RestRoute(HttpMethod.POST, "message/create", false);

        /**
         * ????????????????????????
         */
        public static final RestRoute UPDATE_CHANNEL_MESSAGE = new RestRoute(HttpMethod.POST, "message/update", false);

        /**
         * ????????????????????????
         */
        public static final RestRoute DELETE_CHANNEL_MESSAGE = new RestRoute(HttpMethod.POST, "message/delete", false);

        /**
         * ?????????????????????????????????????????????
         */
        public static final RestRoute CHANNEL_MESSAGE_REACTION_LIST = new RestRoute(HttpMethod.GET, "message/reaction-list", false);

        /**
         * ???????????????????????????
         */
        public static final RestRoute ADD_REACTION = new RestRoute(HttpMethod.POST, "message/add-reaction", false);

        /**
         * ???????????????????????????
         */
        public static final RestRoute DELETE_REACTION = new RestRoute(HttpMethod.POST, "message/delete-reaction", false);

    }

    /**
     * ????????????
     */
    public static class ChannelUser {

        /**
         * ?????????????????????????????????
         */
        public static final RestRoute GET_JOINED_CHANNEL = new RestRoute(HttpMethod.GET, "channel-user/get-joined-channel", true);

    }

    /**
     * ????????????
     */
    public static class DirectMessageSession {

        /**
         * ??????????????????????????????
         */
        public static final RestRoute USER_CHAT_SESSION_LIST = new RestRoute(HttpMethod.GET, "user-chat/list", true);

        /**
         * ??????????????????????????????
         */
        public static final RestRoute GET_USER_CHAT_SESSION = new RestRoute(HttpMethod.GET, "user-chat/view", false);

        /**
         * ????????????????????????
         */
        public static final RestRoute CREATE_USER_CHAT_SESSION = new RestRoute(HttpMethod.POST, "user-chat/create", false);

        /**
         * ????????????????????????
         */
        public static final RestRoute DELETE_USER_CHAT_SESSION = new RestRoute(HttpMethod.POST, "user-chat/delete", false);
    }

    /**
     * ??????
     */
    public static class DirectMessage {

        /**
         * ??????????????????????????????
         */
        public static final RestRoute DIRECT_MESSAGE_LIST = new RestRoute(HttpMethod.GET, "direct-message/list", false);

        /**
         * ????????????????????????
         */
        public static final RestRoute SEND_DIRECT_MESSAGE = new RestRoute(HttpMethod.POST, "direct-message/create", false);

        /**
         * ????????????????????????
         */
        public static final RestRoute UPDATE_DIRECT_MESSAGE = new RestRoute(HttpMethod.POST, "direct-message/update", false);

        /**
         * ????????????????????????
         */
        public static final RestRoute DELETE_DIRECT_MESSAGE = new RestRoute(HttpMethod.POST, "direct-message/delete", false);

        /**
         * ?????????????????????????????????????????????
         */
        public static final RestRoute DIRECT_MESSAGE_REACTION_LIST = new RestRoute(HttpMethod.GET, "direct-message/reaction-list", false);

        /**
         * ???????????????????????????
         */
        public static final RestRoute ADD_DIRECT_MESSAGE_REACTION = new RestRoute(HttpMethod.POST, "direct-message/add-reaction", false);

        /**
         * ???????????????????????????
         */
        public static final RestRoute DELETE_DIRECT_MESSAGE_REACTION = new RestRoute(HttpMethod.POST, "direct-message/delete-reaction", false);
    }

    /**
     * ???????????????
     */
    public static class GuildRole {
        /**
         * ???????????????????????????
         */
        public static final RestRoute GUILD_ROLE_LIST = new RestRoute(HttpMethod.GET, "guild-role/list", true);

        /**
         * ?????????????????????
         */
        public static final RestRoute CREATE_GUILD_ROLE = new RestRoute(HttpMethod.POST, "guild-role/create", false);

        /**
         * ?????????????????????
         */
        public static final RestRoute UPDATE_GUILD_ROLE = new RestRoute(HttpMethod.POST, "guild-role/update", false);

        /**
         * ?????????????????????
         */
        public static final RestRoute DELETE_GUILD_ROLE = new RestRoute(HttpMethod.POST, "guild-role/delete", false);

        /**
         * ??????????????????
         */
        public static final RestRoute GRANT_GUILD_ROLE = new RestRoute(HttpMethod.POST, "guild-role/grant", false);

        /**
         * ??????????????????
         */
        public static final RestRoute REVOKE_GUILD_ROLE = new RestRoute(HttpMethod.POST, "guild-role/revoke", false);
    }

    /**
     * ?????????
     */
    public static class Intimacy {
        /**
         * ????????????????????????
         */
        public static final RestRoute INTIMACY_LIST = new RestRoute(HttpMethod.GET, "intimacy/index", false);

        /**
         * ????????????????????????
         */
        public static final RestRoute UPDATE_USER_INTIMACY = new RestRoute(HttpMethod.POST, "intimacy/update", false);
    }

    /**
     * ???????????????
     */
    public static class GuildEmoji {

        /**
         * ???????????????????????????
         */
        public static final RestRoute GUILD_EMOJI_LIST = new RestRoute(HttpMethod.GET, "guild-emoji/list", true);

        /**
         * ?????????????????????
         */
        public static final RestRoute CREATE_GUILD_EMOJI = new RestRoute(HttpMethod.POST, "guild-emoji/create", false);

        /**
         * ?????????????????????
         */
        public static final RestRoute UPDATE_GUILD_EMOJI = new RestRoute(HttpMethod.POST, "guild-emoji/update", false);

        /**
         * ?????????????????????
         */
        public static final RestRoute DELETE_GUILD_EMOJI = new RestRoute(HttpMethod.POST, "guild-emoji/delete", false);
    }

    /**
     * ???????????????
     */
    public static class Invite {

        /**
         * ??????????????????
         */
        public static final RestRoute INVITE_LIST = new RestRoute(HttpMethod.GET, "invite/list", true);

        /**
         * ??????????????????
         */
        public static final RestRoute CREATE_INVITE = new RestRoute(HttpMethod.POST, "invite/create", false);

        /**
         * ??????????????????
         */
        public static final RestRoute DELETE_INVITE = new RestRoute(HttpMethod.POST, "invite/delete", false);

    }

    /**
     * ?????????
     */
    public static class Misc {

        /**
         * ????????????????????????
         */
        public static final RestRoute GATEWAY = new RestRoute(HttpMethod.GET, "gateway/index", false);

        /**
         * ????????????/??????
         */
        public static final RestRoute UPLOAD_ASSET = new RestRoute(HttpMethod.POST, "asset/create", false);

        /**
         * ????????????????????????
         */
        public static final RestRoute SELF_USER = new RestRoute(HttpMethod.GET, "user/me", false);

        /**
         * ????????????????????????
         */
        public static final RestRoute GET_USER_INFO = new RestRoute(HttpMethod.GET, "user/view", false);

        /**
         * ???????????????
         */
        public static final RestRoute BOT_OFFLINE = new RestRoute(HttpMethod.POST, "user/offline", false);

    }

    /**
     * ??????
     */
    public static class Game {

        /**
         * ??????????????????
         */
        @NonParameters
        public static final RestRoute GAME_LIST = new RestRoute(HttpMethod.GET, "game", true);

        /**
         * ???????????????
         * ??????????????????5???
         */
        @RequiredParameters(@Parameter(name = "name", type = String.class))
        @OptionalParameters(@Parameter(name = "icon", type = String.class))
        public static final RestRoute CREATE_GAME = new RestRoute(HttpMethod.POST, "game/create", false);

        /**
         * ??????????????????
         */
        @RequiredParameters(@Parameter(name = "id", type = int.class))
        @OptionalParameters({
                @Parameter(name = "name", type = String.class),
                @Parameter(name = "icon", type = String.class)
        })
        public static final RestRoute UPDATE_GAME = new RestRoute(HttpMethod.POST, "game/update", false);

        /**
         * ????????????
         */
        @RequiredParameters(@Parameter(name = "id", type = int.class))
        public static final RestRoute DELETE_GAME = new RestRoute(HttpMethod.POST, "game/delete", false);

        /**
         * ?????????
         */
        @RequiredParameters({
                @Parameter(name = "id", type = int.class),
                @Parameter(name = "data_type", type = int.class, mustBe = "int:1")
        })
        public static final RestRoute SET_ACTIVITY = new RestRoute(HttpMethod.POST, "game/activity", false);

        /**
         * ?????????
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
