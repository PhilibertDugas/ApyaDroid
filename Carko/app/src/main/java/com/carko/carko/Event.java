package com.carko.carko;

import android.net.Uri;
import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

/**
 * Created by fabrice on 2017-06-10.
 */

public class Event {
    private final String TAG = Event.class.getSimpleName();

    private int id;
    private LatLng latLng;
    private String photoURL;
    private int drawable;  // TODO: replace this with the photo URL
    private int range;
    private float price;
    private String label;
    private int targetAudience;
    private String startTime;
    private String endTime;

    Event(int id, int drawable, String label){
        this.id = id;
        this.drawable = drawable;
        this.label = label;
    }

    Event(JSONObject obj) throws JSONException{
        this.photoURL = obj.getString("photo_url");
        this.id = obj.getInt("id");
        this.label = obj.getString("label");
        this.latLng = new LatLng(obj.getDouble("latitude"), obj.getDouble("longitude"));
        this.price = (float) obj.getDouble("price");
        this.range = obj.getInt("range");
        this.startTime = obj.getString("start_time");
        this.endTime = obj.getString("end_time");
        this.targetAudience = obj.getInt("target_audience");
        this.drawable = R.drawable.ghost;
    }

    public int getId(){
        return id;
    }

    public int getDrawable(){
        return drawable;
    }

    public String getLabel(){
        return label;
    }

    public String getPhotoURL(){
        return this.photoURL;
    }

    public String toString(){
        String string = "Event " + id + ": " + label;
        return string;
    }

    public static void getAllEvents(EventClient.Complete complete){
        EventClient.getAllEvents(complete);
    }
}
