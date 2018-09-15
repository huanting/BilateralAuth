package com.example.huanting.testhttp;

import android.util.Log;


import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;

public class KeyManagerCreator {
    private static final String TAG = "secure.ssl.ks";

    public static final String KEY_STORE_TYPE_P12 = "PKCS12";

    private String mKeyStoreType;
    private String mKeyStoreAssets;
    private String mPwd;

    /**
     * a creator of KeyManger[], using to init sslContext.
     * @param keyStoreType see {@link #KEY_STORE_TYPE_P12}
     * @param keyStoreAssets the keyStore file name in assets folder.
     * @param pwd the password of the keystore.
     */
    public  KeyManagerCreator(String keyStoreType, String keyStoreAssets, String pwd) {
        mKeyStoreType = keyStoreType == null ? "" : keyStoreType;
        mKeyStoreAssets = keyStoreAssets == null ? "" : keyStoreAssets;
        mPwd = pwd == null ? "" : pwd;
    }

    public KeyManager[] create() {
        Log.d(TAG, "create: " + mKeyStoreAssets + ", type: " + mKeyStoreType);

        KeyManagerFactory keyManagerFactory;
        try {
            KeyStore clientKeyStore = KeyStore.getInstance(mKeyStoreType);
            clientKeyStore.load(MyApp.getInstance().getAssets().open(mKeyStoreAssets), mPwd.toCharArray());
            keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, mPwd.toCharArray());
        } catch (Exception e) {
            keyManagerFactory = null;
            Log.d(TAG, "create", e);
        }

        return keyManagerFactory == null ? null : keyManagerFactory.getKeyManagers();
    }
}
