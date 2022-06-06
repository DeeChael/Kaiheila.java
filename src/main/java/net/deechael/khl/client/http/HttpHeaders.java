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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HttpHeaders extends HashMap<String, List<Object>> {

    public void addHeader(String key, Object value) {
        List<Object> objects = getOrDefault(key, new ArrayList<>());
        objects.add(value);
        put(key, objects);
    }

    public void addHeaders(String key, List<Object> values) {
        List<Object> objects = getOrDefault(key, new ArrayList<>());
        objects.addAll(values);
        put(key, objects);
    }

    public void setHeader(String key, Object value) {
        put(key, Collections.singletonList(value));
    }

    public void setHeaders(String key, List<Object> values) {
        put(key, values);
    }

    public void removeHeaders(String key) {
        remove(key);
    }

}
