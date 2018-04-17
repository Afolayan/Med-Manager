package com.afolayan.med_manager.job;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/17/2018.
 */
public class NotificationJobCreator implements JobCreator {

    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case SetReminderJob.TAG:
                return new SetReminderJob();
            default:
                return null;
        }
    }
}