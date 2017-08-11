package com.carko.carko.controllers

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.carko.carko.AuthenticationHelper
import com.carko.carko.models.Vehicule
import org.json.JSONException

/**
 * Created by fabrice on 2017-08-10.
 */
object VehiculeClient {
    val TAG = "APYA - VehiculeClient"

    fun postVehicule(vehicule: Vehicule, complete: (Vehicule?, Error?) -> Unit) {
        if (!AuthenticationHelper.customerAvailable()) {
            Log.e(TAG, "Customer is not available!")
            return
        }
        val baseUrl = ApiClient.getInstance().baseUrl
        val uriBuilder = baseUrl.buildUpon()
        uriBuilder.appendPath("customers")
                .appendPath(AuthenticationHelper.getCustomer()?.id.toString())
                .appendPath("vehicules")
        val vehiculeUrl = uriBuilder.build()
        val body = vehicule.toJson()
        Log.i(TAG, "postVehicule() - url: %s".format(vehiculeUrl.toString()))
        val request = object: JsonObjectRequest(
                Request.Method.POST,
                vehiculeUrl.toString(),
                body,
                { response ->
                    Log.i(this@VehiculeClient.TAG, "Response: " + response.toString())
                    var result: Vehicule? = null
                    var error: Error? = null
                    try {
                        result = Vehicule(response)
                    } catch (e: JSONException) {
                        error = Error(e.toString(), e)
                    }
                    complete(result, error)
                }, { error ->
                    Log.e(this@VehiculeClient.TAG, "Error: " + error.toString())
                    error.printStackTrace()
                    complete(null, Error(error.toString(), error))
                }) {

            override fun getHeaders(): MutableMap<String, String> {
                return ApiClient.getInstance().authHeaders
            }
        }
        ApiClient.getInstance().addRequest(request)
    }

    fun updateVehicule(vehicule: Vehicule, complete: (Error?) -> Unit) {
        if (!AuthenticationHelper.customerAvailable()) {
            Log.e(TAG, "Customer is not available!")
            return
        }

        if (vehicule.id == null) {
            Log.e(TAG, "Invalid vehicule update!")
            return
        }

        val baseUrl = ApiClient.getInstance().baseUrl
        val uriBuilder = baseUrl.buildUpon()
        uriBuilder.appendPath("customers")
                .appendPath(AuthenticationHelper.getCustomer()?.id.toString())
                .appendPath("vehicules")
                .appendPath(vehicule.id.toString())
        val vehiculeUrl = uriBuilder.build()
        val body = vehicule.toJson()
        Log.i(TAG, "updateVehicule() - url: %s".format(vehiculeUrl.toString()))
        val request = object: JsonObjectRequest(
                Request.Method.PATCH,
                vehiculeUrl.toString(),
                body,
                { response ->
                    Log.i(this@VehiculeClient.TAG, "Response: " + response.toString())
                    complete(null)
                }, { error ->
                    Log.e(this@VehiculeClient.TAG, "Error: " + error.toString())
                    error.printStackTrace()
                    complete(Error(error.toString(), error))
                }) {

            override fun getHeaders(): MutableMap<String, String> {
                return ApiClient.getInstance().authHeaders
            }
        }
        ApiClient.getInstance().addRequest(request)
    }
}