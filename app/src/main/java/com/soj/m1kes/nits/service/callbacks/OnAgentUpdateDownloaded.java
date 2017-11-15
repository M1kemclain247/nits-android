package com.soj.m1kes.nits.service.callbacks;

import com.soj.m1kes.nits.models.Agent;

/**
 * Created by m1kes on 8/31/2017.
 */

public interface OnAgentUpdateDownloaded {

    public void onDownloaded(Agent agent);
    public void onFailedDownload();

}
