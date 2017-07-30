package com.carko.carko.models

/**
 * Created by fabrice on 2017-07-29.
 */
public class Customer(
        var email: String,
        var displayName: String,
        var id: Int,
        var firebaseId: String,
        var stripeId: String,
        var vehicule: Vehicule?,
        var accountId: String?,
        var externalLast4Digits: String?,
        var externalBankName: String?)