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

        void showRecyclerView();

        void showToast(String s);
    }

    interface Presenter {

        void onViewCreated();

        void onViewDestroyed();

        void applyBackerFilter(String minBacker, String maxBacker);

        void sortByEndAscending();

        void sortByEndDescending();

        void sortByA2Z();

        void sortByZ2A();

        void applyTitleFilter(CharSequence text);
    }

}
