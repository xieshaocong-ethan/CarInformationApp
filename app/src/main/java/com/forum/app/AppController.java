package com.forum.app;

import android.app.Application;

import com.example.car.BuildConfig;
import com.forum.ui.activity.CrashLogActivity;

import net.danlew.android.joda.JodaTimeAndroid;

public class AppController extends Application implements Thread.UncaughtExceptionHandler {

    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);

        if (!BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable e) {
        CrashLogActivity.start(this, e);
        System.exit(1);
    }

}
