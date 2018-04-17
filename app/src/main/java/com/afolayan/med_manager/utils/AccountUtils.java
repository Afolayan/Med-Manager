package com.afolayan.med_manager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/10/2018.
 */

public class AccountUtils {

    private static final String SIGNED_IN = "signed_in";
    private static final String USER_EMAIL = "user_email";

    public static void setSignedIn(Context context, boolean signedIn){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(SIGNED_IN, signedIn).apply();
    }
    public static boolean hasSignedIn(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(SIGNED_IN, false);
    }

    public static void setUserEmail(Context context, String email){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(USER_EMAIL, email).apply();
    }
    public static String getUserEmail(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(USER_EMAIL, "");
    }
}
