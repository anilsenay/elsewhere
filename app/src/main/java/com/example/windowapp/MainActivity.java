package com.example.windowapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    AlertDialog alert;
    ArrayList<VideoModel> videoList;
    VideoView videoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        videoList = new ArrayList<>();

        db.collection("Video")
                .whereEqualTo("isActive", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                VideoModel video = document.toObject(VideoModel.class);
                                videoList.add(video);
                            }
                            System.out.println(">>SIZE: " + videoList.size());

                        } else {
                            Log.w("Firebase Activity", "Error getting documents.", task.getException());
                        }
                    }
                });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Select video resource")
                .setCancelable(false)
                .setPositiveButton("Select From Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);
                    }
                })
                .setNegativeButton("Record Video", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //checkPermission();
                        Intent takePicture = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        startActivityForResult(takePicture, 0);
                    }
                });
        alert = builder.create();


    }

    @Override
    protected void onResume() {
        super.onResume();
        playBackgroundVideo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoview.stopPlayback();
    }

    public void playBackgroundVideo() {
        videoview = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.background_video);
        videoview.setVideoURI(uri);
        videoview.start();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    public void goMapsActivity(View view) {
        if(videoList.size() == 0) return;
        Intent intent = new Intent(this, MapsActivity.class);
        Store store = Store.getInstance();
        store.setData(videoList);
        startActivity(intent);
    }

    public void onClickPublishButton(View view) {
        alert.show();
    }

    public void goPublishActivity(Uri selectedVideo) {
        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra("SELECTED_VIDEO", selectedVideo);
        startActivity(intent);
    }

    public void chooseVideo(View view) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 5);
    }

    // startActivityForResult is used to receive the result, which is the selected video.
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedVideo = imageReturnedIntent.getData();
                    System.out.println("0 " + selectedVideo);
                    goPublishActivity(selectedVideo);
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedVideo = imageReturnedIntent.getData();
                    System.out.println("1 " + selectedVideo);
                    goPublishActivity(selectedVideo);
                }
                break;
        }
    }

    public void randomVideo(View view) {
        if(videoList.size() == 0) return;
        Intent startVideoPlayer = new Intent(this, VideoPlayer.class);
        Store store = Store.getInstance();
        store.setData(videoList);
        startActivity(startVideoPlayer);
    }

    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        }
    }

}