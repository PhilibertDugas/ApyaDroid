package com.carko.carko.controllers

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.carko.carko.AppState
import com.carko.carko.models.BankAccount
import com.carko.carko.utils.Utils
import org.json.JSONObject

/**
 * Created by fabrice on 2017-08-20.
 */
object BankAccountClient {

    val TAG = "APYA - AccountClient"

    fun postAccount(account: BankAccount, complete: (Error?) -> Unit) {
        if (AppState.customer == null) {
            Log.e(TAG, "Customer is not logged in!")
            return
        }

        val tosAcceptance = JSONObject()
        val ip = Utils.getDeviceIPAddress(true)
        tosAcceptance.put("date", Math.round(System.currentTimeMillis()/1000.0f))
        tosAcceptance.put("ip", ip)

        val accountJson = JSONObject()
        accountJson.put("legal_entity", account.toJson())
        accountJson.put("tos_acceptance", tosAcceptance)

        val parameters = JSONObject()
        parameters.put("account", accountJson)

        val baseUrl = ApiClient.getInstance().baseUrl
        val url = baseUrl.buildUpon()
                .appendPath("customers")
                .appendPath(AppState.customer?.id.toString())
                .appendPath("accounts")
                .build()

        Log.i(TAG, "postAccount() - url: $url")
        val request = object: JsonObjectRequest(
                Request.Method.POST,
                url.toString(),
                parameters, { response ->
            Log.i(TAG, "postAccount() - response: %s".format(response))
            complete(null)
        }, { error ->
            Log.e(TAG, "postAccount() - error: %s".format(error.toString()))
            complete(Error(String(error.networkResponse.data)))
        }) {

            override fun getHeaders(): MutableMap<String, String> {
                return ApiClient.getInstance().authHeaders
            }
        }
        ApiClient.getInstance().addRequest(request)
    }

    fun postExternalAccount(token: String, complete: (Error?) -> Unit) {
        val tokenJson = JSONObject()
        tokenJson.put("token", token)

        val parameters = JSONObject()
        parameters.put("external", tokenJson)

        val baseUrl = ApiClient.getInstance().baseUrl
        val url = baseUrl.buildUpon()
                .appendPath("customers")
                .appendPath(AppState.customer?.id.toString())
                .appendPath("external")
                .build()

        Log.i(TAG, "postExternalAccount() - url: $url")
        val request = object: JsonObjectRequest(
                Request.Method.POST,
                url.toString(),
                parameters, { response ->
            Log.i(TAG, "postExternalAccount() - response: %s".format(response))
            complete(null)
        }, { error ->
            Log.e(TAG, "postExternalAccount() - error: %s".format(error.toString()))
            complete(Error(String(error.networkResponse.data)))
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                return ApiClient.getInstance().authHeaders
            }
        }
        ApiClient.getInstance().addRequest(request)
    }
}