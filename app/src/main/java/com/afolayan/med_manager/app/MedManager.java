package com.afolayan.med_manager.app;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/4/2018.
 */

public class MedManager extends Application {

    public MedManager() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
