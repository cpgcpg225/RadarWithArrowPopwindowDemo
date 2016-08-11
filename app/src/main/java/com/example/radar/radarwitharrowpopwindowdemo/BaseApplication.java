package com.example.radar.radarwitharrowpopwindowdemo;

import android.app.Application;

/**
 * Created by cpg
 * on 2016/8/11.
 */
public class BaseApplication extends Application{
    private static BaseApplication instance;
    public static BaseApplication getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }
}
