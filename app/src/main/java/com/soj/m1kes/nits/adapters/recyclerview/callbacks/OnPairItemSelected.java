package com.soj.m1kes.nits.adapters.recyclerview.callbacks;

import com.soj.m1kes.nits.adapters.recyclerview.models.PairChildObject;
import com.soj.m1kes.nits.models.IPair;
import com.soj.m1kes.nits.models.Job;


public interface OnPairItemSelected {
    public void onClick(int position, PairChildObject iPair);
}
