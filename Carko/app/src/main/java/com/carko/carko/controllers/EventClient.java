package com.carko.carko.controllers;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.carko.carko.models.Event;
import com.carko.carko.models.Parking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventClient {

    public interface Complete<T> {
        void onComplete(T response, String e);
    }

    private static String TAG = "APYA - " + EventClient.class.getSimpleName();

    public static void getAllEvents(final Complete complete) {
        Uri baseUrl = ApiClient.getInstance().getBaseUrl();
        Uri.Builder uriBuilder = baseUrl.buildUpon();
        uriBuilder.appendPath("events");
        Uri eventsUrl = uriBuilder.build();
        Log.i(TAG, "events url: " + eventsUrl.toString());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                eventsUrl.toString(), null, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response){
                Log.i(TAG, response.toString());
                ArrayList<Event> events = new ArrayList<>();
                String error = null;
                try {
                    for (int i = 0; i < response.length(); i++) {
                        events.add(new Event((JSONObject) response.get(i)));
                    }
                } catch (JSONException e) {
                    error = e.getMessage();
                }
                complete.onComplete(events, error);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.toString());
                complete.onComplete(new ArrayList<Event>(), error.toString());
            }
        });
        ApiClient.getInstance().addRequest(request);
    }

    static void getEventParkings(Event event, final Complete complete){
        Uri baseUrl = ApiClient.getInstance().getBaseUrl();
        Uri.Builder uriBuilder = baseUrl.buildUpon()
                .appendPath("events")
                .appendPath(Integer.toString(event.getId()))
                .appendPath("parkings");
        Uri eventParkingsUrl = uriBuilder.build();
        Log.i(TAG, "event parkings url: " + eventParkingsUrl.toString());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                eventParkingsUrl.toString(), null, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response){
                Log.i(TAG, response.toString());
                ArrayList<Parking> parkings = new ArrayList<>();
                String error = null;
                try {
                    for (int i = 0; i < response.length(); i++) {
                        parkings.add(new Parking((JSONObject) response.get(i)));
                    }
                } catch (JSONException e) {
                    error = e.getMessage();
                }
                complete.onComplete(parkings, error);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                complete.onComplete(new ArrayList<Event>(), error.getMessage());
            }
        });
        ApiClient.getInstance().addRequest(request);
    }
}
