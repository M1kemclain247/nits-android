package com.soj.m1kes.nits;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.soj.m1kes.nits.adapters.recyclerview.adapters.JobsExpandableAdapter;
import com.soj.m1kes.nits.adapters.recyclerview.callbacks.OnJobsItemSelected;
import com.soj.m1kes.nits.adapters.recyclerview.models.JobsChildObject;
import com.soj.m1kes.nits.adapters.recyclerview.models.JobsParentObject;
import com.soj.m1kes.nits.models.Agent;
import com.soj.m1kes.nits.models.Job;
import com.soj.m1kes.nits.service.JobsService;
import com.soj.m1kes.nits.service.callbacks.OnJobsDownloaded;
import com.soj.m1kes.nits.service.callbacks.OnPostUnsynced;
import com.soj.m1kes.nits.sqlite.adapters.AgentsAdapter;
import com.soj.m1kes.nits.sqlite.adapters.JobsAdapter;
import com.soj.m1kes.nits.util.ActionBarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class JobsActivity extends AppCompatActivity {



    private Context context = this;
    private EditText editJob;
    private Button btnSearchJob;
    private RecyclerView jobsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private JobsExpandableAdapter expandableAdapter;
    public List<ParentObject> parentObjects = new ArrayList<>();
    private TextView txtEmpty;
    private ProgressBar progressBarSync;


    private OnJobsItemSelected onJobsItemSelected = (position, job) -> System.out.println("Clicked!");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        ActionBarUtils.setupActionBar("Jobs",this);
        initGui();
        setOnClicks();
        setupRecyclerView();
        SyncJobs();
    }

    private void SyncJobs(){
        progressBarSync.setVisibility(View.VISIBLE);
        List<Job> unsycnedJobs = JobsAdapter.getAllUnsyncedJobs(context);
        if(unsycnedJobs!=null&&!unsycnedJobs.isEmpty()){
            new JobsService(context,onPostUnsynced,unsycnedJobs);
        }else {
            new JobsService(context,onJobsDownloaded);
        }
    }

    OnPostUnsynced onPostUnsynced = new OnPostUnsynced() {
        @Override
        public void success() {
            new JobsService(context,onJobsDownloaded);
        }

        @Override
        public void failed() {
            if(progressBarSync!=null)
            progressBarSync.setVisibility(View.GONE);
        }
    };

    private void initGui(){
        editJob = (EditText)findViewById(R.id.editAgentName);
        btnSearchJob = (Button)findViewById(R.id.btnSearchJob);
        jobsRecyclerView = (RecyclerView)findViewById(R.id.jobsRecyclerView);
        txtEmpty = (TextView)findViewById(R.id.txtEmpty);
        progressBarSync = (ProgressBar)findViewById(R.id.progressBarSync);
    }

    private void setOnClicks(){
        btnSearchJob.setOnClickListener(v -> System.out.println("Clicked Search Button"));

        editJob.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // filter your list from your input
                if(s.toString().equalsIgnoreCase("")){
                    setupRecyclerView();
                }else{
                    filter(s.toString());
                }
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

    }

    void filter(String text){
        List<Job> temp = new ArrayList();
        for(Job p: JobsAdapter.getAllJobs(context)){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE).matcher(p.getName()).find()){
                temp.add(p);
            }
        }
        //update recyclerview
        //setupRecyclerView.updateList(temp);
        setupRecyclerView(temp);
    }

    OnJobsDownloaded onJobsDownloaded = new OnJobsDownloaded() {
        @Override
        public void onDownloaded() {
            progressBarSync.setVisibility(View.GONE);
            setupRecyclerView();
            Toast.makeText(context,"Jobs Updated!",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed(){
            progressBarSync.setVisibility(View.GONE);
            Toast.makeText(context,"Failed to Refresh Jobs!",Toast.LENGTH_SHORT).show();
        }

    };

    private void setupRecyclerView() {
        List<Job> jobs = JobsAdapter.getAllJobs(context);
        if(jobs.isEmpty()){
            txtEmpty.setVisibility(View.VISIBLE);
            jobsRecyclerView.setVisibility(View.GONE);
        }else{
            txtEmpty.setVisibility(View.GONE);
            jobsRecyclerView.setVisibility(View.VISIBLE);
        }

        System.out.println("Number of Available Jobs: "+jobs.size());
        this.parentObjects = getAllParentObjects(jobs);
        this.expandableAdapter = new JobsExpandableAdapter(context, parentObjects, onJobsItemSelected);
        this.expandableAdapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
        this.expandableAdapter.setParentClickableViewAnimationDefaultDuration();
        this.expandableAdapter.setParentAndIconExpandOnClick(true);
        this.layoutManager = new LinearLayoutManager(context);
        this.jobsRecyclerView.setLayoutManager(this.layoutManager);
        this.jobsRecyclerView.setHasFixedSize(true);
        this.jobsRecyclerView.setAdapter(this.expandableAdapter);
        this.expandableAdapter.notifyDataSetChanged();
    }


    private void setupRecyclerView(List<Job> jobs) {
        if(jobs.isEmpty()){
            txtEmpty.setVisibility(View.VISIBLE);
            jobsRecyclerView.setVisibility(View.GONE);
        }else{
            txtEmpty.setVisibility(View.GONE);
            jobsRecyclerView.setVisibility(View.VISIBLE);
        }

        System.out.println("Number of Available Jobs: "+jobs.size());
        this.parentObjects = getAllParentObjects(jobs);
        this.expandableAdapter = new JobsExpandableAdapter(context, parentObjects, onJobsItemSelected);
        this.expandableAdapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
        this.expandableAdapter.setParentClickableViewAnimationDefaultDuration();
        this.expandableAdapter.setParentAndIconExpandOnClick(true);
        this.layoutManager = new LinearLayoutManager(context);
        this.jobsRecyclerView.setLayoutManager(this.layoutManager);
        this.jobsRecyclerView.setHasFixedSize(true);
        this.jobsRecyclerView.setAdapter(this.expandableAdapter);
        this.expandableAdapter.notifyDataSetChanged();
    }

    private List<ParentObject> getAllParentObjects(List<Job> jobs) {
        List<ParentObject> parentItems = new ArrayList<>();
        for(int i = 0;i< jobs.size();i++){
            Job j = jobs.get(i);
            JobsParentObject obj = new JobsParentObject(j.getName());
            ArrayList<Object> childList = new ArrayList<>();
            childList.add(new JobsChildObject(j.getId(), j.getName(), j.getDate(), j.getDescription(), j.getPriority(), j.getDetails()));
            obj.setChildObjectList(childList);
            parentItems.add(obj);
        }
        return parentItems;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_jobs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(context,MenuScreen.class));
            return true;
        }else if(id == R.id.idSort){
            //showChangeIPDialog();

        }else if(id == R.id.idAdd){
            //Add new Job
            Toast.makeText(context,"Add a new Job",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(context,AddJobActivity.class));

        }else if(id == R.id.actionSync){
            SyncJobs();
        }
        return super.onOptionsItemSelected(item);
    }


}
