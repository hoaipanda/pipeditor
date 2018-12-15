package com.camera.pipeditor;


import android.content.Context;
import android.support.multidex.MultiDexApplication;


public class App extends MultiDexApplication {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
