package com.example.windowapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class VideoPlayer extends AppCompatActivity {

    ExoPlayer player;
    PlayerView playerView;
    TextView locationText;
    ArrayList<VideoModel> videoList;
    ArrayList<String> videoUrlList;
    HashMap<String, String> videoLocationMap;
    boolean isComingFromIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        player = new ExoPlayer.Builder(this).build();
        playerView = new PlayerView(this);
        playerView = findViewById(R.id.videoView2);
        locationText = findViewById(R.id.location_text);
        isComingFromIntent = getIntent().hasExtra("ITEMS");
        prepareVideoList();
        videoLocationMap = new HashMap<>();

        preparePlayer();
        preparePlayerView();
        setVideoLocations();

        playRandomVideo();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }

    public void prepareVideoList() {
        if (isComingFromIntent)
            videoUrlList = new ArrayList<>(getIntent().getStringArrayListExtra("ITEMS"));
        else
            videoList = new ArrayList<>(Store.getInstance().getData());
    }

    private void preparePlayer() {

        if (isComingFromIntent)
            player.setMediaItems(getPlaylistFromMap());
        else
            player.setMediaItems(getPlaylist());

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

    public List<MediaItem> getPlaylist() {
        List<MediaItem> mediaItems = new ArrayList<>();

        for (VideoModel video : videoList) {
            MediaMetadata.Builder mediaMetadataBuilder = new MediaMetadata.Builder();
            MediaMetadata mediaMetadata = mediaMetadataBuilder
                    .setMediaUri((Uri.parse(video.getUrl())))
                    .build();

            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(Uri.parse(video.getUrl()))
                    .setMediaMetadata(mediaMetadata)
                    .build();

            mediaItems.add(mediaItem);
        }

        return mediaItems;
    }

    public List<MediaItem> getPlaylistFromMap() {
        List<MediaItem> mediaItems = new ArrayList<>();

        for (String videoUrl : videoUrlList) {
            MediaMetadata.Builder mediaMetadataBuilder = new MediaMetadata.Builder();
            MediaMetadata mediaMetadata = mediaMetadataBuilder
                    .setMediaUri((Uri.parse(videoUrl)))
                    .build();

            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(Uri.parse(videoUrl))
                    .setMediaMetadata(mediaMetadata)
                    .build();

            mediaItems.add(mediaItem);
        }

        return mediaItems;
    }

    public void setVideoLocations() {

        if (isComingFromIntent) {

            for (String videoUrl : videoUrlList) {
                VideoModel video = Store.getInstance().getVideoFromUrl(videoUrl);
                videoLocationMap.put(videoUrl, video.getCity() + ", " + video.getCountry());
            }

        } else {

            for (VideoModel video : videoList) {
                videoLocationMap.put(video.getUrl(), video.getCity() + ", " + video.getCountry());
            }
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
