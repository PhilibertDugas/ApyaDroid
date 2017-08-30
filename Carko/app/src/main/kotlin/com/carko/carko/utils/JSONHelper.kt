package com.carko.carko.utils

import android.util.Log
import org.json.JSONException
import org.json.JSONObject

object JSONHelper {

    val TAG: String = JSONHelper::class.java.getSimpleName()

    fun getInt(jsonObject: JSONObject, name: String): Int {
        try {
            return jsonObject.getInt(name)
        } catch (e: JSONException){
            Log.e(TAG, e.message)
            return 0
        }
    }

    fun getFloat(jsonObject: JSONObject, name: String): Float {
        try {
            return jsonObject.getDouble(name).toFloat()
        } catch (e: JSONException){
            Log.e(TAG, e.message)
            return 0.0f
        }
    }

    fun getDouble(jsonObject: JSONObject, name: String): Double {
        try {
            return jsonObject.getDouble(name)
        } catch (e: JSONException){
            Log.e(TAG, e.message)
            return 0.0
        }
    }

    fun getBoolean(jsonObject: JSONObject, name: String): Boolean {
        try {
            return jsonObject.getBoolean(name)
        } catch (e: JSONException){
            Log.e(TAG, e.message)
            return false
        }
    }

    fun getString(jsonObject: JSONObject, name: String): String {
        try {
            return jsonObject.getString(name)
        } catch (e: JSONException){
            Log.e(TAG, e.message)
            return ""
        }
    }

    fun <T> getArray(jsonObject: JSONObject, name: String): ArrayList<T> {
        val array = ArrayList<T>()
        try {
            val jsonArray = jsonObject.getJSONArray(name)
            for (i in 0..jsonArray.length()-1) {
                array.add(jsonArray.get(i) as T)
            }
            return array
        } catch (e: JSONException) {
            Log.e(TAG, e.message)
            return array
        }
    }

    fun getJSONObject(jsonObject: JSONObject, name: String): JSONObject {
        try {
            return jsonObject.getJSONObject(name)
        } catch (e: JSONException){
            Log.e(TAG, e.message)
            return JSONObject()
        }
    }
}