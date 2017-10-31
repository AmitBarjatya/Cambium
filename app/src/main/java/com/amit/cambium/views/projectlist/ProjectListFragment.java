package com.amit.cambium.views.projectlist;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amit.cambium.R;
import com.amit.cambium.views.projectdetail.ProjectDetailFragment;
import com.amit.cambium.models.Project;
import com.amit.cambium.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

/**
 * Fragment which shows lists of projects in a recycler view
 * Created by Amit Barjatya on 10/29/17.
 */

public class ProjectListFragment extends Fragment implements ProjectListContract.View, ItemClickListener {

    private static final String TAG = "ProjListFragment";

    @BindView(R.id.rvProjectList)
    RecyclerView rvProjectList;

    @BindView(R.id.rlError)
    RelativeLayout rlError;

    @BindView(R.id.rlFetchingData)
    RelativeLayout rlFetchingData;

    @BindView(R.id.errorText)
    TextView tvErrorMessage;

    @OnClick(R.id.filter)
    public void onFilterButtonClicked(){
        showFilterDialog();
    }

    @OnClick(R.id.sort)
    public void onSortButtonClicked(){
        showSortOptionDialog();
    }

    @OnTextChanged(R.id.search)
    public void onSearchText(CharSequence text){
        mPresenter.applyTitleFilter(text);
    }

    /**
     * Adapter to show the list of projects
     */
    ProjectListAdapter mAdapter;
    /**
     * Presenter to manage application logic
     */
    ProjectListContract.Presenter mPresenter;

    /**
     * list of projects to show
     */
    List<Project> projects;

    /**
     * Butterknife unbinder to release resources when view gets destroyed
     */
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        mPresenter = new ProjectListPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResumed();
    }

    @Override
    public void onPause() {
        mPresenter.onPaused();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onClick(Object object) {
        Project project = (Project) object;
        mPresenter.onItemClicked(project);
    }

    void initRecyclerView() {
        projects = new ArrayList<>();
        mAdapter = new ProjectListAdapter(projects, this);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvProjectList.setLayoutManager(llm);
        rvProjectList.setAdapter(mAdapter);
    }

    /**
     * Shows the list of projects in this view
     * @param projs
     */
    @Override
    public void show(List<Project> projs) {
        if (projects != null && projs!=null) {
            projects.clear();
            projects.addAll(projs);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Go to details fragment with the given projects serial number
     * @param project
     */
    @Override
    public void showDetailsFragmentFor(Project project) {
        Fragment fragment = new ProjectDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("serialNumber", project.getSerialNumber());
        fragment.setArguments(bundle);
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right, android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right)
                    .replace(R.id.fragment_container, fragment, null)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Filter Dialog box for taking minBackers and maxBackers value
     *
     * On Done presenter should show only the list of projects which have backers in this range
     */
    private void showFilterDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter);


        final EditText minValue = (EditText) dialog.findViewById(R.id.minValue);
        final EditText maxValue = (EditText) dialog.findViewById(R.id.maxValue);
        AppCompatButton done = (AppCompatButton) dialog.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String minBacker = minValue.getText().toString();
                String maxBacker = maxValue.getText().toString();
                mPresenter.applyBackerFilter(minBacker,maxBacker);
                dialog.cancel();
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    /**
     * Apply custom sort on list of projects
     */
    private void showSortOptionDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sort);

        TextView tvEndAscending = (TextView) dialog.findViewById(R.id.tv_time_ascending);
        TextView tvEndDescending = (TextView) dialog.findViewById(R.id.tv_time_descending);
        TextView tvAlphabeticallyA2Z = (TextView) dialog.findViewById(R.id.tv_alphabetically_a_to_z);
        TextView tvAlphabeticallyZ2A = (TextView) dialog.findViewById(R.id.tv_alphabetically_z_to_a);

        tvEndAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.sortByEndAscending();
                dialog.cancel();
            }
        });


        tvEndDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.sortByEndDescending();
                dialog.cancel();
            }
        });
        tvAlphabeticallyA2Z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.sortByA2Z();
                dialog.cancel();
            }
        });
        tvAlphabeticallyZ2A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.sortByZ2A();
                dialog.cancel();
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }


    /**
     * Show a toast in this view
     * @param s the display string of the toast
     */
    @Override
    public void showToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    /**
     * Show error screen with this message
     * @param errorMessage
     */
    @Override
    public void showErrorScreen(String errorMessage) {
        rvProjectList.setVisibility(View.GONE);
        rlFetchingData.setVisibility(View.GONE);
        rlError.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(errorMessage)){
            tvErrorMessage.setText(errorMessage);
        }
    }

    /**
     * Show fetch data screen when fetching data for the first time
     */
    @Override
    public void showDataFetchScreen() {
        rvProjectList.setVisibility(View.GONE);
        rlFetchingData.setVisibility(View.VISIBLE);
        rlError.setVisibility(View.GONE);
    }

    /**
     * show recycler view when data is successfully fetched
     */
    @Override
    public void showRecyclerView() {
        rvProjectList.setVisibility(View.VISIBLE);
        rlFetchingData.setVisibility(View.GONE);
        rlError.setVisibility(View.GONE);
    }
}
