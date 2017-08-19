package com.carko.carko.controllers

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.carko.carko.AuthenticationHelper
import com.carko.carko.models.Vehicle
import org.json.JSONException

/**
 * Created by fabrice on 2017-08-10.
 */
object VehicleClient {
    val TAG = "APYA - VehicleClient"

    fun postVehicule(vehicle: Vehicle, complete: (Vehicle?, Error?) -> Unit) {
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
        val body = vehicle.toJson()
        Log.i(TAG, "postVehicule() - url: %s".format(vehiculeUrl.toString()))
        val request = object: JsonObjectRequest(
                Request.Method.POST,
                vehiculeUrl.toString(),
                body,
                { response ->
                    Log.i(this@VehicleClient.TAG, "Response: " + response.toString())
                    var result: Vehicle? = null
                    var error: Error? = null
                    try {
                        result = Vehicle(response)
                    } catch (e: JSONException) {
                        error = Error(e.toString(), e)
                    }
                    complete(result, error)
                }, { error ->
                    Log.e(this@VehicleClient.TAG, "Error: " + error.toString())
                    error.printStackTrace()
                    complete(null, Error(error.toString(), error))
                }) {

            override fun getHeaders(): MutableMap<String, String> {
                return ApiClient.getInstance().authHeaders
            }
        }
        ApiClient.getInstance().addRequest(request)
    }

    fun updateVehicule(vehicle: Vehicle, complete: (Error?) -> Unit) {
        if (!AuthenticationHelper.customerAvailable()) {
            Log.e(TAG, "Customer is not available!")
            return
        }

        if (vehicle.id == null) {
            Log.e(TAG, "Invalid vehicle update!")
            return
        }

        val baseUrl = ApiClient.getInstance().baseUrl
        val uriBuilder = baseUrl.buildUpon()
        uriBuilder.appendPath("customers")
                .appendPath(AuthenticationHelper.getCustomer()?.id.toString())
                .appendPath("vehicules")
                .appendPath(vehicle.id.toString())
        val vehiculeUrl = uriBuilder.build()
        val body = vehicle.toJson()
        Log.i(TAG, "updateVehicule() - url: %s".format(vehiculeUrl.toString()))
        val request = object: JsonObjectRequest(
                Request.Method.PATCH,
                vehiculeUrl.toString(),
                body,
                { response ->
                    Log.i(this@VehicleClient.TAG, "Response: " + response.toString())
                    complete(null)
                }, { error ->
                    Log.e(this@VehicleClient.TAG, "Error: " + error.toString())
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