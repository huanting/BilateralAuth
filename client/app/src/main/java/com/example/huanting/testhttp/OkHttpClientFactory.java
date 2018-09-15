package com.example.huanting.testhttp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class OkHttpClientFactory {

    private static final int DEFAULT_CONN_TIMEOUT = 15;
    private static final int DEFAULT_WRITE_TIMEOUT = 15;
    private static final int DEFAULT_READ_TIMEOUT = 15;

    private static OkHttpClient sApiClient;
    private static OkHttpClient sExoClient;
    private static OkHttpClient sDownloadClient;

    public static OkHttpClient obtainGlideClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_CONN_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    public static OkHttpClient obtainApiClient() {
        if (sApiClient != null)
            return sApiClient;
        synchronized (OkHttpClientFactory.class) {
            if (sApiClient == null) {
                sApiClient = new OkHttpClient.Builder()
                        .connectTimeout(DEFAULT_CONN_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(false)
                        .build();
            }
        }
        return sApiClient;
    }

    public static OkHttpClient obtainExoClient() {
        if (sExoClient != null)
            return sExoClient;

        synchronized (OkHttpClientFactory.class) {
            if (sExoClient == null) {
                sExoClient = new OkHttpClient.Builder()
                        .connectTimeout(DEFAULT_CONN_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                        .build();
            }
        }
        return sExoClient;
    }

    public static OkHttpClient obtainDownloadClient() {
        if (sDownloadClient != null)
            return sDownloadClient;

        synchronized (OkHttpClientFactory.class) {
            if (sDownloadClient == null) {
                sDownloadClient = new OkHttpClient.Builder()
                        .connectTimeout(DEFAULT_CONN_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(false)
                        .build();
            }
        }
        return sDownloadClient;
    }

}
