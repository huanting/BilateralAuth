package com.example.huanting.testhttp;

import android.app.Application;

/**
 * create by huanting on 2018/9/9 下午7:02
 */
public class MyApp extends Application {

    public static MyApp instance ;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApp getInstance() {
        return instance;
    }
}
