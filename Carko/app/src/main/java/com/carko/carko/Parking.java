package com.carko.carko;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;

class Parking {

    private String TAG = Parking.class.getSimpleName();

    private int id;
    private int customerId;
    private LatLng latLng;
    private String address;
    private String description;
    private String photoUrl;
    private ArrayList<String> photoUrls;
    private float price;
    private float totalRevenue;
    private AvailabilityInfo availabilityInfo;
    private boolean isAvailable;
    private boolean isComplete;
    private boolean isDeleted;

    //Dummy constructor for testing
    Parking(String address, String availability, int drawable){
    }

    Parking(JSONObject jsonObject) {
        id = JSONHelper.INSTANCE.getInt(jsonObject, "id");
        customerId = JSONHelper.INSTANCE.getInt(jsonObject,"customer_id");
        latLng = new LatLng(
                JSONHelper.INSTANCE.getDouble(jsonObject,"latitude"),
                JSONHelper.INSTANCE.getDouble(jsonObject,"longitude"));
        address = JSONHelper.INSTANCE.getString(jsonObject,"address");
        description = JSONHelper.INSTANCE.getString(jsonObject, "description");
        price = JSONHelper.INSTANCE.getFloat(jsonObject, "price");
        totalRevenue = JSONHelper.INSTANCE.getFloat(jsonObject, "total_revenue");
        availabilityInfo = new AvailabilityInfo(
                JSONHelper.INSTANCE.getJSONObject(jsonObject, "availability_info"));
        isAvailable = JSONHelper.INSTANCE.getBoolean(jsonObject, "is_available");
        isComplete = JSONHelper.INSTANCE.getBoolean(jsonObject, "is_complete");
        isDeleted = JSONHelper.INSTANCE.getBoolean(jsonObject, "is_deleted");
        photoUrl = JSONHelper.INSTANCE.getString(jsonObject, "photo_url");
        photoUrls = JSONHelper.INSTANCE.getArray(jsonObject, "multiple_photo_urls");
    }

    long getId(){ return id; }
    LatLng getLatLng(){ return latLng; }
    String getAvailability() { return ""; }
    String getAddress() {return address; }
    int getDrawable() {
        return 0;
    }

}
