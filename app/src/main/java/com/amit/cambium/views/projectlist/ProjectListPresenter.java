package com.amit.cambium.views.projectlist;

import com.amit.cambium.models.Project;
import com.amit.cambium.models.repository.DataListener;
import com.amit.cambium.models.repository.DataSource;
import com.amit.cambium.models.repository.DataSourceImpl;
import com.amit.cambium.models.repository.ErrorListener;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;

import java.util.List;

/**
 *
 * MVP presenter for Project list feature
 * Created by Amit Barjatya on 10/29/17.
 */

public class ProjectListPresenter implements ProjectListContract.Presenter, DataListener, ErrorListener {

    private static final String TAG = "ProjListPresenter";

    private ProjectListContract.View mView;
    /**
     * DataSource to be used
     */
    private DataSource dataSource;

    ProjectListPresenter(ProjectListContract.View mView) {
        this.mView = mView;
        dataSource = new DataSourceImpl();
    }

    /**
     * WHen the view is resumed ask the datasource for list of projects
     */
    public void onResumed() {
        dataSource.getProjectListAsync(this, this);
    }

    /**
     * when view is paused clear all the resources held by datasource
     */
    @Override
    public void onPaused() {
        dataSource.clearListeners();
    }

    /**
     * Action to be taken when user clicks on an item in the view
     * Current Action: takes user to the details of this project
     * @param project
     */
    @Override
    public void onItemClicked(Project project) {
        mView.showDetailsFragmentFor(project);
    }

    /**
     * Data callback from datasource on successful data fetched
     *
     * @param projectList the list of projects to consume
     */
    @Override
    public void onData(List<Project> projectList) {
        if (projectList.size() > 0) {
            mView.showRecyclerView();
        }
        mView.show(projectList);
    }

    /**
     * Apply backer filter on the list of projects
     * @param minBacker min value for the backers detault is Integer.MIN_VALUE
     * @param maxBacker max value for the backer default is Integer.MAX_VALUE
     */
    @Override
    public void applyBackerFilter(String minBacker, String maxBacker) {
        long minValue = Integer.MIN_VALUE;
        long maxValue =Integer.MAX_VALUE;
        if (minBacker!=null && !minBacker.isEmpty()){
            try {
                minValue = Long.parseLong(minBacker);
            }catch (NumberFormatException e){
                mView.showToast("Minimum Value must be a number.");
                return;
            }
        }

        if (maxBacker!=null && !maxBacker.isEmpty()){
            try {
                minValue = Long.parseLong(minBacker);
            }catch (NumberFormatException e){
                mView.showToast("Maximum Value must be a number.");
                return;
            }
        }

        List<Project> projects = dataSource.withBackerFilter(minValue,maxValue);
        mView.show(projects);
        mView.showToast("Filter applied.");
        if (projects.size() == 0){
            mView.showToast("Found 0 results.");
        }
    }

    /**
     * Sort projects with ascending end time
     */
    @Override
    public void sortByEndAscending() {
        List<Project> projects = dataSource.withEndAscendingSort();
        mView.show(projects);
        mView.showToast("Sorting applied.");
    }

    /**
     * Sort projects with descending end time
     */
    @Override
    public void sortByEndDescending() {
        List<Project> projects = dataSource.withEndDescendingSort();
        mView.show(projects);
        mView.showToast("Sorting applied.");
    }

    /**
     * sort projects alphabetically a to z
     */
    @Override
    public void sortByA2Z() {
        List<Project> projects = dataSource.withA2ZSort();
        mView.show(projects);
        mView.showToast("Sorting applied.");
    }

    /**
     * sort al[phabetically z to a
     */
    @Override
    public void sortByZ2A() {
        List<Project> projects = dataSource.withZ2ASort();
        mView.show(projects);
        mView.showToast("Sorting applied.");
    }

    /**
     * error callback from datasource
     * @param throwable
     */
    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof NoConnectionError){
            mView.showErrorScreen("No Internet Connection");
        }else if (throwable instanceof TimeoutError){
            mView.showErrorScreen("Slow Internet Connection");
        }else{
            mView.showErrorScreen(throwable.getMessage());
        }
    }

    @Override
    public void applyTitleFilter(CharSequence text) {
        List<Project> projects = dataSource.withTitleFilter(text.toString());
        mView.show(projects);
    }
}
