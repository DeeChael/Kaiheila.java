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

package net.deechael.khl.configurer.api;

import net.deechael.khl.api.Bot;
import net.deechael.khl.bot.KaiheilaBotBuilder;
import net.deechael.khl.configurer.AbstractConfigurer;

public class ApiConfigurer extends AbstractConfigurer<KaiheilaBotBuilder, Bot> {

    private String appId;

    private String appSecret;

    private String token;

    private String baseUrl = "https://www.kookapp.cn/api/v3/";

    public ApiConfigurer(KaiheilaBotBuilder kaiheilaBotBuilder) {
        super(kaiheilaBotBuilder);
    }

    public ApiConfigurer appId(String appId) {
        this.appId = appId;
        return this;
    }

    public ApiConfigurer appSecret(String appSecret) {
        this.appSecret = appSecret;
        return this;
    }

    public ApiConfigurer token(String token) {
        this.token = token;
        return this;
    }

    public ApiConfigurer baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getToken() {
        return token;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}