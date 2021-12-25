package com.example.windowapp;

import static java.util.stream.Collectors.toMap;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class MapsActivity extends BaseGoogleMapsActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ClusterManager<VideoModel> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<VideoModel> videos = Store.getInstance().getData();
        System.out.println("VIDEOS SIZE: "+videos.size());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setupMap(googleMap);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-26.167616, 28.079329), 10));

        mClusterManager = new ClusterManager<>(this, googleMap);
        mClusterManager.setRenderer(new RenderClusterInfoWindow(googleMap, mClusterManager));

        mClusterManager.setOnClusterClickListener(item -> {
            // Listen for clicks on a cluster here
            System.out.println("items: " + item.getItems());


            Intent intentVideoPlayer = new Intent(this, VideoPlayer.class);
            intentVideoPlayer.putStringArrayListExtra("ITEMS", getVideoURLs(item.getItems()));
            startActivity(intentVideoPlayer);
            return false;
        });

        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);
        addPersonItems();
        mClusterManager.cluster();
    }


    private ArrayList<String> getVideoURLs(Collection<VideoModel> items) {
        ArrayList<String> urlList = new ArrayList<>();

        for (VideoModel video : items) {
            urlList.add(video.getUrl());
        }

        return urlList;
    }

    private void addPersonItems() {
        ArrayList<VideoModel> videos = Store.getInstance().getData();

        for(int i = 0; i < videos.size(); i++) {
            videos.get(i).setmPosition();
            mClusterManager.addItem(videos.get(i));
        }
    }



    private class RenderClusterInfoWindow extends DefaultClusterRenderer<VideoModel> implements Serializable{

        public RenderClusterInfoWindow(GoogleMap map, ClusterManager<VideoModel> clusterManager) {
            super(getApplicationContext(), map, clusterManager);
            setMinClusterSize(1);
        }

        @Override
        protected void onClusterRendered(Cluster<VideoModel> cluster, Marker marker) {
            super.onClusterRendered(cluster, marker);
        }

        @Override
        protected void onBeforeClusterItemRendered(VideoModel item, MarkerOptions markerOptions) {
            markerOptions.title(item.getName());
            super.onBeforeClusterItemRendered(item, markerOptions);
        }

        @Override
        protected int getColor(int clusterSize) {
            return Color.parseColor("#12CA67"); // Return any color you want here. You can base it on clusterSize.
        }

    }
}