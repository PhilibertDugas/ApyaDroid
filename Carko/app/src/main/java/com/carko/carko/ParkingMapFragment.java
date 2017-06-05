package com.carko.carko;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParkingMapFragment extends Fragment
        implements OnMapReadyCallback {

    private View fragmentLayout;
    private View customInfoWindow;
    private MapView mapView;
    private MapboxMap map;

    public ParkingMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MapboxAccountManager.start(getActivity(), getString(R.string.mapbox_access_token));

        fragmentLayout = inflater.inflate(R.layout.fragment_parking_map, container, false);
        customInfoWindow = inflater.inflate(R.layout.content_marker_info, null);

        mapView = (MapView) fragmentLayout.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

        // Inflate the layout for this fragment
        return fragmentLayout;
    }

    @Override
    public void onMapReady(MapboxMap mMap) {
        map = mMap;
        map.setInfoWindowAdapter(new ParkingInfoWindowAdapter(customInfoWindow));
    }

    public void addMarker(LatLng pos){
        final MarkerOptions marker = new MarkerOptions().position(pos);
        Log.d("addMarker", pos.toString());
        map.addMarker(marker);
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

}
