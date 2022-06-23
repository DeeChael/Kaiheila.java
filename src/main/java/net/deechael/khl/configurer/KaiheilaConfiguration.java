package net.deechael.khl.configurer;

import net.deechael.khl.configurer.api.ApiConfigurer;
import net.deechael.khl.configurer.client.ClientConfigurer;
import net.deechael.khl.configurer.core.SdkConfigurer;
import net.deechael.khl.configurer.event.EventSourceConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KaiheilaConfiguration {
    protected static final Logger Log = LoggerFactory.getLogger(KaiheilaConfiguration.class);

    private final SdkConfigurer sdkConfigurer;
    private final ApiConfigurer apiConfigurer;
    private final ClientConfigurer clientConfigurer;
    private final EventSourceConfigurer eventSourceConfigurer;

    public static boolean isDebug;

    public KaiheilaConfiguration(SdkConfigurer sdkConfigurer, ClientConfigurer clientConfigurer, ApiConfigurer apiConfigurer, EventSourceConfigurer eventSourceConfigurer) {
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
