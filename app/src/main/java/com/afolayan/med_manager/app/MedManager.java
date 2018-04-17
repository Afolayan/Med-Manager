package com.afolayan.med_manager.app;

import android.app.Application;

import com.afolayan.med_manager.job.NotificationJobCreator;
import com.evernote.android.job.JobManager;

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
        JobManager.create(this).addJobCreator(new NotificationJobCreator());
    }

}
