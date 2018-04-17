package com.afolayan.med_manager.utils;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import com.afolayan.med_manager.R;
import com.afolayan.med_manager.database.model.Medication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/8/2018.
 */

public class Utilities {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, MMMM d, yyyy", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_YEAR = new SimpleDateFormat("yyyy", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_YEAR_MONTH = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_NO_DAY = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("MMMM d, yyyy hh:mm", Locale.getDefault());
    public static final SimpleDateFormat TIME_ONLY_FORMAT = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    public static final String MEDICATION = "MEDICATION";


    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillis = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public static void addReminder(Context context, Medication medication){
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(new Date(medication.getDateFrom()));
        long startMillis = beginTime.getTimeInMillis();
        long oneHourIncrement = 60 * 60 * 1000; //an extra hour
        int hoursInterval = 24 / medication.getFrequencyCount();
        long frequencyIncrement = hoursInterval * oneHourIncrement;
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(new Date(medication.getDateTo()));
        long endMillis = endTime.getTimeInMillis();
        StringBuilder builder = new StringBuilder();
        builder.append(context.getString(R.string.app_name))
                .append("\n")
                .append("Use ").append(medication.getName()).append(" now")
                .append("\n")
                .append(medication.getDescription());
        String notifHeader = String.format(Locale.getDefault(), "Use %s now", medication.getName());
        do{
            addSingleReminder(context, notifHeader, builder.toString(), startMillis, (startMillis+frequencyIncrement));
            startMillis += frequencyIncrement;
        }while (startMillis <= endMillis);
    }

    private static void addSingleReminder(Context context, String notificationHeader, String description, long startTime, long endTime){
        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();
        eventValues.put(CalendarContract.Events.CALENDAR_ID, 1);
        eventValues.put(CalendarContract.Events.TITLE, notificationHeader);
        eventValues.put(CalendarContract.Events.DESCRIPTION, description);
        String regionLocale = Locale.getDefault().toString();
        eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, regionLocale);
        eventValues.put(CalendarContract.Events.DTSTART, startTime);
        eventValues.put(CalendarContract.Events.DTEND, endTime);
        eventValues.put(CalendarContract.Events.HAS_ALARM, 1);

        Uri eventUri = context.getContentResolver().insert(Uri.parse(eventUriString), eventValues);
        long eventID = 0;
        if (eventUri != null) {
            eventID = Long.parseLong(eventUri.getLastPathSegment());
        }

        String reminderUriString = "content://com.android.calendar/reminders";

        ContentValues reminderValues = new ContentValues();

        reminderValues.put("event_id", eventID);
        reminderValues.put("minutes", 1);
        reminderValues.put("method", 1);

        Uri reminderUri = context.getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
        Log.e("Utilities", "addReminder: "+reminderUri );
    }
    public static void addReminder(Context context, Medication medication, int r){
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(new Date(medication.getDateFrom()));
        long startMillis = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        endTime.setTime(new Date(medication.getDateTo()));
        long endMillis = endTime.getTimeInMillis();

        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();

        eventValues.put(CalendarContract.Events.CALENDAR_ID, 1);
        eventValues.put(CalendarContract.Events.TITLE, context.getString(R.string.app_name));
        eventValues.put(CalendarContract.Events.DESCRIPTION, medication.getDescription());
        String regionLocale = Locale.getDefault().toString();
        Log.e("Utilities", "addReminder: locale--> "+regionLocale );
        eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, regionLocale);
        eventValues.put(CalendarContract.Events.DTSTART, startMillis);
        eventValues.put(CalendarContract.Events.DTEND, endMillis);
        eventValues.put(CalendarContract.Events.HAS_ALARM, 1);

        Uri eventUri = context.getContentResolver().insert(Uri.parse(eventUriString), eventValues);
        long eventID = 0;
        if (eventUri != null) {
            eventID = Long.parseLong(eventUri.getLastPathSegment());
        }

        String reminderUriString = "content://com.android.calendar/reminders";

        ContentValues reminderValues = new ContentValues();

        reminderValues.put("event_id", eventID);
        reminderValues.put("minutes", 1);
        reminderValues.put("method", 1);

        Uri reminderUri = context.getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
        Log.e("Utilities", "addReminder: "+reminderUri );
    }

    public static List<Medication> medicationsInAMonth(List<Medication> sourceList, int year, int monthInReview){
        List<Medication> medications = new ArrayList<>();
        Calendar medDate = Calendar.getInstance();

        for(Medication medication: sourceList) {
            medDate.setTime(new Date(medication.getDateCreated()));
            //now compare the dates using functions
            int medYear = medDate.get(Calendar.YEAR);
            int medMonth = medDate.get(Calendar.MONTH);
            if (medYear == year) {
                if (medMonth == monthInReview) {
                    // the date falls in month in review
                    medications.add(medication);
                }
            }
        }
        return medications;
    }
}
