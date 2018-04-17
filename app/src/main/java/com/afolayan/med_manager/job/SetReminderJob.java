package com.afolayan.med_manager.job;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.afolayan.med_manager.MedicationActivity;
import com.afolayan.med_manager.R;
import com.afolayan.med_manager.database.model.Medication;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/17/2018.
 */

public class SetReminderJob extends Job {

    static final String TAG = "SetReminderJob";

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {

        PersistableBundleCompat bundleCompat = params.getExtras();
        NotificationManager mNM = (NotificationManager)getContext().getSystemService(NOTIFICATION_SERVICE);
        Intent myIntent = new Intent(getContext() , MedicationActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, myIntent, 0);

        String channelId = getContext().getString(R.string.app_name);
        String appName = getContext().getString(R.string.app_name);
        String title = appName + " Reminder";
        String reminderName = bundleCompat.getString("name", "");
        String reminderDesc = bundleCompat.getString("desc", "");
        long from = bundleCompat.getLong("from", 0);
        long to = bundleCompat.getLong("to", 0);
        long id = bundleCompat.getLong("id", 0);
        boolean isFirstTime = bundleCompat.getBoolean("isFirstTime", true);
        String contentText = reminderName +" is to be used now";
        Notification notification = new NotificationCompat.Builder(getContext(), channelId)
                .setContentTitle(title)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentIntent)
                .setBadgeIconType(R.mipmap.ic_launcher)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(reminderDesc))
                .setColor(Color.GREEN)

                .build();
        if (mNM != null && !medicationDateLimitPassed(to)) {
            int NOTIFICATION = (int) id;
            mNM.notify(NOTIFICATION, notification);
        }

        if(isFirstTime){
            bundleCompat.putBoolean("isFirstTime", false);
            schedulePeriodic(bundleCompat);
        }
        Set<JobRequest> requests = JobManager.create(getContext()).getAllJobRequests();
        for (JobRequest request: requests) {
            PersistableBundleCompat requestExtras = request.getExtras();
            long maxDate = requestExtras.getLong("to", 0);
            if(medicationDateLimitPassed(maxDate)){
                cancelJob(request.getJobId());
            }
        }
        return Result.SUCCESS;
    }

    private void cancelJob(int jobId) {
        JobManager.instance().cancel(jobId);
    }

    private boolean medicationDateLimitPassed(long to){
        return System.currentTimeMillis() > to;
    }
    public static void setReminder(Medication medication) {
        long oneHourIncrement = 60 * 60 * 1000; //an hour
        int hoursInterval = 24 / medication.getFrequencyCount();
        long frequencyInterval = hoursInterval * oneHourIncrement;
        PersistableBundleCompat bundle = new PersistableBundleCompat();
        bundle.putString("name", medication.getName());
        bundle.putString("desc", medication.getDescription());
        bundle.putLong("from", medication.getDateFrom());
        bundle.putLong("to", medication.getDateTo());
        bundle.putLong("id", medication.getId());
        bundle.putLong("interval", frequencyInterval);
        bundle.putBoolean("isFirstTime", true);

        scheduleExactJob(bundle);
    }
    private static void scheduleExactJob(PersistableBundleCompat bundle) {
        long from = bundle.getLong("from", 0);
        long timeToStart = from - System.currentTimeMillis();
        Log.e(TAG, "scheduleExactJob: from -- > "+from );
        Log.e(TAG, "scheduleExactJob: timeToStart -- > "+timeToStart );
        new JobRequest.Builder(SetReminderJob.TAG)
                .setExact(timeToStart)
                .setExtras(bundle)
                .build()
                .schedule();
    }
    private static void schedulePeriodic(PersistableBundleCompat bundle) {
        long frequencyInterval = bundle.getLong("interval", 0);
        new JobRequest.Builder(SetReminderJob.TAG)
                .setPeriodic(frequencyInterval, TimeUnit.MINUTES.toMillis(5))
                .setUpdateCurrent(true)
                .setExtras(bundle)
                .build()
                .schedule();
    }


}