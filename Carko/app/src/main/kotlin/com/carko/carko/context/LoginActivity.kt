package com.carko.carko.context

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.carko.carko.AuthenticationHelper
import com.carko.carko.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ResultCodes
import com.google.firebase.auth.FirebaseAuth
import java.util.Arrays


class LoginActivity : AppCompatActivity() {

    private val TAG = "APYA - LoginActivity"
    private val RC_SIGN_IN = 3235

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startLogin()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == ResultCodes.OK) {
                // Successfully signed in
                Log.i(TAG, "User signed in.")
                val user = FirebaseAuth.getInstance().currentUser
                AuthenticationHelper.ensureCustomerInBackend(user!!)
            }
        }
        setResult(resultCode, data)
        finish()
    }

    private fun startLogin() {
        val providers = Arrays.asList<AuthUI.IdpConfig>(
                AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
        )
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.LoginTheme)
                        .build(),
                RC_SIGN_IN)
    }

}
