package com.soj.m1kes.nits;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.soj.m1kes.nits.adapters.recyclerview.adapters.AgentExpandableAdapter;
import com.soj.m1kes.nits.adapters.recyclerview.adapters.AgentPairExpAdapter;
import com.soj.m1kes.nits.adapters.recyclerview.callbacks.OnPairItemSelected;
import com.soj.m1kes.nits.adapters.recyclerview.models.AgentChildObject;
import com.soj.m1kes.nits.adapters.recyclerview.models.AgentParentObject;
import com.soj.m1kes.nits.adapters.recyclerview.models.PairChildObject;
import com.soj.m1kes.nits.adapters.recyclerview.models.PairParentObject;
import com.soj.m1kes.nits.models.Agent;
import com.soj.m1kes.nits.models.IPair;
import com.soj.m1kes.nits.service.AgentPairsService;
import com.soj.m1kes.nits.service.AgentsService;
import com.soj.m1kes.nits.service.callbacks.OnAgentPairsDownloaded;
import com.soj.m1kes.nits.service.callbacks.OnAgentPairsSynced;
import com.soj.m1kes.nits.sqlite.adapters.AgentPairsAdapter;
import com.soj.m1kes.nits.sqlite.adapters.AgentsAdapter;
import com.soj.m1kes.nits.util.ActionBarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PairedAgents extends AppCompatActivity {

    private Context context = this;
    private EditText editAgentName;
    private Button btnSearch;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AgentPairExpAdapter agentExpandableAdapter;
    public List<ParentObject> parentObjects = new ArrayList();
    private TextView txtEmpty;
    private ProgressBar progressBarSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_agents);
        ActionBarUtils.setupActionBar("Agent IPS",this);
        setupGui();

        if(savedInstanceState==null){
            List<Agent> agents =  AgentsAdapter.getAllAgents(context);
            if(!agents.isEmpty()){
                setupRecyclerView();
            }
            SyncPairs();

        }else{
            setupRecyclerView();
        }
    }


    public void SyncPairs(){
        progressBarSync.setVisibility(View.VISIBLE);
        List<IPair> unsyncedAgents = AgentPairsAdapter.getUnsyncedPairs(context);
        if(unsyncedAgents!=null&&!unsyncedAgents.isEmpty()){
            new AgentPairsService(context,onAgentPairsSynced).StartPostUnSynced();
        }else {
            new AgentPairsService(onAgentPairsDownloaded, context).StartSync();
        }
    }

    private void setupRecyclerView() {
        Context context = this;
        List<IPair> agents = AgentPairsAdapter.getAllAgentIPS(context);
        if(agents.isEmpty()){
            txtEmpty.setText("No Agent Details");
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            txtEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        this.parentObjects = getAllParentObjects(agents);
        this.agentExpandableAdapter = new AgentPairExpAdapter(context, parentObjects, onAgentItemSelected);
        this.agentExpandableAdapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
        this.agentExpandableAdapter.setParentClickableViewAnimationDefaultDuration();
        this.agentExpandableAdapter.setParentAndIconExpandOnClick(true);
        this.layoutManager = new LinearLayoutManager(context);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setAdapter(this.agentExpandableAdapter);
        this.agentExpandableAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView(List<IPair> agents) {
        Context context = this;
        if(agents.isEmpty()){
            txtEmpty.setText("No Search Results Found");
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            txtEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        this.parentObjects = getAllParentObjects(agents);
        this.agentExpandableAdapter = new AgentPairExpAdapter(context, parentObjects, onAgentItemSelected);
        this.agentExpandableAdapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
        this.agentExpandableAdapter.setParentClickableViewAnimationDefaultDuration();
        this.agentExpandableAdapter.setParentAndIconExpandOnClick(true);
        this.layoutManager = new LinearLayoutManager(context);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setAdapter(this.agentExpandableAdapter);
        this.agentExpandableAdapter.notifyDataSetChanged();
    }

    OnPairItemSelected onAgentItemSelected = new OnPairItemSelected() {
        @Override
        public void onClick(int position, PairChildObject iPair) {

        }
    };

    OnAgentPairsSynced onAgentPairsSynced = new OnAgentPairsSynced() {
        @Override
        public void onSyncComplete() {
            new AgentPairsService(onAgentPairsDownloaded, context).StartSync();
        }

        @Override
        public void onSyncFailed() {
            progressBarSync.setVisibility(View.GONE);
            Toast.makeText(context,"Failed to connect to server",Toast.LENGTH_SHORT).show();
        }
    };


    private List<ParentObject> getAllParentObjects(List<IPair> agents) {
        List<ParentObject> parentItems = new ArrayList<>();
        for(int i = 0;i< agents.size();i++){
            IPair a = agents.get(i);
            PairParentObject obj = new PairParentObject(a.getName());
            ArrayList<Object> childList = new ArrayList<>();
            childList.add(new PairChildObject(a.getId(), a.getName(), a.getIp(),a.getModel(),a.getConnectionModes(),a.getPort()));
            obj.setChildObjectList(childList);
            parentItems.add(obj);
        }
        return parentItems;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(context,OthersActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupGui(){
        editAgentName = (EditText)findViewById(R.id.editAgentPairName);
        btnSearch = (Button)findViewById(R.id.btnSearchPair);
        recyclerView = (RecyclerView) findViewById(R.id.agentsPairRecyclerView);
        txtEmpty = (TextView)findViewById(R.id.txtEmptyPair);
        progressBarSync = (ProgressBar)findViewById(R.id.progressBarSyncPair);



        editAgentName.addTextChangedListener(new TextWatcher() {
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
        List<IPair> temp = new ArrayList();
        for(IPair p: AgentPairsAdapter.getAllAgentIPS(context)){
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


    private OnAgentPairsDownloaded onAgentPairsDownloaded = new OnAgentPairsDownloaded() {
        @Override
        public void onDownloaded() {
            progressBarSync.setVisibility(View.GONE);
            setupRecyclerView();
            Toast.makeText(context,"Downloaded Pairs",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onFailed() {
            progressBarSync.setVisibility(View.GONE);
            Toast.makeText(context,"Failed to update pairs",Toast.LENGTH_SHORT).show();
        }
    };


}
