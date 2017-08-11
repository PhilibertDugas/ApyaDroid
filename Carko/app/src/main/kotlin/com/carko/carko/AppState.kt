package com.carko.carko

import android.content.Context
import android.util.Log
import com.carko.carko.models.Customer
import com.stripe.android.model.Token
import org.json.JSONObject

/**
 * Created by fabrice on 2017-07-30.
 */
object AppState {
    val TAG = "APYA - AppState"
    var customer: Customer? = null
    var authToken: String? = null

    val USER_KEY = "user"
    val AUTH_KEY = "auth"

    fun cacheCustomer(customer: Customer) {
        this.customer = customer
        updateCache()
    }

    fun cacheAuthToken(token: String?) {
        this.authToken = token
        updateTokenCache()
    }

    fun cacheBankToken(token: Token) {
        this.customer?.accountId = token.id
        this.customer?.externalLast4Digits = token.bankAccount.last4
        this.customer?.externalBankName = token.bankAccount.bankName
        updateCache()
    }

    fun cachedCustomer(): Customer? {
        if (this.customer == null) {
            Log.i(TAG, "Getting cached customer")
            val context = ApyaApp.getAppContext()
            val preferences = context.getSharedPreferences(
                    context.getString(R.string.preferences_file), Context.MODE_PRIVATE)

            val data = preferences.getString(USER_KEY, null)
            Log.i(TAG, "data: " + data)
            if (data != null && data != "{}") {
                val cachedCustomer = JSONObject(data)
                val customer = Customer(cachedCustomer)
                this.customer = customer
            }
        }
        return this.customer
    }

    fun cachedToken(): String? {
        if (this.authToken == null) {
            val context = ApyaApp.getAppContext()
            val preferences = context.getSharedPreferences(
                    context.getString(R.string.preferences_file), Context.MODE_PRIVATE)
            val data = preferences.getString(AUTH_KEY, null)
            this.authToken = data
        }
        return this.authToken
    }

    private fun updateCache() {
        val context = ApyaApp.getAppContext()
        val preferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file), Context.MODE_PRIVATE)
        val jsonCustomer = this.customer?.toJson()
        Log.i(TAG, "Updating customer cache: " + jsonCustomer.toString())
        preferences.edit().putString(USER_KEY, jsonCustomer.toString()).apply()
    }

    private fun updateTokenCache() {
        val context = ApyaApp.getAppContext()
        val preferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file), Context.MODE_PRIVATE)
        preferences.edit().putString(AUTH_KEY, this.authToken).apply()
    }

    fun resetCustomer() {
        this.customer = null
        val context = ApyaApp.getAppContext()
        val preferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file), Context.MODE_PRIVATE)
        preferences.edit().remove(USER_KEY).apply()
    }

}