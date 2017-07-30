package com.carko.carko.models

/**
 * Created by fabrice on 2017-07-29.
 */
class Reservation(
        var id: Int,
        var parking: Parking?,
        var event: Event?,
        var customerId: Int,
        var isActive: Boolean,
        var startTime: String,
        var stopTime: String,
        var totalCost: Float,
        var charge: String)