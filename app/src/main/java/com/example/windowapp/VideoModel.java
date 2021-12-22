package com.example.windowapp;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class VideoModel implements ClusterItem {

    private LatLng mPosition;
    private String name;
    private String url;
    private String aspect_ratio;
    private String city;
    private String country;
    private double latitude;
    private double longitude;

    public VideoModel() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public VideoModel(double latitude, double longitude, String name, String url, String aspect_ratio, String city, String country) {
        this.mPosition = new LatLng(latitude, longitude);
        this.url = url;
        this.aspect_ratio = aspect_ratio;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
    public String getAspectRatio() {
        return aspect_ratio;
    }
    public String getCity() {
        return city;
    }
    public String getCountry() {
        return country;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public void setAspectRatio(String aspect_ratio) {
        this.aspect_ratio = aspect_ratio;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLongitudeL(double longitude) {
        this.longitude = longitude;
    }

    public void setmPosition() {
        this.mPosition = new LatLng(this.latitude, this.longitude);
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
