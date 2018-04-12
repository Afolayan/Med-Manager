package com.afolayan.med_manager.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/8/2018.
 */

public class Utilities {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, MMMM d, yyyy", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_YEAR = new SimpleDateFormat("yyyy", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_NO_DAY = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("MMMM d, yyyy hh:mm", Locale.getDefault());
    public static final String MEDICATION = "MEDICATION";


    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillis = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }
}
