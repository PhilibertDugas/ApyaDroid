package com.carko.carko

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult

/**
 * Created by fabrice on 2017-07-30.
 */
object AuthenticationHelper {
    fun customerLoggedIn() {
        updateAuthToken({ error ->

        })
    }

    fun updateAuthToken(complete: (Exception?) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.getIdToken(true)
                ?.addOnCompleteListener { task ->
                    AppState.cacheAuthToken(task.result.token)
                    complete(null)
                }
                ?.addOnFailureListener { exception ->
                    complete(exception)
                }
    }

}