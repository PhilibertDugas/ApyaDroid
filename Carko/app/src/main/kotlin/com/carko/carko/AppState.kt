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

    fun cacheCustomer(context: Context, customer: Customer) {
        this.customer = customer
        updateCache(context)
    }

    fun cacheAuthToken(context: Context, token: String?) {
        this.authToken = token
        updateTokenCache(context)
    }

    fun cacheBankToken(context: Context, token: Token) {
        this.customer?.accountId = token.id
        this.customer?.externalLast4Digits = token.bankAccount.last4
        this.customer?.externalBankName = token.bankAccount.bankName
        updateCache(context)
    }

    fun cachedCustomer(context: Context): Customer? {
        if (this.customer == null) {
            Log.i(TAG, "Caching customer")
            val preferences = context.getSharedPreferences(
                    context.getString(R.string.preferences_file), Context.MODE_PRIVATE)

            val data = preferences.getString(USER_KEY, null)
            if (data != null) {
                val cachedCustomer = JSONObject(data)
                val customer = Customer(cachedCustomer)
                this.customer = customer
            }
        }
        return this.customer
    }

    fun cachedToken(context: Context): String? {
        if (this.authToken == null) {
            val preferences = context.getSharedPreferences(
                    context.getString(R.string.preferences_file), Context.MODE_PRIVATE)
            val data = preferences.getString(AUTH_KEY, null)
            this.authToken = data
        }
        return this.authToken
    }

    private fun updateCache(context: Context) {
        val preferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file), Context.MODE_PRIVATE)
        val jsonCustomer = this.customer?.toJson()
        preferences.edit().putString(USER_KEY, jsonCustomer.toString()).apply()
    }

    private fun updateTokenCache(context: Context) {
        val preferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file), Context.MODE_PRIVATE)
        preferences.edit().putString(AUTH_KEY, this.authToken).apply()
    }

    fun resetCustomer(context: Context) {
        this.customer = null
        val preferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file), Context.MODE_PRIVATE)
        preferences.edit().remove(USER_KEY).apply()
    }

}