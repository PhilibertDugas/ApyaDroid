package com.carko.carko.context

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.carko.carko.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_add_parking_map.*


class AddParkingMapActivity : AppCompatActivity(),
        OnMapReadyCallback {

    val TAG = "APYA - AddParking"
    val PLACE_AUTOCOMPLETE_REQUEST_CODE = 1111

    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_parking_map)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Toolbar
        toolbar.title = getString(R.string.title_activity_add_parking_map)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        button_next.setOnClickListener {
            Toast.makeText(this, "NEXT!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_parking_map, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                try {
                    val filter = AutocompleteFilter.Builder()
                            .setCountry("CA")
                            .build()
                    val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(filter)
                            .build(this)
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE)
                } catch (e: GooglePlayServicesRepairableException) {
                    Log.e(TAG, e.message, e)
                } catch (e: GooglePlayServicesNotAvailableException) {
                    Log.e(TAG, e.message, e)
                }
                return true
            }
            else -> {
                // Unrecognized action
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data)
                Log.i(TAG, "Place: " + place.name)
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 20.0f))
                center_marker.visibility = View.VISIBLE
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data)
                Log.e(TAG, status.statusMessage)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i(TAG, "User cancelled the search")
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val mtlPos = LatLng(45.5017, -73.5673)
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(mtlPos, 14.0f))
    }
}
