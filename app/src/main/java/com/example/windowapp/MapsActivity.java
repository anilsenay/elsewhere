package com.example.windowapp;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;

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
            return false;
        });

        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);
        addPersonItems();
        mClusterManager.cluster();
    }

    private void addPersonItems() {
        ArrayList<VideoModel> videos = Store.getInstance().getData();

        for(int i = 0; i < videos.size(); i++) {
            videos.get(i).setmPosition();
            mClusterManager.addItem(videos.get(i));
        }
    }

    private class RenderClusterInfoWindow extends DefaultClusterRenderer<VideoModel> {

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