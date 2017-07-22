package com.carko.carko

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.FirebaseStorage

import kotlinx.android.synthetic.main.fragment_parking_desc.view.*

class ParkingDescFragment: Fragment() {
    companion object {
        val TAG = "APYA - " + ParkingDescFragment::class.java.getSimpleName()
        val PARKING_KEY = "com.carko.carko.PARKING_KEY"
        val EVENT_KEY = "com.carko.carko.EVENT_KEY"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                          savedInstanceState: Bundle?): View {
        val layout = inflater.inflate(R.layout.fragment_parking_desc, container, false)
        val event = arguments.getParcelable<Event>(EVENT_KEY)
        val parking = arguments.getParcelable<Parking>(PARKING_KEY)

        layout.event_name.text = event.label
        layout.address.text = parking.address
        layout.description.text = parking.description
        val reserveText = getString(R.string.reserve_now) + "\n" + parking.price.toString()
        layout.reserve_button.text = reserveText

        // Load image from Firebase
        if (parking.photoUrl != "") {
            Log.i(TAG, "Loading " + parking.photoUrl)
            val ref = FirebaseStorage.getInstance()
                    .getReferenceFromUrl(parking.photoUrl)
            Glide.with(this.context)
                    .using(FirebaseImageLoader())
                    .load(ref)
                    .into(layout.photo1)
        } else {
            Log.i(TAG, "No available pictures for photo1")
        }

        if (parking.photoUrls.size > 0) {
            Log.i(TAG, "Loading " + parking.photoUrls[0])
            val ref = FirebaseStorage.getInstance()
                    .getReferenceFromUrl(parking.photoUrls[0])
            Glide.with(this.context)
                    .using(FirebaseImageLoader())
                    .load(ref)
                    .into(layout.photo2)
        } else {
            Log.i(TAG, "No available pictures for photo2")
        }

        return layout
    }
}