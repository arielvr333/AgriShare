package com.example.agrishare;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class MYApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){ return context; }
}
