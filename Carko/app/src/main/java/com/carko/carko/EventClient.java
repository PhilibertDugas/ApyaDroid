package com.carko.carko;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fabrice on 2017-06-17.
 */

public class EventClient {

    static class Complete {
        public void onComplete(Object response, Error e){}
    }

    private static String TAG = EventClient.class.getSimpleName();

    public static void getAllEvents(final Complete complete) {
        Uri baseUrl = ApiClient.getInstance().getBaseUrl();
        Uri.Builder uriBuilder = baseUrl.buildUpon();
        uriBuilder.appendPath("events");
        Uri eventsUrl = uriBuilder.build();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                eventsUrl.toString(), null, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response){
                Log.d(TAG, response.toString());
                ArrayList<Event> events = new ArrayList<>();
                Error error = null;
                try {
                    for (int i = 0; i < response.length(); i++) {
                        events.add(new Event((JSONObject) response.get(i)));
                    }
                } catch (JSONException e) {
                    error = new Error(e.getMessage());
                }
                complete.onComplete(events, error);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                complete.onComplete(new ArrayList<Event>(), new Error(error.getMessage()));
            }
        });
        ApiClient.getInstance().addRequest(request);
    }

    public static void getEventParkings(){}
}
