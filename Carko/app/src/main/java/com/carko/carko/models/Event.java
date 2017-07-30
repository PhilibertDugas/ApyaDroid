package com.carko.carko.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.carko.carko.controllers.EventClient;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fabrice on 2017-06-10.
 */

public class Event implements Parcelable {
    private final String TAG = Event.class.getSimpleName();

    private int id;
    private LatLng pos;
    private String photoURL;
    private int range;
    private float price;
    private String label;
    private int targetAudience;
    private String startTime;
    private String endTime;

    public Event(JSONObject obj) throws JSONException{
        this.photoURL = obj.getString("photo_url");
        this.id = obj.getInt("id");
        this.label = obj.getString("label");
        this.pos = new LatLng(obj.getDouble("latitude"), obj.getDouble("longitude"));
        this.price = (float) obj.getDouble("price");
        this.range = obj.getInt("range");
        this.startTime = obj.getString("start_time");
        this.endTime = obj.getString("end_time");
        this.targetAudience = obj.getInt("target_audience");
    }

    public int getId(){
        return id;
    }

    public String getLabel(){
        return label;
    }

    public String getPhotoURL(){
        return this.photoURL;
    }

    public LatLng getPos() { return this.pos; }

    public int getRange() { return this.range; }

    public float getPrice() { return this.price; }

    public String toString(){
        String string = "Event " + id + ": " + label;
        return string;
    }

    public static void getAllEvents(EventClient.Complete complete){
        EventClient.getAllEvents(complete);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags){
        int[] ints = {this.id, this.range, this.targetAudience};
        double[] doubles = {this.pos.latitude, this.pos.longitude};
        String[] strings = {this.photoURL, this.label, this.startTime, this.endTime};
        out.writeIntArray(ints);
        out.writeDoubleArray(doubles);
        out.writeStringArray(strings);
        out.writeFloat(this.price);
    }

    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>(){
        public Event createFromParcel(Parcel in){
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    private Event(Parcel in){
        int[] ints = new int[3];
        double[] doubles = new double[2];
        String[] strings = new String[4];
        in.readIntArray(ints);
        in.readDoubleArray(doubles);
        in.readStringArray(strings);
        this.price = in.readFloat();
        this.id = ints[0];
        this.range = ints[1];
        this.targetAudience = ints[2];
        this.pos = new LatLng(doubles[0], doubles[1]);
        this.photoURL = strings[0];
        this.label = strings[1];
        this.startTime = strings[2];
        this.endTime = strings[3];
    }
}
