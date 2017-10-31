package com.amit.cambium;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amit.cambium.views.projectlist.ProjectListFragment;

/**
 * MainActivity class for the application
 * Manages the fragment transactions
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
    }

    /**
     * On Start initialize with list of project fragment
     */
    private void initFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new ProjectListFragment(), null)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Finish this activity when there is just one fragment in the backstack
     */
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
