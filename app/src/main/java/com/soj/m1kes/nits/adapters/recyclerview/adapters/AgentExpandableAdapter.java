package com.soj.m1kes.nits.adapters.recyclerview.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.soj.m1kes.nits.R;
import com.soj.m1kes.nits.adapters.recyclerview.callbacks.OnAgentItemSelected;
import com.soj.m1kes.nits.adapters.recyclerview.models.AgentChildObject;
import com.soj.m1kes.nits.adapters.recyclerview.models.AgentParentObject;

import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;



public class AgentExpandableAdapter extends ExpandableRecyclerAdapter<AgentExpandableAdapter.AgentParentHolder, AgentExpandableAdapter.AgentChildHolder> {


    private LayoutInflater mInflator;
    private Context context;
    private OnAgentItemSelected callback;
    private List<ParentObject> history;

    public AgentExpandableAdapter(Context context, List<ParentObject> parentItemList, OnAgentItemSelected callback) {
        super(context, parentItemList);
        history = parentItemList;
        this.context = context;
        mInflator = LayoutInflater.from(context);
        this.callback = callback;
    }

    @Override
    public AgentParentHolder onCreateParentViewHolder(ViewGroup viewGroup) {


        View view = mInflator.inflate(R.layout.list_agent_group, viewGroup, false);
        return new AgentParentHolder(view);
    }

    @Override
    public AgentChildHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflator.inflate(R.layout.list_agent_item, viewGroup, false);
        return new AgentChildHolder(view);
    }

    @Override
    public void onBindParentViewHolder(AgentParentHolder holder, int i, Object parentObject) {


        AgentParentObject agentParentObject = (AgentParentObject) parentObject;
        holder.txtName.setText(agentParentObject.getName());

    }

    @Override
    public void onBindChildViewHolder(AgentChildHolder holder, int i, Object childObject) {

        AgentChildObject agentChildObject = (AgentChildObject) childObject;
        holder.txtAddress.setText(agentChildObject.getAddress());
        holder.txtOfficeID.setText(agentChildObject.getOfficeID());
        holder.txtHelpDeskPin.setText(agentChildObject.getHelpDeskPin());
        holder.parentLayout.setOnClickListener(v -> callback.onClick(i,agentChildObject));
    }

    public List<ParentObject> filterData(String query){

        query = query.toLowerCase();
        ArrayList<ParentObject> newList = new ArrayList<ParentObject>();

        if(query.isEmpty()){
            mParentItemList = history;
        }
        else {
            for(ParentObject parentObject: mParentItemList){
                AgentParentObject agentParentObject = (AgentParentObject)parentObject;
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


    public class AgentParentHolder extends ParentViewHolder {

        public TextView txtName;

        public AgentParentHolder(View view) {
            super(view);
            this.txtName = (TextView) view.findViewById(R.id.txtName);
        }
    }

    public class AgentChildHolder extends ChildViewHolder {


        public TextView txtAddress;
        public TextView txtOfficeID;
        public TextView txtHelpDeskPin;
        private LinearLayout parentLayout;


        public AgentChildHolder(View itemView) {
            super(itemView);
            txtAddress = (TextView)itemView.findViewById(R.id.txtAddress);
            txtOfficeID = (TextView)itemView.findViewById(R.id.txtOfficeID);
            txtHelpDeskPin = (TextView)itemView.findViewById(R.id.txtHelpDeskPin);
            parentLayout = (LinearLayout)itemView.findViewById(R.id.parentLayout);
        }
    }


}
