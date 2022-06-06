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

package net.deechael.khl.config;

/**
 * 抽象配置类
 *
 * @param <O> 配置 Builder 构造器
 * @param <R> 配置 Builder 构建结果
 */
public abstract class AbstractConfigurer<O extends Configurer<R>, R> {

    protected final O rootContext;

    public AbstractConfigurer(O o) {
        this.rootContext = o;
    }

    public O and() {
        return rootContext;
    }

    public R build() {
        return rootContext.build();
    }

}