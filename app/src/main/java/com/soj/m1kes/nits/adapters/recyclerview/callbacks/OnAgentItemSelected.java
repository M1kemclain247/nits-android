package com.soj.m1kes.nits.adapters.recyclerview.callbacks;

import com.soj.m1kes.nits.adapters.recyclerview.models.AgentChildObject;
import com.soj.m1kes.nits.models.Agent;



public interface OnAgentItemSelected {
    public void onClick(int position,AgentChildObject agent);
}
