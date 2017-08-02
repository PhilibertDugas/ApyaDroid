package com.carko.carko

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by fabrice on 2017-07-30.
 */
object AuthenticationHelper {
    fun customerLoggedIn(context: Context) {
        updateAuthToken(context, { error ->

        })
    }

    fun updateAuthToken(context : Context, complete: (Exception?) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.getIdToken(true)
                ?.addOnCompleteListener { task ->
                    AppState.cacheAuthToken(context, task.result.token)
                    complete(null)
                }
                ?.addOnFailureListener { exception ->
                    complete(exception)
                }
    }

}