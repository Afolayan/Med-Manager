package com.afolayan.med_manager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afolayan.med_manager.database.model.Medication;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.afolayan.med_manager.utils.Utilities.MEDICATION;

/**
 * Created by emmanuel on 24/02/2017.
 *
 */

public class MedicationDetailsDialogFragment extends DialogFragment {

    private OnDismissDialogListener listener;
    private View view;
    private Button dismissDialogButton;
    private AppCompatActivity activity;

    public MedicationDetailsDialogFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MedicationActivity) context;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_medication_details, container);
        dismissDialogButton = view.findViewById(R.id.dismiss_dialog);
        dismissDialogButton.setOnClickListener(v -> {
            if (isVisible()) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout llName = view.findViewById(R.id.med_name);
        final LinearLayout llDescription = view.findViewById(R.id.med_desc);
        LinearLayout llMedStartDate     = view.findViewById(R.id.med_start_date);
        LinearLayout llMedStartTime     = view.findViewById(R.id.med_start_time);
        LinearLayout llMedEndDate     = view.findViewById(R.id.med_end);
        LinearLayout llFrequency  = view.findViewById(R.id.med_frequency);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        if(getArguments() != null) {
            String medicationJson = getArguments().getString(MEDICATION);
            Medication selectedMed = new Gson().fromJson(medicationJson, Medication.class);

            String name = selectedMed.getName();
            String description = selectedMed.getDescription();
            long dateCreated = selectedMed.getDateCreated();
            long dateFrom = selectedMed.getDateFrom();
            long dateTo = selectedMed.getDateTo();
            String frequency = selectedMed.getFrequency();
            String dateFromString = dateFormat.format(new Date(dateFrom));
            String time = timeFormat.format(new Date(dateFrom));
            String dateToString = dateFormat.format(new Date(dateTo));

            setViewFieldValue(llName, R.string.medication_name, name);
            setViewFieldValue(llMedStartDate, R.string.start_date, dateFromString);
            setViewFieldValue(llMedStartTime, R.string.start_time, time);
            setViewFieldValue(llMedEndDate, R.string.end_date, dateToString);
            setViewFieldValue(llDescription, R.string.medication_description, description);
            setViewFieldValue(llFrequency, R.string.frequency, frequency);

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return dialog;
    }


    public void setOnDismiss(OnDismissDialogListener listener){
        this.listener = listener;

    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(listener != null) {
            listener.onDismiss();
        }
    }

    private void setViewFieldValue(LinearLayout view, int resField, String resValue){
        ((TextView)view.findViewById(R.id.field_label)).setText(resField);
        ((TextView)view.findViewById(R.id.field_value)).setText(resValue);
    }


    public interface OnDismissDialogListener {
        void onDismiss();
    }

}
