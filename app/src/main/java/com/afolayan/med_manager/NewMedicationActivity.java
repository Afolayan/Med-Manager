package com.afolayan.med_manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import com.afolayan.med_manager.model.Frequency;
import com.afolayan.med_manager.services.NotificationService;
import com.afolayan.med_manager.utils.Utilities;
import com.google.gson.Gson;
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.afolayan.med_manager.utils.Utilities.MEDICATION;

public class NewMedicationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = NewMedicationActivity.class.getSimpleName();
    Toolbar toolbar;

    /*
    et_medication_name
            et_medication_description
            spinner_frequency
            btn_select_period
            btn_choose_time
     */
    EditText etMedicationName, etMedicationDescription;
    Spinner frequencySpinner;
    Button btnSelectPeriod, btnChooseTime;
    String selectedFrequency;

    Date startDate, endDate;

    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.e(TAG, "onItemSelected: item selected at position "+position);
            Frequency frequency = loadFrequencyTypes().get(position);
            Log.e(TAG, "onItemSelected: frequency at position = "+ frequency);
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

        toolbar = findViewById(R.id.toolbar);
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

        Medication medication = new Medication();
        medication.setDateFrom(startDate.getTime());
        medication.setDateTo(endDate.getTime());
        medication.setName(medicationName);
        medication.setDescription(medicationDesc);
        medication.setFrequency(selectedFrequency.getName());
        medication.setDateCreated(System.currentTimeMillis());

        //save medication info
        MedicationViewModel viewModel = new MedicationViewModel(this.getApplication());
        viewModel.insertSingleMedication(medication);

        //use frequency value to setup reminders
        long days = Utilities.getDateDiff(startDate, endDate, TimeUnit.DAYS);
        Log.e(TAG, "processDone: date diff-> "+days );

        Intent notifierIntent = new Intent(this, NotificationService.class);
        notifierIntent.putExtra(MEDICATION, new Gson().toJson(medication));
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent alarmPendingIntent = PendingIntent.getService(this, 100,
                notifierIntent, FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        if (alarmManager != null) {
            int intervalMillis = 60 * 60 * selectedFrequency.getCount() * 1000;
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, startDate.getTime(), intervalMillis, alarmPendingIntent);
        }

        Snackbar.make(findViewById(android.R.id.content), "Reminder Created for "+medicationName, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", v -> NewMedicationActivity.this.finish()).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_choose_time:

                break;
            case R.id.btn_select_period:
                openDateRangePicker();
                break;
        }
    }

    private void openDateRangePicker() {
        SmoothDateRangePickerFragment smoothDateRangePickerFragment
                = SmoothDateRangePickerFragment.newInstance(
                (dateRangePickerFragment, yearStart, monthStart, dayStart, yearEnd, monthEnd, dayEnd) -> {
                    // grab the date range, do what you want
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(yearStart, monthStart, dayStart, 0, 0); //beginning of the day
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

    private List<Frequency> loadFrequencyTypes(){
        List<Frequency> frequencies = new ArrayList<>();

        Frequency onceFrequency = new Frequency("Once a day", 1, 0.5);
        Frequency twiceADay = new Frequency("Twice a day", 2, 0.5);
        Frequency thriceADay = new Frequency("Three time a day", 3, 0.5);
        Frequency afterMeal = new Frequency("After each meal", 3, 0.5);
        Frequency sixHours = new Frequency("Every 6 hours", 4, 0.5);
        Frequency everyHour = new Frequency("Every 1 hour", 1, 0.5);
        Frequency everyFiveMins = new Frequency("Every 5 minutes", 5, 0.5);

        frequencies.add(onceFrequency);
        frequencies.add(twiceADay);
        frequencies.add(thriceADay);
        frequencies.add(afterMeal);
        frequencies.add(sixHours);
        frequencies.add(everyHour);
        frequencies.add(everyFiveMins);

        return frequencies;
    }
}
