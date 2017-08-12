package com.carko.carko;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.carko.carko.models.Customer;
import com.carko.carko.models.Parking;
import com.carko.carko.views.SlideView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        NavigationView.OnNavigationItemSelectedListener{

    private final String TAG = "APYA - " + MapActivity.class.getSimpleName();
    private final int PERMISSIONS_REQUEST_CODE = 111;
    private final int COLOR_CIRCLE_INACTIVE = 0x33ff0000;
    private final int COLOR_CIRCLE_ACTIVE = 0x66ff0000;
    private final int MARKER_DRAWABLE = R.drawable.marker_68;

    private View container;
    private SlideView slideView;
    private NavigationView navigationView;
//    private Event event;

    private GoogleMap mMap;
//    private Circle eventRadius;
    private Marker activeMarker;

    private Function1<Customer, Unit> mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Drawer
        DrawerLayout drawer = findViewById(R.id.map_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation view
        // TODO create custom layout for
        navigationView = findViewById(R.id.map_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        // Enable up button
//        ActionBar actionbar = getSupportActionBar();
//        actionbar.setDisplayHomeAsUpEnabled(true);

        container = findViewById(R.id.map_container);
        slideView = findViewById(R.id.slide_view);

        // Get event
//        Intent intent = getIntent();
//        event = intent.getParcelableExtra(EventViewHolder.EXTRA_EVENT);
//        Toast.makeText(this, event.getLabel(), Toast.LENGTH_SHORT).show();

        mAuthListener = new Function1<Customer, Unit>() {
            @Override
            public Unit invoke(Customer customer) {
                Log.i(TAG, "listener - Listener notified in MapActivity!");
                if (customer != null) {
                    Log.i(TAG, "mAuthListener - Customer logged in from MapActivity!");
                    View header = navigationView.getHeaderView(0);
                    TextView displayName = header.findViewById(R.id.display_name);
                    displayName.setText(customer.getDisplayName());
                } else {
                    Log.i(TAG, "mAuthListener - Customer logged out from MapActivity!");
                    View header = navigationView.getHeaderView(0);
                    TextView displayName = header.findViewById(R.id.display_name);
                    displayName.setText("Android Studio");
                }
                return null;
            }
        };

        loadMapAsync();
    }

    @Override
    public void onPause() {
        super.onPause();
        AuthenticationHelper.INSTANCE.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        AuthenticationHelper.INSTANCE.addAuthStateListener(mAuthListener);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (AuthenticationHelper.INSTANCE.customerAvailable()) {
            Toast.makeText(this, "User already logged in!", Toast.LENGTH_SHORT).show();
            Customer customer = AuthenticationHelper.INSTANCE.getCustomer();
            if (customer != null) {
                Log.i(TAG, customer.toJson().toString());
            } else {
                Log.e(TAG, "Customer not properly saved!");
            }
        } else {
            AuthenticationHelper.INSTANCE.login(this);
            return true;
        }

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.item_payment) {
            // open stripe view
            // philibert
        } else if (id == R.id.item_vehicule) {
            // open custom activity
            // mario
        } else if (id == R.id.item_history) {
            // open custom activity
            //
        } else if (id == R.id.item_help) {
            // open mail
            //
        } else if (id == R.id.item_rent) {
            // open custom activity
            // rough
        } else if (id == R.id.item_payout) {
            // open custom activity
        }

        DrawerLayout drawer = findViewById(R.id.event_grid_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadMapAsync() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "onMapReady");
        mMap = googleMap;
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(event.getPos()));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(event.getPos(), 14.0f));
//        EventClient.getEventParkings(event, new EventClient.Complete<ArrayList<Parking>>() {
//            @Override
//            public void onComplete(ArrayList<Parking> response, String e) {
//                if (e == null) {
//                    Log.i(TAG, "getEventParkings: " + response.toString());
//                    MapActivity.this.addParkings(response);
//                } else {
//                    Log.e(TAG, "getEventParkings: " + e);
//                }
//            }
//        });
//        eventRadius = mMap.addCircle(new CircleOptions()
//            .center(event.getPos())
//            .radius(event.getRange())
//            .strokeColor(Color.TRANSPARENT)
//            .fillColor(COLOR_CIRCLE_INACTIVE));
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapClick(LatLng pos) {
        Log.i(TAG, "onMapClick");
        slideView.setVisibility(View.INVISIBLE);
        slideView.invalidate();
//        eventRadius.setFillColor(COLOR_CIRCLE_INACTIVE);
        resetActiveMarker();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i(TAG, "onMarkerClick");
        Parking parking = (Parking) marker.getTag();
        setActiveMarker(marker);

        slideView.setVisibility(View.VISIBLE);
        slideView.invalidate();

//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        ParkingDescFragment fragment = new ParkingDescFragment();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(ParkingDescFragment.Companion.getEVENT_KEY(), event);
//        bundle.putParcelable(ParkingDescFragment.Companion.getPARKING_KEY(), parking);
//        fragment.setArguments(bundle);
//        fragmentTransaction.replace(R.id.card_view, fragment);
//        fragmentTransaction.commit();

//        eventRadius.setFillColor(COLOR_CIRCLE_ACTIVE);

        return false;
    }

    private void addParkings(ArrayList<Parking> parkings) {
        for (Parking parking : parkings) {
            LatLng pos = parking.getLatLng();
            Log.i(TAG, "addMarker: " + pos.toString());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(pos)
                    .icon(BitmapDescriptorFactory.fromResource(MARKER_DRAWABLE));
            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(parking);
        }
    }

    private void setActiveMarker(Marker marker) {
        if (activeMarker != null) {
            // Reset last active marker
            activeMarker.setIcon(BitmapDescriptorFactory.fromResource(MARKER_DRAWABLE));
        }
        activeMarker = marker;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), MARKER_DRAWABLE);
        int width = bitmap.getWidth() + 20;
        int height = bitmap.getHeight() + 20;
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));
    }

    private void resetActiveMarker() {
        if (activeMarker != null) {
            // Reset last active marker
            activeMarker.setIcon(BitmapDescriptorFactory.fromResource(MARKER_DRAWABLE));
        }
        activeMarker = null;
    }

    /* FIXME Permissions are not required right now but might be later */
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
                        ActivityCompat.requestPermissions(MapActivity.this,
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
