package com.example.windowapp;

import android.util.Log;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.os.Bundle;

import java.util.ArrayList;

public class FirebaseActivity extends AppCompatActivity {
    private static final String TAG = "Firebase Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<Video> videoList = new ArrayList<>();

        db.collection("Video")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Video video = document.toObject(Video.class);
                                videoList.add(video);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });

    }

}