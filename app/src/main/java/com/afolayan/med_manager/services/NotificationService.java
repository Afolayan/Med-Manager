package com.afolayan.med_manager.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.afolayan.med_manager.MedicationActivity;
import com.afolayan.med_manager.R;
import com.afolayan.med_manager.database.model.Medication;
import com.google.gson.Gson;

import static com.afolayan.med_manager.utils.Utilities.MEDICATION;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/12/2018.
 */

public class NotificationService extends WakeReminderIntentService {

    private static int NOTIFICATION = 1001;
    Medication medication;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String medicationString = intent.getStringExtra(MEDICATION);
        Log.e("NotificationService", "onStartCommand: medicationString --> " + medicationString);
        medication = new Gson().fromJson(medicationString, Medication.class);
        Log.e("NotificationService", "onStartCommand: medication --> " + medication);
        createNotification(medication);
        return START_STICKY;
    }

    private void createNotification(Medication medication) {
        NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent myIntent = new Intent(this , MedicationActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, myIntent, 0);

        String channelId = getString(R.string.app_name);
        String appName = getString(R.string.app_name);
        String title = appName + " Reminder";
        String contentText = medication.getName() +" is to be used now";
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentIntent)
                .setBadgeIconType(R.mipmap.ic_launcher)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND)
                .build();


        if (mNM != null && !medicationLimitPassed()) {
            NOTIFICATION = (int) medication.getId();
            mNM.notify(NOTIFICATION, notification);
        }
        stopSelf();
    }

    private boolean medicationLimitPassed(){
        return System.currentTimeMillis() > medication.getDateTo();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        String medicationString = intent.getStringExtra(MEDICATION);
        Log.e("NotificationService", "onBind: medicationString --> " + medicationString);
        medication = new Gson().fromJson(medicationString, Medication.class);
        Log.e("NotificationService", "onBind: medication --> " + medication);
        createNotification(medication);
        return null;
    }

    @Override
    void doReminderWork(Intent intent) {
        String medicationString = intent.getStringExtra(MEDICATION);
        Log.e("NotificationService", "onStartCommand: medicationString --> " + medicationString);
        medication = new Gson().fromJson(medicationString, Medication.class);
        Log.e("NotificationService", "onStartCommand: medication --> " + medication);
        createNotification(medication);
    }
}
