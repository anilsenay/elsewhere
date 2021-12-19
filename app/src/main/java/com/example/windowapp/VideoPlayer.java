package com.example.windowapp;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class VideoPlayer extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    //private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        playRandomVideo();

    }

    public void playRandomVideo() {
        VideoView videoView = (VideoView) findViewById(R.id.videoView2);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        //mediaController. media controller buttons will be set or hid 
        setVideoFromURL(videoView);
        videoView.setMediaController(mediaController);
        videoView.setOnCompletionListener(this);
        videoView.start();

    }

    public void setVideoFromURL(VideoView videoView) {
        //String uriPath = "https://www.youtube.com/watch?v=dE1P4zDhhqw";
        //String uriPath = "https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1";
        String uriPath = "https://firebasestorage.googleapis.com/v0/b/windowapp-335316.appspot.com/o/2519717394.mp4?alt=media";
        Uri uri = Uri.parse(uriPath);
        //mp.setDataSource(getApplicationContext(), uri);
        //videoView.setVideoPath("https://www.youtube.com/watch?v=dE1P4zDhhqw");
        videoView.setVideoURI(uri);

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);

    }
}
