package com.carko.carko;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class EventMapActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    private String TAG = "APYA - " + EventMapActivity.class.getSimpleName();
    private int PERMISSIONS_REQUEST_CODE = 111;

    private View customInfoWindow;
    private View container;
    private SlideView slideView;
    private Event event;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_map);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Enable up button
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        customInfoWindow = getLayoutInflater().inflate(R.layout.content_marker_info, null);
        container = findViewById(R.id.event_map_container);
        slideView = (SlideView) findViewById(R.id.slide_view);
        requestPermissions();

        // Get event
        Intent intent = getIntent();
        event = intent.getParcelableExtra(EventViewHolder.EXTRA_EVENT);
        Toast.makeText(this, event.getLabel(), Toast.LENGTH_SHORT).show();
    }

    private void loadMapAsync() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(event.getPos(), 14.0f));
        EventClient.getEventParkings(event, new EventClient.Complete<ArrayList<Parking>>() {
            @Override
            public void onComplete(ArrayList<Parking> response, String e) {
                if (e == null) {
                    Log.i(TAG, "getEventParkings: " + response.toString());
                    EventMapActivity.this.addParkings(response);
                } else {
                    Log.e(TAG, "getEventParkings: " + e);
                }
            }
        });
        mMap.addCircle(new CircleOptions()
            .center(event.getPos())
            .radius(event.getRange())
            .strokeColor(Color.TRANSPARENT)
            .fillColor(0x55ff0000));
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapClick(LatLng pos) {
        Log.i(TAG, "onMapClick");
        slideView.setVisibility(View.INVISIBLE);
        slideView.invalidate();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i(TAG, "onmarkerClick");
        slideView.setVisibility(View.VISIBLE);
        slideView.invalidate();
        return false;
    }

    private void addMarker(LatLng pos){
        final MarkerOptions marker = new MarkerOptions().position(pos);
        Log.i(TAG, "addMarker: " + pos.toString());
        mMap.addMarker(marker);
    }

    private void addParkings(ArrayList<Parking> parkings) {
        for (Parking parking : parkings) {
            addMarker(parking.getLatLng());
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
    }

    private void showLinkToSettingsSnackbar() {
        if (container == null) {
            return;
        }
        Snackbar.make(container,
                R.string.permission_denied_explanation,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Build intent that displays the App settings screen.
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",
                                BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).show();
    }

    private void showRequestPermissionsSnackbar() {
        if (container == null) {
            return;
        }
        Snackbar.make(container, R.string.permission_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ask_again, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Request permission.
                        ActivityCompat.requestPermissions(EventMapActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != PERMISSIONS_REQUEST_CODE) {
            return;
        }

        for (int i=0; i<permissions.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(permission)) {
                    Log.i(TAG, "Permission denied without 'NEVER ASK AGAIN': " + permission);
                    showRequestPermissionsSnackbar();
                } else {
                    Log.i(TAG, "Permission denied with 'NEVER ASK AGAIN': " + permission);
                    showLinkToSettingsSnackbar();
                }
            } else {
                Log.i(TAG, "Permission granted");
                loadMapAsync();
            }
        }
    }
}
