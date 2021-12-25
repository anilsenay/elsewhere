package com.example.windowapp;

import java.util.ArrayList;

public class Store {
    ArrayList<VideoModel> s = new ArrayList<>();
    private static final Store ourInstance = new Store();
    public static Store getInstance() {
        return ourInstance;
    }
    private Store() {
    }
    public void setData(ArrayList<VideoModel> s) {
        this.s = s;
    }
    public ArrayList<VideoModel> getData() {
        return s;
    }
    public void addData(VideoModel s) {
        this.s.add(s);
    }

    public VideoModel getVideoFromUrl(String url) {
        for (VideoModel video: s) {
            if (url.equals(video.getUrl()))
                return video;
        }
        return null;
    }
}