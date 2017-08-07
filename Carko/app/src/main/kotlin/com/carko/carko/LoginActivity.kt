package com.carko.carko

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.ResultCodes
import java.util.Arrays


class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 3235

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startLogin()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show()
                return
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, "Sign in cancelled!", Toast.LENGTH_SHORT).show()
                    return
                }

                if (response.errorCode == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No network!", Toast.LENGTH_SHORT).show()
                    return
                }

                if (response.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Unknown error!", Toast.LENGTH_SHORT).show()
                    return
                }
            }

            Toast.makeText(this, "Unknown response!", Toast.LENGTH_SHORT).show()
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
