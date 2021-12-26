package com.ineedyourcode.donotes;

import android.app.Application;
import android.content.Context;

public class DoNotesApp extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        DoNotesApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return DoNotesApp.context;
    }
}
