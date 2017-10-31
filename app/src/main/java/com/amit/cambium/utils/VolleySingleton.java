package com.amit.cambium.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import okhttp3.OkHttpClient;

/**
 * Singleton Volley class for all network related tasks
 * Manages the volley queue of the entire application
 * Created by Amit Barjatya on 10/29/17.
 */

public class VolleySingleton {

    private static VolleySingleton instance;
    private RequestQueue requestQueue;

    private VolleySingleton(Context context) {
        requestQueue = Volley.newRequestQueue(context, new OkHttpStack(new OkHttpClient()));
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context.getApplicationContext());
        }
    }

    public static VolleySingleton getInstance() {
        if (instance == null) {
            throw new RuntimeException("Volley not initialized. Call init() first");
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}