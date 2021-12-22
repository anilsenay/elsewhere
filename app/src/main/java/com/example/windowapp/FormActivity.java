package com.example.windowapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    QuerySnapshot countryList;
    QuerySnapshot cityList;
    String[] countries;
    String[] cities = {"First Select Country"};
    ArrayAdapter<String> countryAdapter;
    ArrayAdapter<String> cityAdapter;
    FirebaseFirestore db;
    Spinner citySpinner;
    String selectedCity, selectedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Spinner countrySpinner = findViewById(R.id.countrySpinner);
        countrySpinner.setOnItemSelectedListener(this);
        citySpinner = findViewById(R.id.citySpinner);
        citySpinner.setOnItemSelectedListener(this);

        countryAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner, R.id.spinnerText, new String[]{"Loading..."});
        countrySpinner.setAdapter(countryAdapter);

        db = FirebaseFirestore.getInstance();

        db.collection("Country")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            countryList = task.getResult();
                            int countrySize = task.getResult().size();
                            countries = new String[countrySize + 1];
                            countries[0] = "Select Country";
                            int i = 1;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                countries[i] = (String) document.getData().get("name");
                                i++;
                            }
                            countryAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner, R.id.spinnerText, countries);
                            countrySpinner.setAdapter(countryAdapter);
                        } else {
                            Log.w("Firebase Activity", "Error getting documents.", task.getException());
                        }
                    }
                });

        cityAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner, R.id.spinnerText, cities);
        citySpinner.setAdapter(cityAdapter);

        Uri selectedVideo = (Uri) getIntent().getExtras().get("SELECTED_VIDEO");
        System.out.println(selectedVideo);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if(parent.getAdapter().equals(countryAdapter)) {
            if(pos == 0) {
                cityAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner, R.id.spinnerText, new String[]{"First Select Country"});
                citySpinner.setAdapter(cityAdapter);
            } else {
                cityAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner, R.id.spinnerText, new String[]{"Loading..."});
                citySpinner.setAdapter(cityAdapter);
                selectedCountry = (String) parent.getItemAtPosition(pos);
                db.collection("City")
                    .whereEqualTo("country", parent.getItemAtPosition(pos))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                cityList = task.getResult();
                                int countrySize = task.getResult().size();
                                cities = new String[countrySize];
                                int i = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    cities[i] = (String) document.getData().get("name");
                                    i++;
                                }
                                if(cities.length == 0) {
                                    cities = new String[] {selectedCountry};
                                }
                                cityAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner, R.id.spinnerText, cities);
                                citySpinner.setAdapter(cityAdapter);
                            } else {
                                Log.w("Firebase Activity", "Error getting documents.", task.getException());
                            }
                        }
                    });
            }
        } else {
            String selected = (String) parent.getItemAtPosition(pos);
            if(selected != "First Select Country" || selected != "Loading...") {
                selectedCity = selected;
            }
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void onPressBack(View view) {
        finish();
    }

    public void onPublish(View view) {
        if(selectedCity.length() > 0 && selectedCountry.length() > 0) {
            // video upload
            // then get video info (url, aspectratio)
            // video.put("url", url);
            // video.put("aspect_ratio", aspect_ratio);

            Map<String, Object> video = new HashMap<>();
            video.put("city", selectedCity);
            video.put("country", selectedCountry);

            String latitude = "";
            String longitude = "";

            if (cityList.size() > 0) {
                for (QueryDocumentSnapshot document : cityList) {
                    if (document.getData().get("name") == selectedCity) {
                        latitude = (String) document.getData().get("latitude");
                        longitude = (String) document.getData().get("longitude");
                    }
                }
            }

            if (latitude.length() == 0 && longitude.length() == 0) {
                for (QueryDocumentSnapshot document : countryList) {
                    if (document.getData().get("name") == selectedCountry) {
                        latitude = (String) document.getData().get("latitude");
                        longitude = (String) document.getData().get("longitude");
                    }
                }
            }

            video.put("latitude", latitude);
            video.put("longitude", longitude);

            System.out.println("video: " + video);

            // POST TO FIREBASE
            db.collection("Video")
                    .add(video)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());
                            // REDIRECT TO SUCCESS PAGE
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error adding document", e);
                        }
                    });
        } else {
            // SHOW ERROR
        }
    }
}