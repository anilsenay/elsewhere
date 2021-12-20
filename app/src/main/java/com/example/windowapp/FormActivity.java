package com.example.windowapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class FormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Uri selectedVideo = (Uri) getIntent().getExtras().get("SELECTED_VIDEO");
        System.out.println(selectedVideo);

        Spinner countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        countrySpinner.setOnItemSelectedListener(this);
        String[] countries = new String[]{"United Kingdom", "Turkey", "France"};
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, R.layout.spinner, R.id.spinnerText, countries);
        countrySpinner.setAdapter(countryAdapter);

        Spinner citySpinner = (Spinner) findViewById(R.id.citySpinner);
        citySpinner.setOnItemSelectedListener(this);
        String[] cities = new String[]{"London", "İstanbul", "Paris"};
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, R.layout.spinner, R.id.spinnerText, cities);
        citySpinner.setAdapter(cityAdapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void onPressBack(View view) {
        finish();
    }
}