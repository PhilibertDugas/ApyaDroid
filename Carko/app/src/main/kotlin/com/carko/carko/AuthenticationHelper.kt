package com.carko.carko

import android.content.Context
import android.util.Log
import com.carko.carko.models.Customer
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.User
import com.google.firebase.auth.FirebaseAuth
import java.util.*

/**
 * Created by fabrice on 2017-07-30.
 */
object AuthenticationHelper {
    val TAG = "APYA - AuthHelper"

    fun customerLoggedIn(context: Context, customer: Customer) {
        updateAuthToken(context, { error ->
            AppState.cacheCustomer(context, customer)
            // TODO Notify observers
        })
    }

    fun customerAvailable(context: Context): Boolean {
        val customer = AppState.cachedCustomer(context)
        val authToken = AppState.cachedToken(context)
        return FirebaseAuth.getInstance().currentUser == null ||
                customer == null ||
                authToken == null
    }

    // This should only be called if customerAvailable() returns true
    fun getCustomer(context: Context): Customer? {
        return AppState.cachedCustomer(context)
    }

    fun updateCustomer(context: Context, customer: Customer) {
        AppState.cacheCustomer(context, customer)
    }

    // This should only be called is customerAvailable() returns true
    fun getAuthToken(context: Context): String? {
        return AppState.cachedToken(context)
    }

    fun resetCustomer(context: Context) {
        try {
            // TODO Do FB too
            FirebaseAuth.getInstance().signOut()
            // FBSDKAccessToken.setCurrent(nil)
            AppState.resetCustomer(context)
        } catch (exception: Exception) {

        }
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

    fun ensureCustomerInBackend(context: Context, user: User) {
        Customer.getCustomer { customer, error ->
            if (error != null) {
                Log.e(TAG, error)
            } else if (customer != null) {
                AuthenticationHelper.customerLoggedIn(context, customer)
            } else {
                // New customer flow
                val newCustomer = Customer.NewCustomer(user.email, user.name, user.providerId)
                newCustomer.register({ error ->
                    if (error != null) {
                        Log.e(TAG, error.toString())
                        FirebaseAuth.getInstance().signOut()
                    } else {
                        this.initCustomer(context)
                    }
                })
            }
        }
    }

    private fun initCustomer(context: Context) {
        Customer.getCustomer { customer, error ->
            if (error != null) {
                Log.e(TAG, error.toString())
            } else if (customer != null){
                AuthenticationHelper.customerLoggedIn(context, customer)
            }
        }
    }
}