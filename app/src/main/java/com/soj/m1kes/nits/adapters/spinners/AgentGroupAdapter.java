package com.soj.m1kes.nits.adapters.spinners;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.soj.m1kes.nits.models.AgentGroup;

import java.util.List;


public class AgentGroupAdapter extends BaseAdapter implements SpinnerAdapter {

    private Context context;
    private List<AgentGroup> agentGroups;

    public AgentGroupAdapter(Context context, List<AgentGroup> agentGroups){
        this.context =  context;
        this.agentGroups = agentGroups;
    }



    @Override
    public int getCount() {
        return agentGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return agentGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(context);
        txt.setGravity(Gravity.LEFT);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
        txt.setText(agentGroups.get(position).getGroupName());
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(context);
        txt.setPadding(48, 48, 48, 48);
        txt.setTextSize(18);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setGravity(Gravity.CENTER_HORIZONTAL);
        txt.setText(agentGroups.get(position).getGroupName());
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

}