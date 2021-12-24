package com.example.windowapp;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class VideoPlayer extends AppCompatActivity {

    ExoPlayer player;
    PlayerView playerView;
    TextView locationText;
    ArrayList<VideoModel> videoList;
    HashMap<String, String> videoLocationMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        player = new ExoPlayer.Builder(this).build();
        playerView = new PlayerView(this);
        playerView = (PlayerView) findViewById(R.id.videoView2);
        locationText = (TextView) findViewById(R.id.location_text);
        videoList = new ArrayList<>(Store.getInstance().getData());
        videoLocationMap = new HashMap<>();

        preparePlayer();
        preparePlayerView();
        setVideoLocations();

        playRandomVideo();

    }

    private void preparePlayer() {
        player.setMediaItems(getPlayList());
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.setShuffleModeEnabled(true);
    }

    private void preparePlayerView() {
        playerView.setPlayer(player);
        playerView.setShowFastForwardButton(false);
        playerView.setShowRewindButton(false);
        playerView.setShowNextButton(false);
        playerView.setShowPreviousButton(false);
    }

    public void playRandomVideo() {
        player.prepare();

        locationText.setText(videoLocationMap
                .get(Objects.requireNonNull(Objects
                        .requireNonNull(player
                                .getCurrentMediaItem())
                        .mediaMetadata.mediaUri)
                        .toString()));

        player.setPlayWhenReady(true);
    }

    public List<MediaItem> getPlayList() {
        List<MediaItem> mediaItems = new ArrayList<>();

        for (VideoModel video : videoList) {
            MediaMetadata.Builder mediaMetadataBuilder = new MediaMetadata.Builder();
            MediaMetadata mediaMetadata = mediaMetadataBuilder.
                    setMediaUri((Uri.parse(video.getUrl()))).build();

            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(Uri.parse(video.getUrl()))
                    .setMediaMetadata(mediaMetadata).build();

            mediaItems.add(mediaItem);
        }

        return mediaItems;
    }

    public void setVideoLocations() {
        for (VideoModel video : videoList) {
            videoLocationMap.put(video.getUrl(), video.getCity() + ", " + video.getCountry());
        }
    }

    public void onNextButtonClick(View view) {
        player.seekToNext();
    }

    public void onPrevButtonClick(View view) {
        player.seekToPrevious();
    }

    public void onMenuButtonClick(View view) {
        Intent intentMain = new Intent(this, MainActivity.class);
        player.release();
        startActivity(intentMain);
    }

}
