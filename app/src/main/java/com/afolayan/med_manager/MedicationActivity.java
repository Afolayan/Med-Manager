package com.afolayan.med_manager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afolayan.med_manager.adapter.MedicationListSection;
import com.afolayan.med_manager.database.model.Medication;
import com.afolayan.med_manager.database.model.User;
import com.afolayan.med_manager.database.viewmodel.MedicationViewModel;
import com.afolayan.med_manager.database.viewmodel.UserViewModel;
import com.afolayan.med_manager.utils.AccountUtils;
import com.afolayan.med_manager.utils.Utilities;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import static com.afolayan.med_manager.utils.AccountUtils.hasSignedIn;

public class MedicationActivity extends AppCompatActivity {

    private static final String TAG = MedicationActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 1234;
    private SectionedRecyclerViewAdapter sectionAdapter;
    private View rootView;
    private SearchView searchView;
    private MedicationViewModel medicationViewModel;

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }
    private RecyclerView medicationsRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        if(!hasSignedIn(this)) {
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> startActivity(new Intent(MedicationActivity.this, NewMedicationActivity.class)));
        sectionAdapter = new SectionedRecyclerViewAdapter();
        medicationsRecyclerView = findViewById(R.id.medication_list);
        rootView = findViewById(R.id.root_layout);
        searchView = findViewById(R.id.search_view);

        medicationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicationsRecyclerView.setHasFixedSize(true);
        String email = AccountUtils.getUserEmail(this);

        medicationViewModel = new MedicationViewModel(this);
        medicationViewModel.fetchAllMedications(email)
                .observe(this, medications -> {
                    if(medications != null) {
                        if(medications.size() > 0) {
                            displayMedicationsSection(medications);
                        }
                    }
                });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                medicationViewModel.getSearchMedications(query).observe(MedicationActivity.this, medications ->
                        displayMedicationsSection(medications));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                medicationViewModel.getSearchMedications(newText).observe(MedicationActivity.this, medications ->
                        displayMedicationsSection(medications));
                return true;
            }
        });
    }

    private void displayMedicationsSection(List<Medication> medications) {
        //MedicationListAdapter adapter = new MedicationListAdapter(MedicationActivity.this, medications);
        //medicationsRecyclerView.setAdapter(adapter);
        sectionAdapter = new SectionedRecyclerViewAdapter();
        Calendar now = Calendar.getInstance();
        Calendar lastCal = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        if(medications.size() > 0) {
            Medication lastMedication = medications.get(medications.size() - 1);
            lastCal.setTime(new Date(lastMedication.getDateCreated()));
            int lastYearRecord = lastCal.get(Calendar.YEAR);
            for (int i = currentYear; i >= lastYearRecord; i--) { //loop from the current year to the last on the list
                for (int currentMonth = 12; currentMonth >= 1; currentMonth--) { //loop through the months
                    List<Medication> medicationsInAMonth = Utilities.medicationsInAMonth(medications, currentYear, currentMonth);
                    if (medicationsInAMonth.size() > 0) {
                        MedicationListSection listSection = new MedicationListSection(MedicationActivity.this, medicationsInAMonth);
                        listSection.setDeleteImageClickListener(v -> {
                            Medication vTag = (Medication) v.getTag();
                            medicationViewModel.deleteSingleMedication(vTag);
                            sectionAdapter.notifyDataSetChanged();
                        });
                        sectionAdapter.addSection(listSection);
                    }
                }
            }
            findViewById(R.id.tv_no_medication).setVisibility(View.GONE);
            medicationsRecyclerView.setAdapter(sectionAdapter);
            medicationsRecyclerView.setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.tv_no_medication).setVisibility(View.VISIBLE);
            medicationsRecyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.setQuery("", false);
        rootView.requestFocus();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if(account != null){
            UserViewModel userViewModel = new UserViewModel(this);
            String email = account.getEmail();
            AccountUtils.setUserEmail(this, email);
            String displayName = account.getDisplayName();
            Uri photoUrl = account.getPhotoUrl();

            //Toast.makeText(this, "Welcome, "+displayName, Toast.LENGTH_LONG).show();
            userViewModel.fetchSingleUserByEmail(email, user -> {
                if(user == null){
                    //user does not exist
                    User newUser = new User();
                    newUser.setName(displayName);
                    newUser.setEmail(email);
                    if(photoUrl != null) {
                        newUser.setPhotoUrl(photoUrl.toString());
                    }

                    userViewModel.insertUser(newUser);
                } else {
                    //user exists, update user info
                    if(photoUrl != null) {
                        user.setPhotoUrl(photoUrl.toString());
                    }
                    user.setName(displayName);
                    userViewModel.updateSingleUser(user);
                }
            });
            AccountUtils.setSignedIn(this, true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_medication, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_profile:
                startActivity(new Intent(this, UserProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
