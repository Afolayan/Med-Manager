package com.afolayan.med_manager;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/16/2018.
 */

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static final String TAG = TimePickerDialogFragment.class.getSimpleName();
    OnTimeSet onTimeSetListener;

    public void setOnTimeSetListener(OnTimeSet onTimeSetListener) {
        this.onTimeSetListener = onTimeSetListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        Log.e(TAG, "onTimeSet: hh: "+hourOfDay+"::mm: "+minute );
        onTimeSetListener.onTimeSet(hourOfDay, minute);
        dismiss();
    }

    public interface OnTimeSet{
        void onTimeSet(int hourOfDay, int minute);
    }
}