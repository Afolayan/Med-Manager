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
    public static final String MEDICATION = "MEDICATION";


    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillis = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public static void addReminder(Context context, Medication medication){
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
        eventValues.put(CalendarContract.Events.DISPLAY_COLOR, R.color.colorPrimary);
        eventValues.put(CalendarContract.Events.DTSTART, startMillis);
        eventValues.put(CalendarContract.Events.DTEND, endMillis);

        eventValues.put(CalendarContract.Events.RRULE, "FREQ=DAILY;COUNT=2;UNTIL="+endMillis);
        //eventValues.put("eventStatus", 1);
        //eventValues.put("visibility", 3);
        //eventValues.put("transparency", 0);
        eventValues.put(CalendarContract.Events.HAS_ALARM, 1);

        Uri eventUri = context.getContentResolver().insert(Uri.parse(eventUriString), eventValues);
        long eventID = 0;
        if (eventUri != null) {
            eventID = Long.parseLong(eventUri.getLastPathSegment());
        }

        /***************** Event: Reminder(with alert) Adding reminder to event *******************/

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
        //Create 2 instances of calendar
        Calendar medDate = Calendar.getInstance();

        for(Medication medication: sourceList) {
            medDate.setTime(new Date(medication.getDateCreated()));
            //now compare the dates using functions
            int medYear = medDate.get(Calendar.YEAR);
            int medMonth = medDate.get(Calendar.MONTH);
            Log.e("Utilities", medYear+" medicationsInAMonth: "+medMonth);
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
