package com.carko.carko.models

import com.carko.carko.controllers.VehiculeClient
import org.json.JSONObject

/**
 * Created by fabrice on 2017-07-29.
 */
class Vehicule(var license: String,
               var make: String,
               var model: String,
               var year: String,
               var color: String,
               var province: String,
               var id: Int?): Model {

    constructor(json: JSONObject): this(
            json.getString("license"),
            json.getString("make"),
            json.getString("model"),
            json.getString("year"),
            json.getString("color"),
            json.getString("province"),
            if (json.has("id")) json.getInt("id") else null
    )

    override fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("license", this.license)
        json.put("make", this.make)
        json.put("model", this.model)
        json.put("year", this.year)
        json.put("color", this.color)
        json.put("province", this.province)
        json.put("id", this.id)
        return json
    }

    fun persist(complete: (Vehicule?, Error?) -> Unit) {
        VehiculeClient.postVehicule(this, complete)
    }

    fun update(complete: (Error?) -> Unit) {
        VehiculeClient.updateVehicule(this, complete)
    }
}