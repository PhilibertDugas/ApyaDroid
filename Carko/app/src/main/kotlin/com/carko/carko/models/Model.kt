package com.carko.carko.models

import org.json.JSONObject

/**
 * Created by fabrice on 2017-08-01.
 */
interface Model {

//    fun populate(data: JSONObject){
//        this::class.memberProperties.filterIsInstance<KMutableProperty<*>>().forEach { property ->
//            val type = property.returnType
//            val value = type.javaClass.cast(data.get(property.name))
//            property.setter.call(this, value)
//        }
//    }
//
//    fun toJson(): String {
//        val json = JSONObject()
//        this::class.memberProperties.forEach { property ->
//            Log.i("APYA - Model", "toJson() - property: %s".format(property.name))
//            val value = property.getter.call(this)
//            Log.i("APYA - Model", "toJson() - value: %s".format(value))
////            json.put(property.name, property.getter.call(this))
//        }
//        return json.toString()
//    }

    fun toJson(): JSONObject

}