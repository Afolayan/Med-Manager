package com.afolayan.med_manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.afolayan.med_manager.database.viewmodel.MedicationViewModel;
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

        MedicationViewModel medicationViewModel = new MedicationViewModel(this.getApplication());
        medicationViewModel.fetchAllMedications("")
                .observe(this, medications -> {
                    if(medications != null) {
                        Log.e(TAG, "onCreate: " + medications.size());
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
            Log.e(TAG, "handleSignInResult: account--> "+account.getEmail() );
            Log.e(TAG, "handleSignInResult: account--> "+account.getDisplayName() );
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the   class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
