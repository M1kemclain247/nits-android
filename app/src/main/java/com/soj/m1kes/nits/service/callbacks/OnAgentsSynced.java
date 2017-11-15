package com.soj.m1kes.nits.service.callbacks;

/**
 * Created by Michael on 7/27/2017.
 */

public interface OnAgentsSynced {
    void onSyncCompleted();
    void onSyncCompleted(String id);
    void onSyncFailed();
}
