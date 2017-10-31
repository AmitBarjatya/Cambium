package com.amit.cambium.views.projectdetail;

import com.amit.cambium.models.Project;
import com.amit.cambium.models.repository.DataSource;
import com.amit.cambium.models.repository.DataSourceImpl;

/**
 * MVP presenter for detail feature
 *
 * Created by Amit Barjatya on 10/29/17.
 */

public class ProjectDetailPresenter implements ProjectDetailContract.Presenter {
    /**
     * View which obeys ProjectDetailContract.View contract
     */
    private ProjectDetailContract.View mView;

    /**
     * Datasource to get the project from
     */
    private DataSource dataSource;

    /**
     * serial number of the project currently being displayed
     */
    private long serialNumber = -1;

    ProjectDetailPresenter(ProjectDetailContract.View mView) {
        this.mView = mView;
        dataSource = new DataSourceImpl();
    }

    /**
     * When resumed fetch project with the given serial number
     */
    @Override
    public void onResumed() {
        if (serialNumber != -1) {
            Project project = dataSource.getProjectBySerialNumber(serialNumber);
            mView.showProjectDetails(project);
        }
    }

    /**
     * Clears out listeners from datasource
     */
    @Override
    public void onPaused() {
        dataSource.clearListeners();
    }

    /**
     * Show the details of the project with given serial number
     * @param serialNumber
     */
    @Override
    public void setSerialNumber(long serialNumber) {
        this.serialNumber = serialNumber;
        Project project = dataSource.getProjectBySerialNumber(serialNumber);
        if (project != null)
            mView.showProjectDetails(project);
    }
}
