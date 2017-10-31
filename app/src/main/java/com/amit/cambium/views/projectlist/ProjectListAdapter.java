package com.amit.cambium.views.projectlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amit.cambium.R;
import com.amit.cambium.models.Project;
import com.amit.cambium.utils.DisplayUtils;
import com.amit.cambium.utils.ItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amit Barjatya on 10/29/17.
 */

public class ProjectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ProjListAdapter";

    /**
     * List of projects being managed by this adapter
     */
    List<Project> projects;
    /**
     * Notify the consumer about item clicks
     */
    ItemClickListener clickListener;

    public ProjectListAdapter(List<Project> projects, ItemClickListener listener) {
        this.projects = projects;
        this.clickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_list, parent, false);
        return new ProjectItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        ProjectItemViewHolder holder = (ProjectItemViewHolder) holder1;
        final Project project = projects.get(position);
        holder.projectOwner.setText(String.format("Project by %s", project.getBy()));
        holder.projectTitle.setText(project.getTitle());
        holder.blurb.setText(project.getBlurb());
        String amt = DisplayUtils.currencyFormat(project.getCurrency(), project.getAmtPledged());
        holder.amtPledged.setText(String.format("Amount Pledged: %s", amt));
        holder.backers.setText("Backers: " + project.getNumBackers());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(project);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (projects == null)
            return 0;

        return projects.size();
    }

    /**
     * Viewholder for a project item
     */
    public class ProjectItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.owner)
        TextView projectOwner;

        @BindView(R.id.title)
        TextView projectTitle;

        @BindView(R.id.blurb)
        TextView blurb;

        @BindView(R.id.amtPledged)
        TextView amtPledged;

        @BindView(R.id.backers)
        TextView backers;

        public ProjectItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
