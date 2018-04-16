package com.afolayan.med_manager.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.afolayan.med_manager.services.NotificationService;
import com.afolayan.med_manager.services.WakeReminderIntentService;

import static com.afolayan.med_manager.utils.Utilities.MEDICATION;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/15/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive: received wake up from alarm manager" );
        String medicationJson = intent.getStringExtra(MEDICATION);
        WakeReminderIntentService.acquireStaticLock(context);

        Intent newIntent = new Intent(context, NotificationService.class);
        newIntent.putExtra(MEDICATION, medicationJson);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startService(newIntent);
    }

}