package com.carko.carko.models

import org.json.JSONArray

/**
 * Created by fabrice on 2017-08-01.
 */
open class Model() {

    public constructor(data: JSONArray): this() {
        javaClass.declaredFields.forEachIndexed { index, field ->
            field.set(this, data.get(index))
        }
    }

    fun toJson(): String {
        val data = javaClass.declaredFields.map { field ->
            field.get(this).toString()
        }
        return JSONArray(data).toString()
    }
}