package com.amit.cambium;

import android.app.Application;

import com.amit.cambium.utils.VolleySingleton;
import com.squareup.leakcanary.LeakCanary;

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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        Realm.init(getApplicationContext());
        VolleySingleton.init(this);
    }
}
