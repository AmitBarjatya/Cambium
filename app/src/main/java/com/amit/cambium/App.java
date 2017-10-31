package com.amit.cambium;

import android.app.Application;

import com.amit.cambium.utils.VolleySingleton;

import io.realm.Realm;

/**
 * Real Application class for this app
 * <p>
 * Created by Amit Barjatya on 10/29/17.
 */

public class App extends Application {

    /**
     * Initialize Realm database with app context
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        VolleySingleton.init(this);
    }
}
