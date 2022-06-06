package net.deechael.khl.util;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

public class OkHttpClientSingleton {

    private static OkHttpClient CLIENT;

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.followRedirects(true);
        builder.connectionPool(new ConnectionPool());
        return builder.build();
    }

    public static OkHttpClient getInstance() {
        if (CLIENT == null) {
            CLIENT = createOkHttpClient();
        }
        return CLIENT;
    }

}
