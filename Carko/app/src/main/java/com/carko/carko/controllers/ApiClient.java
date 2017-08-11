package com.carko.carko.controllers;

import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.carko.carko.ApyaApp;

/**
 * Created by fabrice on 2017-06-17.
 */

public class ApiClient {
    public static final String TAG = ApiClient.class.getSimpleName();

    private Uri baseUrl;
    private RequestQueue requestQueue;
    private static ApiClient instance;

    private ApiClient() {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("integration-apya.herokuapp.com");
        baseUrl = uriBuilder.build();
        instance = this;
    }

    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    private RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(ApyaApp.Companion.getAppContext());
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
