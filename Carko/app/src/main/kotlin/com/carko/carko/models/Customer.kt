package com.carko.carko.models

import com.carko.carko.controllers.CustomerClient
import org.json.JSONObject

/**
 * Created by fabrice on 2017-07-29.
 */
class Customer(
        var email: String,
        var displayName: String,
        var id: Int,
        var firebaseId: String,
        var stripeId: String,
        var vehicule: Vehicule?,
        var accountId: String?,
        var externalLast4Digits: String?,
        var externalBankName: String?): Model {

    constructor(json: JSONObject): this(
            json.getString("email"),
            json.getString("display_name"),
            json.getInt("id"),
            json.getString("firebase_id"),
            json.getString("stripe_id"),
            if (json.has("vehicule"))
                Vehicule(json.getJSONObject("vehicule"))
            else
                null,
            if (json.has("account_id"))
                json.getString("account_id")
            else
                null,
            if (json.has("bank_last_4_digits"))
                json.getString("bank_last_4_digits")
            else
                null,
            if (json.has("bank_name"))
                json.getString("bank_name")
            else
                null
    )

    override fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("email", this.email)
        json.put("display_name", this.displayName)
        json.put("id", this.id)
        json.put("firebase_id", this.firebaseId)
        json.put("stripe_id", this.stripeId)
        if (vehicule != null) {
            json.put("vehicule", this.vehicule?.toJson())
            json.put("vehicule_id", this.vehicule?.id)
        }
        if (accountId != null) {
            json.put("account_id", this.accountId)
        }
        if (externalBankName != null) {
            json.put("bank_last_4_digits", this.externalLast4Digits)
            json.put("bank_name", this.externalBankName)
        }
        return json
    }

    companion object {
        fun getCustomer(complete: (Customer?, Error?) -> Unit) {
            CustomerClient.getCustomer(complete)
        }
    }

    class NewCustomer(var email: String?,
                      var displayName: String?,
                      var firebaseId: String): Model {

        override fun toJson(): JSONObject {
            val json = JSONObject()
            json.put("email", this.email)
            json.put("display_name", this.displayName)
            json.put("firebase_id", this.firebaseId)
            return json
        }

        fun register(complete: (Error?) -> Unit) {
            CustomerClient.postCustomer(this, complete)
        }
    }
}