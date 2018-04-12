package com.afolayan.med_manager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.afolayan.med_manager.adapter.MedicationListAdapter;
import com.afolayan.med_manager.database.model.Medication;
import com.afolayan.med_manager.database.model.User;
import com.afolayan.med_manager.database.viewmodel.MedicationViewModel;
import com.afolayan.med_manager.database.viewmodel.UserViewModel;
import com.afolayan.med_manager.utils.AccountUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import static com.afolayan.med_manager.utils.AccountUtils.hasSignedIn;

public class MedicationActivity extends AppCompatActivity {

    private static final String TAG = MedicationActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 1234;
    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

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

        RecyclerView medicationsRecyclerView = findViewById(R.id.medication_list);
        medicationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicationsRecyclerView.setHasFixedSize(true);
        String email = AccountUtils.getUserEmail(this);
        Log.e(TAG, "onCreate: email-> "+email );
        MedicationViewModel medicationViewModel = new MedicationViewModel(this.getApplication());
        medicationViewModel.fetchAllMedications(email)
                .observe(this, medications -> {
                    if(medications != null) {
                        Log.e(TAG, "onCreate: " + medications.size());
                        MedicationListAdapter adapter = new MedicationListAdapter(MedicationActivity.this, medications);
                        medicationsRecyclerView.setAdapter(adapter);
                        for (Medication mm: medications) {
                            Log.e(TAG, "onCreate: medication==> " + mm);
                        }
                    }
                });
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

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the   class reference for more information.
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
