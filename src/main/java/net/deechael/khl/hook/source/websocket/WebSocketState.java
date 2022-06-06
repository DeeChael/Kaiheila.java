/*
 *    Copyright 2020-2021 Rabbit author and contributors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.deechael.khl.hook.source.websocket;

public enum WebSocketState {

    UNKNOWN("未知"),
    INITIALIZING("初始化"),
    FETCH_GATEWAY("获取Gateway"),
    CONNECT("连接中"),
    WAIT_HELLO("等待Hello"),
    HELLO_TIMEOUT("Hello超时"),
    ESTABLISHED("保持连接"),
    PONG_TIMEOUT("PONG超时"),
    RECONNECT("重连服务"),
    SOCKET_FAILED("网络故障"),
    RESTARTING("重启中"),
    FAILED("连接失败"),
    ;

    private final String state;

    WebSocketState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
