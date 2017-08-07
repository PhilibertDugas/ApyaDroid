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
            json.getString("displayName"),
            json.getInt("id"),
            json.getString("firebaseId"),
            json.getString("firebaseId"),
            if (json.getJSONObject("vehicule") != null)
                Vehicule(json.getJSONObject("vehicule"))
            else
                null,
            json.getString("accountId"),
            json.getString("externalLast4Digits"),
            json.getString("externalBankName")
    )

    override fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("email", this.email)
        json.put("displayName", this.displayName)
        json.put("id", this.id)
        json.put("firebaseId", this.firebaseId)
        json.put("stripeId", this.stripeId)
        json.put("vehicule", this.vehicule?.toJson())
        json.put("accountId", this.accountId)
        json.put("externalLast4Digits", this.externalLast4Digits)
        json.put("externalBankName", this.externalBankName)
        return json
    }

    companion object {
        fun getCustomer(complete: (Customer?, String?) -> Unit) {
            CustomerClient.getCustomer(complete)
        }
    }

    class NewCustomer(var email: String?,
                      var displayName: String?,
                      var firebaseId: String): Model {

        override fun toJson(): JSONObject {
            val json = JSONObject()
            json.put("email", this.email)
            json.put("displayName", this.displayName)
            json.put("firebaseId", this.firebaseId)
            return json
        }

        fun register(complete: (String?) -> Unit) {
            CustomerClient.postCustomer(this, complete)
        }
    }
}