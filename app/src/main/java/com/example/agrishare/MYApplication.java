package com.example.agrishare;


import android.app.Application;
import android.content.Context;

public class MYApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MYApplication.context = getApplicationContext();
    }
    public static Context getAppContext() {
        return MYApplication.context;
    }
}
