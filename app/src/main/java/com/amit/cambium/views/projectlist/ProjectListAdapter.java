package com.amit.cambium.views.projectlist;

import android.content.Context;
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
    private List<Project> projects;
    /**
     * Notify the consumer about item clicks
     */
    private ItemClickListener clickListener;

    /**
     * Context in which adapter is running
     */
    private Context mContext;

    ProjectListAdapter(List<Project> projects,
                              ItemClickListener listener, Context context) {
        this.projects = projects;
        this.clickListener = listener;
        this.mContext = context;
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
        holder.projectOwner.setText(String.format(mContext.getString(R.string.all_project_by_placeholder), project.getBy()));
        holder.projectTitle.setText(project.getTitle());
        holder.blurb.setText(project.getBlurb());
        String amt = DisplayUtils.currencyFormat(project.getCurrency(), project.getAmtPledged());
        holder.amtPledged.setText(String.format(mContext.getString(R.string.all_amount_pledged_placeholder), amt));
        holder.backers.setText(String.format(mContext.getString(R.string.all_backers_placeholder), project.getNumBackers()));
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
    class ProjectItemViewHolder extends RecyclerView.ViewHolder {

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

        ProjectItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
