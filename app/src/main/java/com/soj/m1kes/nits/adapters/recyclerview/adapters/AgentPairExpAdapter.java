package com.soj.m1kes.nits.adapters.recyclerview.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.soj.m1kes.nits.R;
import com.soj.m1kes.nits.adapters.recyclerview.callbacks.OnPairItemSelected;
import com.soj.m1kes.nits.adapters.recyclerview.models.PairChildObject;
import com.soj.m1kes.nits.adapters.recyclerview.models.PairParentObject;

import java.util.List;



public class AgentPairExpAdapter extends ExpandableRecyclerAdapter<AgentPairExpAdapter.AgentPairParentHolder, AgentPairExpAdapter.AgentPairChildHolder> {

    private LayoutInflater mInflator;
    private Context context;
    private OnPairItemSelected callback;
    private List<ParentObject> parentItemList;

    public AgentPairExpAdapter(Context context, List<ParentObject> parentItemList, OnPairItemSelected callback) {
        super(context, parentItemList);
        this.context = context;
        mInflator = LayoutInflater.from(context);
        this.callback = callback;
        this.parentItemList= parentItemList;
    }

    @Override
    public AgentPairParentHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflator.inflate(R.layout.list_agent_group, viewGroup, false);
        return new AgentPairParentHolder(view);
    }

    @Override
    public AgentPairChildHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflator.inflate(R.layout.list_pair_item, viewGroup, false);
        return new AgentPairChildHolder(view);
    }

    @Override
    public void onBindParentViewHolder(AgentPairParentHolder holder, int i, Object parentObject) {
        PairParentObject agentParentObject = (PairParentObject) parentObject;
        holder.txtName.setText(agentParentObject.getName());
    }

    @Override
    public void onBindChildViewHolder(AgentPairChildHolder holder, int i, Object childObject) {
        PairChildObject agentChildObject = (PairChildObject) childObject;
        holder.txtPairIP.setText(agentChildObject.getIp());
        holder.txtModel.setText(agentChildObject.getModel());

        String[] modes =  agentChildObject.getConnectionModes().split(",");
        StringBuilder sb = new StringBuilder();
        int start = 0;
        for(String s : modes){
            start++;
            if(start!=1)
                sb.append("/");
            sb.append(s);
        }

        holder.txtConnectionModes.setText(sb.toString());
        holder.txtPort.setText(agentChildObject.getPort());

        holder.parentLayoutPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(i,agentChildObject);
            }
        });
    }

    public void updateList(List<ParentObject> list){
        parentItemList = list;
        notifyDataSetChanged();
    }

    public class AgentPairParentHolder extends ParentViewHolder {

        public TextView txtName;

        public AgentPairParentHolder(View view) {
            super(view);
            this.txtName = (TextView) view.findViewById(R.id.txtName);
        }
    }

    public class AgentPairChildHolder extends ChildViewHolder {

        public TextView txtPairIP;
        public TextView txtModel;
        public TextView txtConnectionModes;
        public TextView txtPort;
        private LinearLayout parentLayoutPair;

        public AgentPairChildHolder(View itemView) {
            super(itemView);
            txtPairIP = (TextView)itemView.findViewById(R.id.txtPairIP);
            parentLayoutPair = (LinearLayout)itemView.findViewById(R.id.parentLayoutPair);
            txtModel = (TextView)itemView.findViewById(R.id.txtModel);
            txtConnectionModes = (TextView)itemView.findViewById(R.id.txtConnectionModes);
            txtPort = (TextView)itemView.findViewById(R.id.txtPort);
        }
    }
}

