package com.carko.carko.controllers;

import android.app.Application;
import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by fabrice on 2017-06-17.
 */

public class ApiClient extends Application{
    public static final String TAG = ApiClient.class.getSimpleName();

    private Uri baseUrl;
    private RequestQueue requestQueue;
    private static ApiClient instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("integration-apya.herokuapp.com");
        baseUrl = uriBuilder.build();
        instance = this;
    }

    public static ApiClient getInstance(){
        return instance;
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public Uri getBaseUrl(){
        return this.baseUrl;
    }

    public <T> void addRequest(Request<T> request, String tag){
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    public <T> void addRequest(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelAll(Object tag){
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}
