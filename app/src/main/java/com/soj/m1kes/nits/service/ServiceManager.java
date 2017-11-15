package com.soj.m1kes.nits.service;

import android.content.Context;
import android.widget.Toast;

import com.soj.m1kes.nits.MainActivity;
import com.soj.m1kes.nits.models.Agent;
import com.soj.m1kes.nits.models.Job;
import com.soj.m1kes.nits.service.callbacks.OnAgentGroupsDownloaded;
import com.soj.m1kes.nits.service.callbacks.OnAgentsDownloaded;
import com.soj.m1kes.nits.service.callbacks.OnAgentsSynced;
import com.soj.m1kes.nits.service.callbacks.OnJobsDownloaded;
import com.soj.m1kes.nits.service.callbacks.OnPostUnsynced;
import com.soj.m1kes.nits.sqlite.adapters.AgentGroupsAdapter;
import com.soj.m1kes.nits.sqlite.adapters.AgentsAdapter;
import com.soj.m1kes.nits.sqlite.adapters.JobsAdapter;

import java.util.List;

/**
 * Created by Michael on 7/31/2017.
 */

public class ServiceManager {

    private Context context;

    public ServiceManager(){

    }

    public ServiceManager(Context context){
        this.context = context;
    }



    public void Sync() {
        if (AgentsAdapter.getAllAgents(context).size() == 0 &&
                AgentGroupsAdapter.getAllAgentGroups(context).size() == 0 &&
                JobsAdapter.getAllJobs(context).size() == 0) {
            doDumbUpdate();
        }else{
            StartSmartSync();
        }
    }

    private void StartSmartSync(){

        int NUM_TRIES = 1;
        boolean isSynced = false;

        while(NUM_TRIES<=3) {

            boolean jobsSynced = false;
            boolean agentsSynced = false;

            List<Job> unsycnedJobs = JobsAdapter.getAllUnsyncedJobs(context);
            if (unsycnedJobs != null && !unsycnedJobs.isEmpty()) {
                new JobsService(context, new OnPostUnsynced() {
                    @Override
                    public void success() {
                        System.out.println("Jobs All Posted");
                    }

                    @Override
                    public void failed() {
                        System.out.println("Failed to Post some Jobs");
                    }
                }, unsycnedJobs);
            } else {
                jobsSynced = true;
            }

            List<Agent> unsyncedAgents = AgentsAdapter.getUnsyncedAgents(context);
            if (unsyncedAgents != null && !unsyncedAgents.isEmpty()) {
                new AgentsService(context, new OnAgentsSynced() {
                    @Override
                    public void onSyncCompleted() {
                        System.out.println("Agents All Posted");
                    }
                    @Override
                    public void onSyncCompleted(String id) {
                        System.out.println("Agents All Posted");
                    }
                    @Override
                    public void onSyncFailed() {
                        System.out.println("Failed to Post some Agents");
                    }
                }, unsyncedAgents).startPostSync();
            } else {
                agentsSynced = true;
            }

            if(jobsSynced&&agentsSynced){
                isSynced = true;
                break;
            }
            NUM_TRIES++;
        }

        if(isSynced)
        doDumbUpdate();

    }

    private void doDumbUpdate(){

        //if(All the DB's are empty then sync the following to get everything.)
        new AgentGroupService(context, new OnAgentGroupsDownloaded() {
            @Override
            public void onDownloaded() {
                new AgentsService(context, new OnAgentsDownloaded() {
                    @Override
                    public void onDownloadComplete() {
                        new JobsService(context, new OnJobsDownloaded() {
                            @Override
                            public void onDownloaded() {

                            }

                            @Override
                            public void onFailed() {

                            }
                        });
                    }

                    @Override
                    public void onDownloadFailed() {

                    }
                }).startDownload();
            }

            @Override
            public void onDownloadFailed() {

            }
        }).SyncGroups();

    }
}
