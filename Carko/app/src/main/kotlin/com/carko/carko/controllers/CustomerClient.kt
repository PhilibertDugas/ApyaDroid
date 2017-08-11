package com.carko.carko.controllers

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.carko.carko.models.Customer
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by fabrice on 2017-08-06.
 */
object CustomerClient {
    val TAG = "APYA - CustomerClient"

    fun getCustomer(complete: (Customer?, Error?) -> Unit) {
        val baseUrl = ApiClient.getInstance().baseUrl
        val uriBuilder = baseUrl.buildUpon()
        uriBuilder.appendPath("customers")
                .appendPath(FirebaseAuth.getInstance().currentUser?.uid)
        val customerUrl = uriBuilder.build()
        Log.i(TAG, "get customer url: " + customerUrl.toString())
        val request = object: StringRequest(
                Request.Method.GET,
                customerUrl.toString(),
                { response ->
                    Log.i(TAG, "Response: " + response.toString())
                    var customer: Customer? = null
                    var error: Error? = null
                    if (response != null && response != "null") {  // FIXME Funny null response...
                        Log.i(TAG, "Got a customer, trying to initialize...")
                        try {
                            customer = Customer(JSONObject(response))
                        } catch (e: JSONException) {
                            error = Error(e.toString(), e)
                        }
                    }
                    complete(customer, error)
                }, { error ->
                    Log.e(TAG, "Error: " + error.toString())
                    error.printStackTrace()
                    complete(null, Error(String(error.networkResponse.data), error))
                }) {

            override fun getHeaders(): MutableMap<String, String> {
                return ApiClient.getInstance().authHeaders
            }
        }
        ApiClient.getInstance().addRequest(request)
    }

    fun postCustomer(customer: Customer.NewCustomer, complete: (Error?) -> Unit) {
        val baseUrl = ApiClient.getInstance().baseUrl
        val uriBuilder = baseUrl.buildUpon()
        uriBuilder.appendPath("customers")
        val customerUrl = uriBuilder.build()
        val body = JSONObject()
        body.put("customer", customer.toJson())
        Log.i(TAG, "post customer url: " + customerUrl.toString())
        val request = object: JsonObjectRequest(
                Request.Method.POST,
                customerUrl.toString(),
                body, { response ->
                    Log.i(TAG, "postCustomer() - response: %s".format(response))
                    complete(null)
                }, { error ->
                    Log.e(TAG, "postCustomer() - error: %s".format(error.toString()))
                    complete(Error(String(error.networkResponse.data)))
                }) {

            override fun getHeaders(): MutableMap<String, String> {
                return ApiClient.getInstance().authHeaders
            }
        }
        ApiClient.getInstance().addRequest(request)
    }
}