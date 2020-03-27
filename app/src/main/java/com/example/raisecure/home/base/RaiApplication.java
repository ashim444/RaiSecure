package com.example.raisecure.home.base;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class RaiApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
