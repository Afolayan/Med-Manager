package com.afolayan.med_manager;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.afolayan.med_manager.adapter.FrequencyListAdapter;
import com.afolayan.med_manager.database.model.Medication;
import com.afolayan.med_manager.database.viewmodel.MedicationViewModel;
import com.afolayan.med_manager.job.SetReminderJob;
import com.afolayan.med_manager.model.Frequency;
import com.afolayan.med_manager.utils.AccountUtils;
import com.afolayan.med_manager.utils.Utilities;
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.afolayan.med_manager.model.Frequency.loadFrequencyTypes;
import static com.afolayan.med_manager.utils.Utilities.addReminder;

public class NewMedicationActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialogFragment.OnTimeSet {

    private static final String TAG = NewMedicationActivity.class.getSimpleName();
    private static final int REQUEST_READ_CALENDAR = 90;

    private EditText etMedicationName, etMedicationDescription;
    private Spinner frequencySpinner;
    private Button btnSelectPeriod, btnChooseTime;

    int selectedHour = 0, selectedMin = 0;
    Date startDate, endDate;

    Medication mMedication;

    private AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Frequency frequency = loadFrequencyTypes().get(position);
            frequencySpinner.setTag(frequency);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medication);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.new_medication);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v->finish());
        toolbar.inflateMenu(R.menu.menu_new_medication);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.action_done:
                    processDone();
                    return true;
            }
            return false;
        });

        etMedicationName = findViewById(R.id.et_medication_name);
        etMedicationDescription = findViewById(R.id.et_medication_description);

        frequencySpinner = findViewById(R.id.spinner_frequency);

        FrequencyListAdapter frequencyListAdapter = new FrequencyListAdapter(loadFrequencyTypes(), this);
        frequencySpinner.setAdapter(frequencyListAdapter);

        frequencySpinner.setOnItemSelectedListener(itemSelectedListener);
        btnChooseTime = findViewById(R.id.btn_choose_time);
        btnSelectPeriod = findViewById(R.id.btn_select_period);

        btnSelectPeriod.setOnClickListener(this);
        btnChooseTime.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_medication, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_done:
                processDone();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void processDone() {
        View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        if(startDate == null || endDate == null){
            Snackbar.make(view, "Select a period in date", Snackbar.LENGTH_LONG).show();
            return;
        }

        Frequency selectedFrequency = (Frequency) frequencySpinner.getTag();
        if(selectedFrequency==null){
            Snackbar.make(view, "Select the frequency of the medication", Snackbar.LENGTH_LONG).show();
            return;
        }

        String medicationName = etMedicationName.getText().toString().trim();
        String medicationDesc = etMedicationDescription.getText().toString().trim();

        if(TextUtils.isEmpty(medicationName)){
            etMedicationName.setError("Set the name of the medication");
            return;
        }if(TextUtils.isEmpty(medicationDesc)){
            etMedicationDescription.setError("Set the description of the medication");
            return;
        }

        if(selectedHour == 0 && selectedMin == 0){
            Snackbar.make(view, "Select the time to start this medication", Snackbar.LENGTH_LONG).show();
            return;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        startCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
        startCalendar.set(Calendar.MINUTE, selectedMin);
        startDate = startCalendar.getTime();

        Medication medication = new Medication();
        medication.setDateFrom(startDate.getTime());
        medication.setDateTo(endDate.getTime());
        medication.setName(medicationName);
        medication.setDescription(medicationDesc);
        medication.setFrequency(selectedFrequency.getName());
        medication.setFrequencyCount(selectedFrequency.getCount());
        medication.setDateCreated(System.currentTimeMillis());
        String email = AccountUtils.getUserEmail(this);
        medication.setEmail(email);


        mMedication = medication;

        //save medication info
        MedicationViewModel viewModel = new MedicationViewModel(this);
        viewModel.insertSingleMedication(medication);

        //use frequency value to setup reminders
        long days = Utilities.getDateDiff(startDate, endDate, TimeUnit.DAYS);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        SetReminderJob.setReminder(medication);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_CALENDAR) && !ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_CALENDAR)) {

                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.READ_CALENDAR, android.Manifest.permission.WRITE_CALENDAR},
                                REQUEST_READ_CALENDAR);

                        // REQUEST_READ_CALENDAR is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
        } else {
            // Permission has already been granted
            addReminder(this, medication);
        }

        Snackbar.make(findViewById(android.R.id.content), "Reminder Created for "+medicationName, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", v -> NewMedicationActivity.this.finish()).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_choose_time:
                openTimePicker();
                break;
            case R.id.btn_select_period:
                openDateRangePicker();
                break;
        }
    }

    private void openTimePicker() {
        TimePickerDialogFragment newFragment = new TimePickerDialogFragment();
        newFragment.setOnTimeSetListener(this);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if(mMedication != null) {
                        addReminder(this, mMedication);
                    }
                } else {

                    // permission denied. Disable the
                    // functionality that depends on this permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_CALENDAR, android.Manifest.permission.WRITE_CALENDAR},
                            REQUEST_READ_CALENDAR);
                }
                break;
            }
        }
    }

    private void openDateRangePicker() {
        SmoothDateRangePickerFragment smoothDateRangePickerFragment
                = SmoothDateRangePickerFragment.newInstance(
                (dateRangePickerFragment, yearStart, monthStart, dayStart, yearEnd, monthEnd, dayEnd) -> {
                    // grab the date range, do what you want
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(yearStart, monthStart, dayStart);
                    startDate = calendar.getTime();

                    calendar = Calendar.getInstance();
                    calendar.set(yearEnd, monthEnd, dayEnd, 23, 59); //end of the day
                    endDate = calendar.getTime();


                    SimpleDateFormat dateFormat = Utilities.DATE_FORMAT;
                    SimpleDateFormat dateFormat1 = Utilities.TIME_FORMAT;
                    String startDateString = dateFormat.format(startDate);
                    String endDateString = dateFormat.format(endDate);

                    btnSelectPeriod.setText(String.format("%s - %s", startDateString, endDateString));
                });

        smoothDateRangePickerFragment.setMinDate(Calendar.getInstance());
        smoothDateRangePickerFragment.show(getFragmentManager(), "smoothDateRangePicker");

    }


    @Override
    public void onTimeSet(int hourOfDay, int minute) {
        this.selectedHour = hourOfDay;
        this.selectedMin = minute;
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        timeCalendar.set(Calendar.MINUTE, minute);
        String timeString = Utilities.TIME_ONLY_FORMAT.format(timeCalendar.getTime());
        btnChooseTime.setText(timeString);
    }
}
