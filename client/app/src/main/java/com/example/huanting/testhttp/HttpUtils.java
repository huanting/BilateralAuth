package com.example.huanting.testhttp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * create by huanting on 2018/9/9 下午6:50
 */
public class HttpUtils {

    public static UrlResponse okPostData(String portal, String urlStr, Map<String, String> headers, byte[] buffer, int connectTimeout, int readTimeout) throws IOException {
        return okPostData(portal, urlStr, headers, buffer, connectTimeout, readTimeout, null, null);
    }

    public static UrlResponse okPostData(String portal, String urlStr, Map<String, String> headers, byte[] buffer, int connectTimeout, int readTimeout, X509TrustManager trustManager, KeyManagerCreator keyManagerCreator) throws IOException {
        String traceId = UUID.randomUUID().toString().replace("-", "");

        StringBuilder builder = new StringBuilder(urlStr);
        if (!urlStr.contains("?"))
            builder.append("?");
        if (builder.toString().contains("="))
            builder.append("&");
        builder.append("trace_id").append("=").append(urlEncode(traceId));

        if (headers == null)
            headers = new LinkedHashMap<String, String>();
        headers.put("trace_id", traceId);
        headers.put("portal", portal);

        URL url = new URL(builder.toString());
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        for (Map.Entry<String, String> header:headers.entrySet())
            requestBuilder.addHeader(header.getKey(), header.getValue());

        String contentType = headers.containsKey("Content-Type") ? headers.get("Content-Type") : "application/octet-stream";
        RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), buffer);
        requestBuilder.post(requestBody);

        OkHttpClient client = getApiOkHttpClient(connectTimeout, readTimeout, trustManager, keyManagerCreator);
        Response response = client.newCall(requestBuilder.build()).execute();
        return new UrlResponse(response);
    }


    private static OkHttpClient getApiOkHttpClient(int connectTimeout, int readTimeout, X509TrustManager trustManager, KeyManagerCreator keyManagerCreator) {
        OkHttpClient.Builder clientBuilder = OkHttpClientFactory.obtainApiClient().newBuilder();
        clientBuilder.writeTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        if (trustManager != null) {
            clientBuilder.sslSocketFactory(new SSLSocketFactoryCompat(trustManager, keyManagerCreator), trustManager);
//            clientBuilder.sslSocketFactory(getSSLSocketFactory(trustManager, keyManagerCreator), trustManager);
        }
        return clientBuilder.build();
    }


    private static SSLSocketFactory getSSLSocketFactory(X509TrustManager mtm, KeyManagerCreator keyManagerCreator) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerCreator == null ? null : keyManagerCreator.create(), (mtm != null) ? new X509TrustManager[] { mtm } : null, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new AssertionError(); // The system has no TLS. Just give up.
        }
    }


    // url encode a string with UTF-8 encoding
    public static String urlEncode(String src) {
        try {
            return URLEncoder.encode(src, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
