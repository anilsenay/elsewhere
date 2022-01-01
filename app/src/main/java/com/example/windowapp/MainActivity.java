package com.example.windowapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;

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


                        } else {
                            Log.w("Firebase Activity", "Error getting documents.", task.getException());
                        }
                    }
                });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Select video resource")
                .setCancelable(true)
                .setPositiveButton("Select From Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);

                    }
                })
                .setNegativeButton("Record Video", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent takePicture = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        startActivityForResult(takePicture, 0);

                    }
                });
        alert = builder.create();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (isAllPermissionsGranted(grantResults)) {
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this,
                        "Permissions denied, all permissions are required to continue",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isAllPermissionsGranted(int[] grantResults) {

        int allPermissionSize = grantResults.length;
        int grantedPermissionsSize = 0;

        for (int permissionResult : grantResults) {
            if (permissionResult == PackageManager.PERMISSION_GRANTED)
                grantedPermissionsSize++;
        }
        return allPermissionSize == grantedPermissionsSize;
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
        if (videoList.size() == 0) return;
        Intent intent = new Intent(this, MapsActivity.class);
        Store store = Store.getInstance();
        store.setData(videoList);
        startActivity(intent);
    }

    public void onClickPublishButton(View view) {

        ArrayList<String> deniedPermissions = getDeniedPermissions();

        if (!deniedPermissions.isEmpty())
            requestPermissions((String[]) deniedPermissions.toArray(new String[deniedPermissions.size()]),
                    MY_CAMERA_PERMISSION_CODE);
        else {
            alert.show();
        }
    }

    public ArrayList<String> getDeniedPermissions() {
        ArrayList<String> deniedPermissions = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            deniedPermissions.add(Manifest.permission.CAMERA);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED)
            deniedPermissions.add(Manifest.permission.RECORD_AUDIO);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED)
            deniedPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED)
            deniedPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return deniedPermissions;
    }

    public void goPublishActivity(Uri selectedVideo) {
        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra("SELECTED_VIDEO", selectedVideo);
        startActivity(intent);
    }

    // startActivityForResult is used to receive the result, which is the selected video.
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode == RESULT_OK) {
            Uri selectedVideo = imageReturnedIntent.getData();
            goPublishActivity(selectedVideo);
        }
    }

    public void randomVideo(View view) {
        if (videoList.size() == 0) return;
        Intent startVideoPlayer = new Intent(this, VideoPlayer.class);
        Store store = Store.getInstance();
        store.setData(videoList);
        startActivity(startVideoPlayer);
    }

}