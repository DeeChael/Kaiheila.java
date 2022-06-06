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

package net.deechael.khl;

import net.deechael.khl.api.Bot;
import net.deechael.khl.config.Configuration;
import net.deechael.khl.config.Configurer;
import net.deechael.khl.config.api.ApiConfigurer;
import net.deechael.khl.config.client.ClientConfigurer;
import net.deechael.khl.config.core.SdkConfigurer;
import net.deechael.khl.config.event.EventSourceConfigurer;

public class RabbitBuilder implements Configurer<Bot> {

    private final SdkConfigurer sdkConfigurer;
    private final ClientConfigurer clientConfigurer;
    private final ApiConfigurer apiConfigurer;
    private final EventSourceConfigurer eventSourceConfigurer;

    private RabbitBuilder() {
        this.sdkConfigurer = new SdkConfigurer(this);
        this.clientConfigurer = new ClientConfigurer(this);
        this.apiConfigurer = new ApiConfigurer(this);
        this.eventSourceConfigurer = new EventSourceConfigurer(this);
    }

    public static RabbitBuilder builder() {
        return new RabbitBuilder();
    }

    public RabbitBuilder createDefault(String token) {
        return this.apiConfigurer.token(token).and();
    }

    public SdkConfigurer sdkConfigurer() {
        return sdkConfigurer;
    }

    public ClientConfigurer clientConfigurer() {
        return clientConfigurer;
    }

    public ApiConfigurer apiConfigurer() {
        return apiConfigurer;
    }

    public EventSourceConfigurer eventSourceConfigurer() {
        return eventSourceConfigurer;
    }

    @Override
    public Bot build() {
        if (apiConfigurer.getToken() == null) {
            throw new IllegalArgumentException("the api token cannot be null");
        }
        Configuration configuration = new Configuration(sdkConfigurer, clientConfigurer, apiConfigurer, eventSourceConfigurer);
        return new RabbitImpl(configuration);
    }
}
