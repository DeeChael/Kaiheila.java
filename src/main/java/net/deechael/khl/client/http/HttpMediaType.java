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

package net.deechael.khl.client.http;

import java.util.Arrays;

public enum HttpMediaType {

    TEXT("text"),
    FORM_BODY("application/x-www-form-urlencoded"),
    FORM_DATA("multipart/form-data"),
    JSON("application/json"),
    ;

    private final String name;

    HttpMediaType(String name) {
        this.name = name;
    }

    public static HttpMediaType of(String name) {
        return Arrays.stream(HttpMediaType.values()).filter(type -> name.matches(type.name)).findFirst().orElse(TEXT);
    }

    public String getName() {
        return name;
    }
}
