package com.carko.carko

import android.app.Fragment
import android.content.Intent
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
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.ResultCodes
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import kotlinx.android.synthetic.main.fragment_parking_desc.view.*
import java.util.*

class ParkingDescFragment: Fragment() {
    companion object {
        val TAG = "APYA - " + ParkingDescFragment::class.java.getSimpleName()
        val PARKING_KEY = "com.carko.carko.PARKING_KEY"
        val EVENT_KEY = "com.carko.carko.EVENT_KEY"
        val RC_SIGN_IN = 3235
        val RC_DIALOG = 3236
    }

    lateinit var event: Event

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                          savedInstanceState: Bundle?): View {
        val layout = inflater.inflate(R.layout.fragment_parking_desc, container, false)
        val parking = arguments.getParcelable<Parking>(PARKING_KEY)

        event = arguments.getParcelable<Event>(EVENT_KEY)

        layout.event_name.text = event.label
        layout.address.text = parking.address
        layout.description.text = parking.description
        layout.price.text = parking.price.toString()
        layout.reserve_button.setOnClickListener {
            Toast.makeText(context, "Reserve now!", Toast.LENGTH_LONG).show()
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                startReservation()
            } else {
                startLogin()
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            onLoginResult(resultCode, data)
        } else if (requestCode == RC_DIALOG) {
            onReservationResult(resultCode, data)
        }
    }

    private fun onLoginResult(resultCode: Int, data: Intent?) {
        val response = IdpResponse.fromResultIntent(data)

        // Successfully signed in
        if (resultCode == ResultCodes.OK) {
            startReservation()
            return
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                Toast.makeText(activity, "Sign in cancelled!", Toast.LENGTH_SHORT).show()
                return
            }

            if (response.errorCode == ErrorCodes.NO_NETWORK) {
                Toast.makeText(activity, "No network!", Toast.LENGTH_SHORT).show()
                return
            }

            if (response.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                Toast.makeText(activity, "Unknown error!", Toast.LENGTH_SHORT).show()
                return
            }
        }

        Toast.makeText(activity, "Unknown response!", Toast.LENGTH_SHORT).show()
    }

    private fun onReservationResult(resultCode: Int, data: Intent?) {
        if (resultCode == ResultCodes.OK) {
            Toast.makeText(activity, "Positive click!", Toast.LENGTH_SHORT).show()
        } else if (resultCode == ResultCodes.CANCELED) {
            Toast.makeText(activity, "Cancelled!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startReservation(){
        val intent = Intent(activity, ReservationDialog::class.java)
        intent.putExtra(ReservationDialog.EVENT_PRICE_KEY, event.price)
        startActivityForResult(intent, RC_DIALOG)
    }

    private fun startLogin(){
        val providers = Arrays.asList<IdpConfig>(
                IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
        )
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.LoginTheme)
                        .build(),
                RC_SIGN_IN)
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