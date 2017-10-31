package com.amit.cambium.views.projectlist;

import com.amit.cambium.models.Project;

import java.util.List;

/**
 *
 * MVP contract for project list feature
 *
 * Created by Amit Barjatya on 10/29/17.
 */

public interface ProjectListContract {

    interface View {
        void show(List<Project> projects);

        void showErrorScreen(String errorMessage);

        void showDataFetchScreen();

        void showRecyclerView();

        void showDetailsFragmentFor(Project project);

        void showToast(String s);
    }

    interface Presenter {

        void onResumed();

        void onPaused();

        void onItemClicked(Project project);

        void applyBackerFilter(String minBacker, String maxBacker);

        void sortByEndAscending();

        void sortByEndDescending();

        void sortByA2Z();

        void sortByZ2A();

        void applyTitleFilter(CharSequence text);
    }

}
