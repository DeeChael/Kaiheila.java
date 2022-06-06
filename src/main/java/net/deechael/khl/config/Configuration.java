package net.deechael.khl.config;

import net.deechael.khl.config.api.ApiConfigurer;
import net.deechael.khl.config.client.ClientConfigurer;
import net.deechael.khl.config.core.SdkConfigurer;
import net.deechael.khl.config.event.EventSourceConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {
    protected static final Logger Log = LoggerFactory.getLogger(Configuration.class);

    private final SdkConfigurer sdkConfigurer;
    private final ApiConfigurer apiConfigurer;
    private final ClientConfigurer clientConfigurer;
    private final EventSourceConfigurer eventSourceConfigurer;

    public Configuration(SdkConfigurer sdkConfigurer, ClientConfigurer clientConfigurer, ApiConfigurer apiConfigurer, EventSourceConfigurer eventSourceConfigurer) {
        this.sdkConfigurer = sdkConfigurer;
        this.clientConfigurer = clientConfigurer;
        this.apiConfigurer = apiConfigurer;
        this.eventSourceConfigurer = eventSourceConfigurer;
    }

    public SdkConfigurer getSdkConfigurer() {
        return sdkConfigurer;
    }

    public ClientConfigurer getClientConfigurer() {
        return clientConfigurer;
    }

    public ApiConfigurer getApiConfigurer() {
        return apiConfigurer;
    }

    public EventSourceConfigurer getEventSourceConfigurer() {
        return eventSourceConfigurer;
    }
}
