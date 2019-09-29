package com.haiyunshan.zima;

import android.app.Application;

public class ZimaApp extends Application {

    private static ZimaApp sInstance;

    public static final ZimaApp instance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
    }
}
