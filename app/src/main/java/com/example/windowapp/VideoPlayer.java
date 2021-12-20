package com.example.windowapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;


public class VideoPlayer extends AppCompatActivity implements MediaPlayer.OnCompletionListener, Player.Listener {
    //private VideoView videoView;
    ExoPlayer player;
    PlayerView playerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        //playRandomVideo();

        player = new ExoPlayer.Builder(this).build();
        playerView = new PlayerView(this);
        playerView = (PlayerView) findViewById(R.id.videoView2);
        playRandomEP();

    }

    public void playRandomVideo() {
        VideoView videoView = (VideoView) findViewById(R.id.videoView2);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        setVideoFromURL(videoView);
        videoView.setMediaController(mediaController);
        videoView.setOnCompletionListener(this);
        videoView.start();

    }

    public void playRandomEP() {
        playerView.setPlayer(player);
        String uriPath = "https://firebasestorage.googleapis.com/v0/b/windowapp-335316.appspot.com/o/2519717394.mp4?alt=media";
        MediaItem mediaItem = MediaItem.fromUri(uriPath);
        player.setMediaItem(mediaItem);
        player.addListener(this);
        player.prepare();
        player.setPlayWhenReady(true);
    }

    public void setVideoFromURL(VideoView videoView) {
        //String uriPath = "https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1";
        String uriPath = "https://firebasestorage.googleapis.com/v0/b/windowapp-335316.appspot.com/o/2519717394.mp4?alt=media";
        Uri uri = Uri.parse(uriPath);
        videoView.setVideoURI(uri);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
    }

    private void returnToMainActivity() {
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
    }

    @Override
    public void onIsPlayingChanged(boolean isPlaying) {
        if (!isPlaying && player.getPlaybackState() == Player.STATE_ENDED){
            player.release();
            returnToMainActivity();
        }
    }

}
