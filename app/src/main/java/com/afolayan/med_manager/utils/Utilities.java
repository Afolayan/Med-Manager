package com.afolayan.med_manager.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/8/2018.
 */

public class Utilities {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, MMMM d, yyyy", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_YEAR = new SimpleDateFormat("yyyy", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_NO_DAY = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("MMMM d, yyyy hh:mm", Locale.getDefault());

}
