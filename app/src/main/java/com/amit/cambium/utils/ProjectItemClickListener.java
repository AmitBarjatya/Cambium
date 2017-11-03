package com.amit.cambium.utils;

import android.view.View;
import android.widget.TextView;

import com.amit.cambium.models.Project;

/**
 * Item click interface for listening to recycler view item clicks
 * Created by Amit Barjatya on 10/29/17.
 */

public interface ProjectItemClickListener {

    void onClick(Project project, TextView titleView, String transitionName);

}
