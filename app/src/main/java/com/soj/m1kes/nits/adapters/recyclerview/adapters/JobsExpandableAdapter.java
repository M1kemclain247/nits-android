package com.soj.m1kes.nits.adapters.recyclerview.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.soj.m1kes.nits.R;
import com.soj.m1kes.nits.adapters.recyclerview.callbacks.OnAgentItemSelected;
import com.soj.m1kes.nits.adapters.recyclerview.callbacks.OnJobsItemSelected;
import com.soj.m1kes.nits.adapters.recyclerview.models.JobsChildObject;
import com.soj.m1kes.nits.adapters.recyclerview.models.JobsParentObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m1kes on 7/20/2017.
 */

public class JobsExpandableAdapter extends ExpandableRecyclerAdapter<JobsExpandableAdapter.JobParentHolder, JobsExpandableAdapter.JobChildHolder> {


    private LayoutInflater mInflator;
    private Context context;
    private OnJobsItemSelected callback;
    private List<ParentObject> history;

    public JobsExpandableAdapter(Context context, List<ParentObject> parentItemList, OnJobsItemSelected callback) {
        super(context, parentItemList);
        history = parentItemList;
        this.context = context;
        mInflator = LayoutInflater.from(context);
        this.callback = callback;
    }



    @Override
    public JobParentHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflator.inflate(R.layout.list_job_group_item, viewGroup, false);
        return new JobParentHolder(view);
    }

    @Override
    public JobChildHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflator.inflate(R.layout.list_job_item, viewGroup, false);
        return new JobChildHolder(view);
    }

    @Override
    public void onBindParentViewHolder(JobParentHolder holder, int i, Object parentObject) {
        JobsParentObject jobsParentObject = (JobsParentObject) parentObject;
        holder.txtName.setText(jobsParentObject.getName());
    }

    @Override
    public void onBindChildViewHolder(JobChildHolder holder, int i, Object childObject) {
        JobsChildObject jobsChildObject = (JobsChildObject) childObject;
        holder.txtDate.setText(jobsChildObject.getDate());
        holder.txtDescription.setText(jobsChildObject.getDescription());
        holder.txtPriority.setText(jobsChildObject.getPriority());
        holder.txtDetails.setText(jobsChildObject.getDetails());
    }

    public List<ParentObject> filterData(String query){

        query = query.toLowerCase();
        ArrayList<ParentObject> newList = new ArrayList<ParentObject>();

        if(query.isEmpty()){
            mParentItemList = history;
        }
        else {
            for(ParentObject parentObject: mParentItemList){
                JobsParentObject agentParentObject = (JobsParentObject)parentObject;
                if(agentParentObject.getName().toLowerCase().contains(query)){
                    System.out.println("Found an item : "+agentParentObject.getName());
                    newList.add(agentParentObject);
                }
            }
        }
        mParentItemList = newList;
        notifyDataSetChanged();
        return newList;
    }


    public class JobParentHolder extends ParentViewHolder {

        public TextView txtName;

        public JobParentHolder(View view) {
            super(view);
            this.txtName = (TextView) view.findViewById(R.id.txtJobName);
        }
    }

    public class JobChildHolder extends ChildViewHolder {

        public TextView txtDate;
        public TextView txtDescription;
        public TextView txtPriority;
        public TextView txtDetails;

        public JobChildHolder(View itemView) {
            super(itemView);
            txtDate = (TextView)itemView.findViewById(R.id.txtDate);
            txtDescription = (TextView)itemView.findViewById(R.id.txtDescription);
            txtPriority = (TextView)itemView.findViewById(R.id.txtPriority);
            txtDetails = (TextView)itemView.findViewById(R.id.txtDetails);
        }
    }
}
