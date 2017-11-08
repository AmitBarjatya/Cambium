package com.amit.cambium.models.repository;

import com.amit.cambium.models.Project;

import java.util.List;

/**
 *
 * This class describes the local storage system used by
 * the applications presenter layer.
 *
 *
 * Created by Amit Barjatya on 10/29/17.
 */

public interface DataSource {

    /**
     * Provide list of projects asynchronously
     *
     * @param dataListener listener for data
     * @param errorListener listener for any error
     * @return list of projects once fetched
     */
    List<Project> getProjectListAsync(DataListener dataListener, ErrorListener errorListener);

    /**
     * Free up any resource held by the implementation
     */
    void clearListeners();

    /**
     * Get a Project from the local db
     *
     * @param serialNumber the serial number of the Project
     * @return Project
     */
    Project getProjectBySerialNumber(long serialNumber);

    /**
     * Apply Filter on number of backers for the available list of projects
     *
     * @param minValue minimum number of backers
     * @param maxValue maximum number of backers
     * @return list of filtered projects
     */
    List<Project> withBackerFilter(long minValue, long maxValue);

    /**
     * Sort the Project list with ascending end time
     * @return list of sorted projects
     */
    List<Project> withEndAscendingSort();

    /**
     * Sort the Project list with descending end time
     * @return list of sorted projects
     */
    List<Project> withEndDescendingSort();

    /**
     * Sort the Project list with title a to z
     * @return list of sorted projects
     */
    List<Project> withA2ZSort();

    /**
     * Sort the Project list with title z to a
     * @return list of sorted projects
     */
    List<Project> withZ2ASort();

    /**
     * with title filter
     * @param s
     * @return
     */
    List<Project> withTitleFilter(String s);

    /**
     * With no title filter
     * @return
     */
    List<Project> withNoFilters();
}
