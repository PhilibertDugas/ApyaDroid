package com.carko.carko;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;

public class EventMapActivity extends AppCompatActivity
    implements OnMapReadyCallback {

    private String TAG = "Apya." + EventMapActivity.class.getSimpleName();

    private View customInfoWindow;
    private MapView mapView;
    private MapboxMap map;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure MapBox
        MapboxAccountManager.start(this, getString(R.string.mapbox_access_token));

        // Contains the mapView and needs to be called after MapBox has been configured
        setContentView(R.layout.activity_event_map);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Enable up button
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        // Initialize map
        customInfoWindow = getLayoutInflater().inflate(R.layout.content_marker_info, null);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Get event
        Intent intent = getIntent();
        event = intent.getParcelableExtra(EventViewHolder.EXTRA_EVENT);
        Toast.makeText(this, event.getLabel(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapReady(MapboxMap mMap) {
        map = mMap;
        map.setInfoWindowAdapter(new ParkingInfoWindowAdapter(customInfoWindow));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(event.getPos())
                .zoom(14)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);

        // Add event parkings
        EventClient.getEventParkings(event, new EventClient.Complete<ArrayList<Parking>>(){
            public void onComplete(ArrayList<Parking> response, String e){
                if (e == null) {
                    Log.i(TAG, "getEventParkings: " + response.toString());
                    EventMapActivity.this.addParkings(response);
                } else {
                    Log.e(TAG, "getEventParkings: " + e);
                }
            }
        });
    }

    private void addMarker(LatLng pos){
        final MarkerOptions marker = new MarkerOptions().position(pos);
        Log.i(TAG, "addMarker: " + pos.toString());
        map.addMarker(marker);
    }

    private void addParkings(ArrayList<Parking> parkings) {
        for (Parking parking : parkings) {
            addMarker(parking.getLatLng());
        }
    }
}
