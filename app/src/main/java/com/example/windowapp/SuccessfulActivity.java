package com.example.windowapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class SuccessfulActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful);
    }

    public void onGoBackButtonClick(View view) {
        finish();
//        Intent intentMain = new Intent(this, MainActivity.class);
//        startActivity(intentMain);
    }
}