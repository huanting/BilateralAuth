package com.example.huanting.testhttp;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.huanting.testhttp.KeyManagerCreator.KEY_STORE_TYPE_P12;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onTestHttp(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendRequest();
                //单向验证
                //verifyPhone();


            }
        }).start();
    }

    private void sendRequest() {
        String url = "https://100.100.97.249:443/bauth/v1/login/op_verify_login_mobile.json";
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        JSONObject jo = new JSONObject();
        try {
            jo.put("clientId", "1111");
            jo.put("appId", "test");
            jo.put("phoneNumber", "13502120890");
            jo.put("captchaCode", "1234");

            X509TrustManager trustManager = new SpecialCertTrustManager("server.crt");
            KeyManagerCreator creator = new KeyManagerCreator(KEY_STORE_TYPE_P12,
                    "client.p12", "client1234");

            Log.d("test", "sendRequest url: " + url);
            UrlResponse response = HttpUtils.okPostData("", url, header, jo.toString().getBytes(),
                    15000, 15000,
                    trustManager, creator);
            Log.d("test", "response: " + response.getContent());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    //只处理单向验证
    private static OkHttpClient okHttpClient;

    public static void verifyPhone() {
        String url = "https://100.100.97.249:443/bauth/v1/login/op_verify_login_mobile.json";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        JSONObject jo = new JSONObject();
        try {
            jo.put("clientId", "1111");
            jo.put("appId", "test");
            jo.put("phoneNumber", "13502120890");
            jo.put("captchaCode", "1234");
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(url);

            for (Map.Entry<String, String> header:headers.entrySet())
                requestBuilder.addHeader(header.getKey(), header.getValue());

            String contentType = headers.containsKey("Content-Type") ? headers.get("Content-Type") : "application/octet-stream";
            RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), jo.toString().getBytes());
            requestBuilder.post(requestBody);


            OkHttpClient httpClient = getOkHttpClient(MyApp.getInstance(),
                    MyApp.getInstance().getAssets().open("server.crt"));

            Log.d("test", "verifyPhone url: " + url);
            Response response = httpClient.newCall(requestBuilder.build()).execute();

            int statusCode = response.code();
            String statusMessage = response.message();
            try {
                String content = response.body().string();

                Log.d("test", "statusCode=" + statusCode + ", msg=" + statusMessage +
                        "content=" + content);
            }catch (NullPointerException e) {
                throw new IOException("response body is null");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取oKHttpClient
     * certificates 证书信息 没有就传null
     * @return
     */
    public static OkHttpClient getOkHttpClient(Application appContext, InputStream... certificates) {
        if (okHttpClient == null) {
            File sdcache = appContext.getExternalCacheDir();
            int cacheSize = 10 * 1024 * 1024;
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
            if (certificates != null){
                builder.sslSocketFactory(getSSLSocketFactory(certificates));
            }
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }
    /**
     * 获取SSLSocketFactory
     *
     * @param certificates 证书流文件
     * @return
     */
    private static SSLSocketFactory getSSLSocketFactory(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
