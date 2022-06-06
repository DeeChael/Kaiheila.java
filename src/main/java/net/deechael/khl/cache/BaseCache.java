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

package net.deechael.khl.cache;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class BaseCache<K, T> implements ICacheView<K, T>, Iterable<T> {

    private final ConcurrentHashMap<K, T> cache = new ConcurrentHashMap<>();
    private final Function<T, String> getNameFunction;

    public BaseCache(Function<T, String> getNameFunction) {
        this.getNameFunction = getNameFunction;
    }

    public int size() {
        return cache.size();
    }

    public void updateElementById(K id, T object) {
        cache.put(id, object);
    }

    public void unloadElementById(K id) {
        cache.remove(id);
    }

    public void unloadAll() {
        cache.clear();
    }

    @Override
    public T getElementByName(String name) {
        return cache.values().stream().filter(entity -> {
            String tName = getNameFunction.apply(entity);
            return tName.equals(name);
        }).findFirst().orElse(null);
    }

    @Override
    public T getElementById(K id) {
        return cache.get(id);
    }

    @Override
    public Iterator<T> iterator() {
        return cache.values().iterator();
    }
}
