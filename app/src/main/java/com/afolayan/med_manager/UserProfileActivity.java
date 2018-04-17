package com.afolayan.med_manager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.afolayan.med_manager.database.viewmodel.UserViewModel;
import com.afolayan.med_manager.utils.AccountUtils;

import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {

    private OnFabInteraction fabInteraction;
    private FloatingActionButton fab;


    public void setFabInteraction(OnFabInteraction fabInteraction) {
        this.fabInteraction = fabInteraction;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> makeEditable());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void makeEditable() {
        findViewById(R.id.et_user_name).setEnabled(true);
        findViewById(R.id.et_user_age).setEnabled(true);
        findViewById(R.id.et_user_allergies).setEnabled(true);
        fabInteraction.onFabClicked();
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(v -> saveUserProfileEdit());
    }

    private void saveUserProfileEdit() {
        EditText etName = findViewById(R.id.et_user_name);
        EditText etAge = findViewById(R.id.et_user_age);
        EditText etAllergies = findViewById(R.id.et_user_allergies);

        UserViewModel viewModel = new UserViewModel(this);
        String userEmail = AccountUtils.getUserEmail(this);
        viewModel.fetchSingleUserByEmail(userEmail, user -> {
            if(user != null){
                String name = etName.getText().toString().trim();
                String ageString = etAge.getText().toString().trim();
                try {
                    int age = Integer.parseInt(ageString);
                    if(age != 0){
                        user.setAge(age);
                    }
                } catch (IllegalArgumentException e){
                    etAge.setError("Age must be whole number only");
                }
                String allergies = etAllergies.getText().toString().trim();
                if(!TextUtils.isEmpty(name)){
                    user.setName(name);
                }
                if(!TextUtils.isEmpty(allergies)){
                    user.setAllergies(allergies);
                }
                Log.e("UPA", "saveUserProfileEdit: user-> "+user);
                viewModel.updateSingleUser(user);


            }
        });
        makeUneditable(etName, etAge, etAllergies);
    }

    private void makeUneditable(EditText etName, EditText etAge, EditText etAllergies) {
        etName.setEnabled(false);
        etAge.setEnabled(false);
        etAllergies.setEnabled(false);
        fab.setOnClickListener(v->makeEditable());
        fab.setImageResource(R.drawable.ic_edit);
    }

    public interface OnFabInteraction{
        void onFabClicked();
    }
}
