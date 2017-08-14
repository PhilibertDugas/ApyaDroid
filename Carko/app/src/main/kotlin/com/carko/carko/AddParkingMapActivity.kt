package com.carko.carko

import android.app.Activity
import android.support.v4.app.FragmentActivity
import android.os.Bundle
import android.support.v4.app.SupportActivity
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_add_parking_map.*


class AddParkingMapActivity : AppCompatActivity(),
        OnMapReadyCallback,
        PlaceSelectionListener {

    val TAG = "APYA - AddParking"

    private var mMap: GoogleMap? = null
    private var autocompleteFragment: PlaceAutocompleteFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_parking_map)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Toolbar
        toolbar.title = getString(R.string.add_parking_header)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        autocompleteFragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment

        button_next.setOnClickListener {
            Toast.makeText(this, "NEXT!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val mtlPos = LatLng(45.5017, -73.5673)
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(mtlPos, 14.0f))
        autocompleteFragment?.setOnPlaceSelectedListener(this)
    }

    override fun onPlaceSelected(place: Place) {
        Log.i(TAG, "Place: " + place.name)
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 20.0f))
        center_marker.visibility = View.VISIBLE
    }

    // Error when selecting place
    override fun onError(status: Status) {
        Log.i(TAG, "An error occurred: " + status)
    }
}
