package com.carko.carko;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.net.URL;

/**
 * Created by fabrice on 2017-06-10.
 */

public class Event {
    private int id;
    private LatLng latLng;
    private URL photoURL;
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

    public int getId(){
        return id;
    }

    public int getDrawable(){
        return drawable;
    }

    public String getLabel(){
        return label;
    }

    public String toString(){
        String string = "Event " + id + ": " + label;
        return string;
    }
}
