package com.example.windowapp;

public class Video {

    public String aspect_ratio;
    public String city;
    public String url;


    public Video() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Video(String aspect_ratio, String city, String url) {
        this.aspect_ratio = aspect_ratio;
        this.city = city;
        this.url = url;
    }

    public String getAspect_ratio() {
        return aspect_ratio;
    }

    public String getCity() {
        return city;
    }

    public String getUrl() {
        return url;
    }

}