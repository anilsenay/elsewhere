package com.example.windowapp;

import android.graphics.Color;
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

public class MapsActivity extends BaseGoogleMapsActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ClusterManager<VideoModel> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        for (int i = 0; i < 3; i++) {
            mClusterManager.addItem(new VideoModel(-26.187616, 28.079329, "PJ", "https://firebasestorage.googleapis.com/v0/b/windowapp-335316.appspot.com/o/2519717394.mp4?alt=media"));
            mClusterManager.addItem(new VideoModel(-26.207616, 28.079329, "PJ2", "https://firebasestorage.googleapis.com/v0/b/windowapp-335316.appspot.com/o/2519717394.mp4?alt=media"));
            mClusterManager.addItem(new VideoModel(-26.217616, 28.079329, "PJ3", "https://firebasestorage.googleapis.com/v0/b/windowapp-335316.appspot.com/o/2519717394.mp4?alt=media"));
        }
        mClusterManager.addItem(new VideoModel(-20.217616, 28.079329, "PJ3", "https://firebasestorage.googleapis.com/v0/b/windowapp-335316.appspot.com/o/2519717394.mp4?alt=media"));
        mClusterManager.addItem(new VideoModel(-20.217616, 28.079329, "PJ3", "https://firebasestorage.googleapis.com/v0/b/windowapp-335316.appspot.com/o/2519717394.mp4?alt=media"));

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