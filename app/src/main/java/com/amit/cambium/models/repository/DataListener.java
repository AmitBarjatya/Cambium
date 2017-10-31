package com.amit.cambium.models.repository;

import com.amit.cambium.models.Project;

import java.util.List;

/**
 *
 * Interface used by a data source to provide any data to the caller.
 *
 *
 * Created by Amit Barjatya on 10/29/17.
 */

public interface DataListener {
    /**
     * Provides data to the caller once it's fetched
     * or on any subsequent data related changes
     *
     * @param projectList the list of projects to consume
     */
    void onData(List<Project> projectList);
}
