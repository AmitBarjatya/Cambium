package com.amit.cambium.views.projectdetail;

import com.amit.cambium.models.Project;

/**
 * MVP contract for Project detail feature
 *
 * Created by Amit Barjatya on 10/29/17.
 */

public interface ProjectDetailContract {
    interface View {
        void showProjectDetails(Project project);
    }

    interface Presenter {

        void setSerialNumber(long serialNumber);

        void onResumed();

        void onPaused();
    }
}
