package com.afolayan.med_manager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> makeEditable());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void makeEditable() {
        findViewById(R.id.et_user_name).setEnabled(true);
        findViewById(R.id.et_user_age).setEnabled(true);
        findViewById(R.id.et_user_allergies).setEnabled(true);
    }

}
