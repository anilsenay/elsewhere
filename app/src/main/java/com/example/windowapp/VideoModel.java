package com.example.windowapp;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class VideoModel implements ClusterItem {

    private final LatLng mPosition;
    private String name;
    private String videoUrl;

    public VideoModel(double lat, double lng, String name, String videoUrl) {
        mPosition = new LatLng(lat, lng);
    }

    public String getName() {
        return name;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return "";
    }
}
