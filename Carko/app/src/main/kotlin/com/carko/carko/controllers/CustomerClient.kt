package com.carko.carko.controllers

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.carko.carko.models.Customer
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by fabrice on 2017-08-06.
 */
object CustomerClient {
    val TAG = "APYA - CustomerClient"

    fun getCustomer(complete: (Customer?, String?) -> Unit) {
        val baseUrl = ApiClient.getInstance().baseUrl
        val uriBuilder = baseUrl.buildUpon()
        uriBuilder.appendPath("customers")
                .appendPath(FirebaseAuth.getInstance().currentUser?.uid)
        val customerUrl = uriBuilder.build()
        Log.i(TAG, "get customer url: " + customerUrl.toString())
        val request = JsonObjectRequest(
                Request.Method.GET,
                customerUrl.toString(),
                null, { response ->
            Log.i(TAG, response.toString())
            var error: String? = null
            var customer: Customer? = null
            try {
                customer = Customer(response)
            } catch (e: JSONException) {
                error = e.toString()
            }
            complete(customer, error)
        }, { error ->
            Log.e(TAG, "Error: " + error.toString())
            complete(null, error.toString())
        })
        ApiClient.getInstance().addRequest(request)
    }

    fun postCustomer(customer: Customer.NewCustomer, complete: (String?) -> Unit) {
        val baseUrl = ApiClient.getInstance().baseUrl
        val uriBuilder = baseUrl.buildUpon()
        uriBuilder.appendPath("customers")
        val customerUrl = uriBuilder.build()
        val parameters = JSONObject()
        parameters.put("customer", customer.toJson())
        Log.i(TAG, "post customer url: " + customerUrl.toString())
        val request = JsonObjectRequest(
                Request.Method.POST,
                customerUrl.toString(),
                parameters, { response ->
            complete(null)
        }, { error ->
            Log.e(TAG, error.toString())
            complete(error.toString())
        })
        ApiClient.getInstance().addRequest(request)
    }
}