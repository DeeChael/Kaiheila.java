package net.deechael.khl.client.http;

import net.deechael.khl.client.http.impl.OkHttpClientImpl;
import net.deechael.khl.util.OkHttpClientSingleton;

@FunctionalInterface
public interface HttpClientFactory {

    HttpClientFactory DEFAULT_HTTP_CLIENT = () -> new OkHttpClientImpl(OkHttpClientSingleton.getInstance());

    IHttpClient buildHttpClient();

}
