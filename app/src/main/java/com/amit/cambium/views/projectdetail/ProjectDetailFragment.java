package com.amit.cambium.views.projectdetail;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amit.cambium.R;
import com.amit.cambium.models.Project;
import com.amit.cambium.utils.DisplayUtils;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment which shows details of a project
 * Serial Number is passed via Bundle Arguments
 *
 * Created by Amit Barjatya on 10/29/17.
 */

public class ProjectDetailFragment extends Fragment implements ProjectDetailContract.View {

    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a, dd/MM/yyyy ");

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.owner)
    TextView owner;

    @BindView(R.id.endTime)
    TextView endsOn;

    @BindView(R.id.blurb)
    TextView blurb;

    @BindView(R.id.amtPledged)
    TextView amtPledged;

    @BindView(R.id.backers)
    TextView backers;

    @BindView(R.id.percentageFunded)
    TextView percentageFunded;

    @BindView(R.id.location)
    TextView location;

    Unbinder unbinder;
    ProjectDetailContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            setSharedElementEnterTransition(
                    TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    /**
     * OnViewCreated callback from the Android system
     * Once the view is created, notifies the presenter with the serial number of the project
     * for which details needs to be shown
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new ProjectDetailPresenter(this);
        if (getArguments() != null) {
            String transitionName = getArguments().getString("transitionName");
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                title.setTransitionName(transitionName);
            }
            long serialNumber = getArguments().getLong("serialNumber");
            mPresenter.setSerialNumber(serialNumber);
            startPostponedEnterTransition();
        }
    }

    /**
     * Notify the presenter that the view is resumed
     */
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResumed();
    }

    /**
     * Notify the presenter that the view is paused
     */
    @Override
    public void onPause() {
        mPresenter.onPaused();
        super.onPause();
    }

    /**
     * Release ButterKnife resources when the view gets destroyed
     */
    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    /**
     * Display this project details in this view
     * @param project
     */
    @Override
    public void showProjectDetails(Project project) {
        title.setText(project.getTitle());
        owner.setText(String.format(getString(R.string.all_project_by_placeholder), project.getBy()));
        blurb.setText(project.getBlurb());
        String amt = DisplayUtils.currencyFormat(project.getCurrency(), project.getAmtPledged());
        amtPledged.setText(String.format(getString(R.string.all_amount_pledged_placeholder), amt));
        backers.setText(String.format(getString(R.string.all_backers_placeholder),project.getNumBackers()));
        endsOn.setText(String.format(getString(R.string.all_ending_on_placeholder), sdf.format(project.getEndTime())));
        double percentage = ((double) project.getPercentageFunded()) / 100;
        percentageFunded.setText(String.format(getString(R.string.all_percentage_completed_placeholder), percentage));
        location.setText(String.format(getString(R.string.all_location_placeholder), project.getLocation(), project.getCountry()));
    }
}
