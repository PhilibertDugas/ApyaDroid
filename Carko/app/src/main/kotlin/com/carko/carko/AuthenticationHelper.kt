package com.carko.carko

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.carko.carko.context.LoginActivity
import com.carko.carko.models.Customer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.collections.ArrayList

/**
 * Created by fabrice on 2017-07-30.
 */
object AuthenticationHelper {
    val TAG = "APYA - AuthHelper"
    val RC_SIGN_IN = 3235

    val authStateListeners = ArrayList<(customer: Customer?) -> Unit>()

    fun addAuthStateListener(listener: (customer: Customer?) -> Unit) {
        authStateListeners.add(listener)
        listener(getCustomer())
    }

    fun removeAuthStateListener(listener: (customer: Customer?) -> Unit) {
        var i = 0
        var found = false
        while (!found && i < authStateListeners.size) {
            val l = authStateListeners[i]
            if (l == listener) {
                Log.i(TAG, "removeAuthStateListener() - removing listener")
                authStateListeners.removeAt(i)
                found = true
            }
            i++
        }
        if (!found) {
            Log.e(TAG, "removeAuthStateListener() - listener not found!")
        }
    }

    private fun notifyListeners(customer: Customer?) {
        authStateListeners.forEach { l -> l(customer) }
    }

    fun login(activity: Activity) {
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivityForResult(intent, RC_SIGN_IN)
    }

    fun logout() {

    }

    fun customerLoggedIn(customer: Customer) {
        updateAuthToken({ error ->
            if (error != null) {
                Log.e(TAG, error.toString())
                return@updateAuthToken
            }
            AppState.cacheCustomer(customer)
            Log.i(TAG, "Customer logged in!")
            notifyListeners(customer)
        })
    }

    fun customerAvailable(): Boolean {
        val customer = AppState.cachedCustomer()
        val authToken = AppState.cachedToken()
        return FirebaseAuth.getInstance().currentUser != null ||
                customer != null ||
                authToken != null
    }

    // This should only be called if customerAvailable() returns true
    fun getCustomer(): Customer? {
        return AppState.cachedCustomer()
    }

    fun updateCustomer(customer: Customer) {
        AppState.cacheCustomer(customer)
    }

    fun resetCustomer() {
        try {
            // TODO Do FB too
            FirebaseAuth.getInstance().signOut()
            // FBSDKAccessToken.setCurrent(nil)
            AppState.resetCustomer()
        } catch (exception: Exception) {
            Log.e(TAG, exception.toString())
        }
    }

    // This should only be called is customerAvailable() returns true
    fun getAuthToken(): String? {
        return AppState.cachedToken()
    }

    fun updateAuthToken(complete: (Error?) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.getIdToken(true)
                ?.addOnCompleteListener { task ->
                    Log.i(TAG, "Retrieved id token")
                    AppState.cacheAuthToken(task.result.token)
                    complete(null)
                }
                ?.addOnFailureListener { exception ->
                    Log.e(TAG, "Failed to get firebase id token")
                    complete(Error(exception))
                }
    }

    fun ensureCustomerInBackend(user: FirebaseUser) {
        Log.i(TAG, "Ensuring customer in backed!")
        Customer.getCustomer { customer, error ->
            if (error != null) {
                Log.e(TAG, error.toString())
            } else if (customer != null) {
                AuthenticationHelper.customerLoggedIn(customer)
            } else {
                // New customer flow
                Log.i(TAG, "New user! (%s, %s %s)".format(user.email, user.displayName, user.uid))
                val newCustomer = Customer.NewCustomer(user.email, user.displayName, user.uid)
                newCustomer.register({ new_error ->
                    if (new_error != null) {
                        Log.e(TAG, new_error.message)
                        FirebaseAuth.getInstance().signOut()
                    } else {
                        this.initCustomer()
                    }
                })
            }
        }
    }

    private fun initCustomer() {
        Customer.getCustomer { customer, error ->
            if (error != null) {
                Log.e(TAG, error.toString())
            } else if (customer != null){
                AuthenticationHelper.customerLoggedIn(customer)
            }
        }
    }
}