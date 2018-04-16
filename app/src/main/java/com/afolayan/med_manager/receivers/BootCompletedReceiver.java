package com.afolayan.med_manager.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.afolayan.med_manager.database.MedManagerDatabase;
import com.afolayan.med_manager.database.model.Medication;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.afolayan.med_manager.utils.Utilities.MEDICATION;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/15/2018.
 */

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("BootCompletedReceiver", "onReceive: action sent ===> "+intent.getAction());
        if("android.intent.action.BOOT_COMPLETED".equalsIgnoreCase(intent.getAction())) {
            Log.e("BootReceiver", "inside onReceive ");
            MedManagerDatabase database = MedManagerDatabase.getDatabase(context);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Log.e("BootReceiver", "run: medications list is not empty ");
                    List<Medication> medications = database.medicationDao().fetchAllMedicationsList();
                    if (medications != null) {
                        for (Medication medication : medications) {
                            Intent notifierIntent = new Intent(context, AlarmReceiver.class);
                            notifierIntent.putExtra(MEDICATION, new Gson().toJson(medication));
                            PendingIntent alarmPendingIntent = PendingIntent.getService(context, 100,
                                    notifierIntent, FLAG_UPDATE_CURRENT);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(new Date(medication.getDateFrom()));
                            if (alarmManager != null) {
                                int intervalMillis = 60 * 60 * medication.getFrequencyCount() * 1000;
                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, medication.getDateFrom(), intervalMillis, alarmPendingIntent);
                            }
                        }
                    } else {
                        Log.e("BootReceiver", "run: medications list is empty ");
                    }
                }

            });
        }
    }
}