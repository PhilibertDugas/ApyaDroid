package com.carko.carko

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

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
        layout.price.text = parking.price.toString()
        layout.reserve_button.isFocusable = true
        layout.reserve_button.isClickable = true
        layout.reserve_button.setOnClickListener {
            Toast.makeText(context, "Reserve now!", Toast.LENGTH_LONG).show()
        }

        if (parking.photoUrl != "") {
            Log.i(TAG, "Loading " + parking.photoUrl)
            val ref = FirebaseStorage.getInstance()
                    .getReferenceFromUrl(parking.photoUrl)
            Glide.with(this)
                    .using(FirebaseImageLoader())
                    .load(ref)
                    .bitmapTransform(CenterCrop(context), RoundedCornersTransformation(context, 16, 0))
                    .listener(FirebaseRequestListener(layout.photo1, layout.progress1))
                    .into(layout.photo1)
        } else {
            Log.i(TAG, "No available pictures for photo1")
        }

        if (parking.photoUrls.size > 0) {
            Log.i(TAG, "Loading " + parking.photoUrls[0])
            val ref = FirebaseStorage.getInstance()
                    .getReferenceFromUrl(parking.photoUrls[0])
            Glide.with(this)
                    .using(FirebaseImageLoader())
                    .load(ref)
                    .bitmapTransform(CenterCrop(context), RoundedCornersTransformation(context, 16, 0))
                    .listener(FirebaseRequestListener(layout.photo2, layout.progress2))
                    .into(layout.photo2)
        } else {
            Log.i(TAG, "No available pictures for photo2")
        }

        return layout
    }

    private class FirebaseRequestListener(private val imageView: ImageView,
                                          private val progressBar: ProgressBar):
            RequestListener<StorageReference, GlideDrawable> {
        override fun onException(e: Exception, model: StorageReference,
                                 target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
            Log.e(TAG, "Exception while loading image: ${e.message}")
            progressBar.visibility = View.GONE
            return true
        }

        override fun onResourceReady(resource: GlideDrawable, model: StorageReference,
                                     target: Target<GlideDrawable>, isFromMemoryCache: Boolean,
                                     isFirstResource: Boolean): Boolean {
            progressBar.visibility = View.GONE
            imageView.setImageDrawable(resource)
            return true
        }
    }
}