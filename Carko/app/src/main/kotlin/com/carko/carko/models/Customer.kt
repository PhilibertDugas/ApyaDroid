package com.carko.carko.models

import org.json.JSONArray

/**
 * Created by fabrice on 2017-07-29.
 */
class Customer: Model {

    lateinit var email: String
    lateinit var displayName: String
    var id: Int = 0
    lateinit var firebaseId: String
    lateinit var stripeId: String
    var vehicule: Vehicule? = null
    var accountId: String? = null
    var externalLast4Digits: String? = null
    var externalBankName: String? = null

    constructor(email: String, displayName: String, id: Int, firebaseId: String, stripeId: String,
                vehicule: Vehicule?, accountId: String?, externalLast4Digits: String?,
                externalBankName: String?) {
        this.email = email
        this.displayName = displayName
        this.id = id
        this.firebaseId = firebaseId
        this.stripeId = stripeId
        this.vehicule = vehicule
        this.accountId = accountId
        this.externalLast4Digits = externalLast4Digits
        this.externalBankName = externalBankName
    }

    constructor(data: JSONArray): super(data)
}