package com.example.windowapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VideoView videoview = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.background_video);
        videoview.setVideoURI(uri);
        videoview.start();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Select From Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , 1);
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

    public void goMapsActivity(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void onClickPublishButton(View view) {
        alert.show();
    }

    public void goPublishActivity( Uri selectedVideo) {
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
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedVideo = imageReturnedIntent.getData();
                    System.out.println("0 "+selectedVideo);
                    goPublishActivity(selectedVideo);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedVideo = imageReturnedIntent.getData();
                    System.out.println("1 "+selectedVideo);
                    goPublishActivity(selectedVideo);
                }
                break;
        }
    }
}