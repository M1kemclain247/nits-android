package com.soj.m1kes.nits;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.soj.m1kes.nits.adapters.recyclerview.adapters.AgentExpandableAdapter;
import com.soj.m1kes.nits.adapters.recyclerview.callbacks.OnAgentItemSelected;
import com.soj.m1kes.nits.adapters.recyclerview.models.AgentChildObject;
import com.soj.m1kes.nits.adapters.recyclerview.models.AgentParentObject;
import com.soj.m1kes.nits.fragments.ChangeIPDialog;
import com.soj.m1kes.nits.models.Agent;
import com.soj.m1kes.nits.models.IPair;
import com.soj.m1kes.nits.prefmanagers.IpPrefManager;
import com.soj.m1kes.nits.service.AgentsService;
import com.soj.m1kes.nits.service.callbacks.OnAgentsDownloaded;
import com.soj.m1kes.nits.service.callbacks.OnAgentsSynced;
import com.soj.m1kes.nits.sqlite.adapters.AgentPairsAdapter;
import com.soj.m1kes.nits.sqlite.adapters.AgentsAdapter;
import com.soj.m1kes.nits.util.ActionBarUtils;
import com.soj.m1kes.nits.util.WebResources;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements ChangeIPDialog.ClickListener {

    private EditText editAgentName;
    private Button btnSearch;
    RecyclerView transactionRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AgentExpandableAdapter agentExpandableAdapter;
    public List<ParentObject> parentObjects = new ArrayList();
    private Context context = this;
    private TextView txtEmpty;
    private ProgressBar progressBarSync;

    OnAgentItemSelected onAgentItemSelected = (position, agent) -> {
        Intent intent = new Intent(context, AgentDetails.class);
        intent.putExtra("agent_details", agent);
        startActivity(intent);
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBarUtils.setupActionBar("Agents",this);
        setupGui();
        initListeners();


        if(savedInstanceState==null){
            List<Agent> agents =  AgentsAdapter.getAllAgents(MainActivity.this);
            if(!agents.isEmpty()){
                setupRecyclerView();
            }
            SyncAgents();

        }else{
            setupRecyclerView();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(MainActivity.this,MenuScreen.class));
            return true;
        }else if(id == R.id.item_Setttings){
            showChangeIPDialog();
        }else if(id == R.id.actionAddAgent){
            startActivity(new Intent(MainActivity.this,AddAgent.class));
        }else if(id == R.id.actionSyncAgents){
            SyncAgents();
        }
        return super.onOptionsItemSelected(item);
    }




    public void showChangeIPDialog() {
        ChangeIPDialog dialog = new ChangeIPDialog();
        dialog.show(getFragmentManager(), "ChangeIPDialog");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setupRecyclerView() {
        Context context = MainActivity.this;
        List<Agent> agents = AgentsAdapter.getAllAgents(context);
        if(agents.isEmpty()){
            txtEmpty.setVisibility(View.VISIBLE);
            transactionRecyclerView.setVisibility(View.GONE);
        }else{
            txtEmpty.setVisibility(View.GONE);
            transactionRecyclerView.setVisibility(View.VISIBLE);
        }
        this.parentObjects = getAllParentObjects(agents);
        this.agentExpandableAdapter = new AgentExpandableAdapter(context, parentObjects, onAgentItemSelected);
        this.agentExpandableAdapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
        this.agentExpandableAdapter.setParentClickableViewAnimationDefaultDuration();
        this.agentExpandableAdapter.setParentAndIconExpandOnClick(true);
        this.layoutManager = new LinearLayoutManager(context);
        this.transactionRecyclerView.setLayoutManager(this.layoutManager);
        this.transactionRecyclerView.setHasFixedSize(true);
        this.transactionRecyclerView.setAdapter(this.agentExpandableAdapter);
        this.agentExpandableAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView(List<Agent> agents) {
        Context context = MainActivity.this;
        if(agents.isEmpty()){
            txtEmpty.setVisibility(View.VISIBLE);
            transactionRecyclerView.setVisibility(View.GONE);
        }else{
            txtEmpty.setVisibility(View.GONE);
            transactionRecyclerView.setVisibility(View.VISIBLE);
        }
        this.parentObjects = getAllParentObjects(agents);
        this.agentExpandableAdapter = new AgentExpandableAdapter(context, parentObjects, onAgentItemSelected);
        this.agentExpandableAdapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
        this.agentExpandableAdapter.setParentClickableViewAnimationDefaultDuration();
        this.agentExpandableAdapter.setParentAndIconExpandOnClick(true);
        this.layoutManager = new LinearLayoutManager(context);
        this.transactionRecyclerView.setLayoutManager(this.layoutManager);
        this.transactionRecyclerView.setHasFixedSize(true);
        this.transactionRecyclerView.setAdapter(this.agentExpandableAdapter);
        this.agentExpandableAdapter.notifyDataSetChanged();
    }


    private List<ParentObject> getAllParentObjects(List<Agent> agents) {
        List<ParentObject> parentItems = new ArrayList<>();
        for(int i = 0;i< agents.size();i++){
            Agent a = agents.get(i);
            AgentParentObject obj = new AgentParentObject(a.getName());
            ArrayList<Object> childList = new ArrayList<>();
            childList.add(new AgentChildObject(a.getId(), a.getName(), a.getAddress(), a.getOfficeID(), a.getHelpDeskPin(), a.getGroup(),a.getContacts()));
            obj.setChildObjectList(childList);
            parentItems.add(obj);
        }
        return parentItems;
    }



    private void setupGui(){
        editAgentName = (EditText)findViewById(R.id.editAgentName);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        transactionRecyclerView = (RecyclerView) findViewById(R.id.agentsRecyclerView);
        txtEmpty = (TextView)findViewById(R.id.txtEmpty);
        progressBarSync = (ProgressBar)findViewById(R.id.progressBarSync);
    }


    public void initListeners(){

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
                if(s.toString().equalsIgnoreCase("")){
                    setupRecyclerView();
                }else{
                    filter(s.toString());
                }
            }
        });
    }

    void filter(String text){
        List<Agent> temp = new ArrayList();
        for(Agent p: AgentsAdapter.getAllAgents(context)){
            if(Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE).matcher(p.getName()).find()){
                temp.add(p);
            }
        }
        setupRecyclerView(temp);
    }

    @Override
    public void onChangeClick(DialogFragment dialog, EditText editIp) {
        String ipAddress =  editIp.getText().toString();
        IpPrefManager.setIpAddress(ipAddress,MainActivity.this);
        Toast.makeText(MainActivity.this,"Ip Address Updated!"+ipAddress,Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        SyncAgents();
    }

    @Override
    public void onCancelClick(DialogFragment dialog) {
            dialog.dismiss();
    }


    private void SyncAgents(){
        progressBarSync.setVisibility(View.VISIBLE);
        List<Agent> unsyncedAgents = AgentsAdapter.getUnsyncedAgents(MainActivity.this);
        if(unsyncedAgents!=null&&!unsyncedAgents.isEmpty()){
            new AgentsService(context,onAgentsSynced,unsyncedAgents).startPostSync();
        }else {
            new AgentsService(context,onAgentsDownloaded).startDownload();
        }
    }

    OnAgentsDownloaded onAgentsDownloaded = new OnAgentsDownloaded() {
        @Override
        public void onDownloadComplete() {
            progressBarSync.setVisibility(View.GONE);
            setupRecyclerView();
            Toast.makeText(context,"Agents Updated!",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDownloadFailed() {
            progressBarSync.setVisibility(View.GONE);
            Toast.makeText(context,"Failed to Refresh Agents!",Toast.LENGTH_SHORT).show();
        }
    };


    OnAgentsSynced onAgentsSynced = new OnAgentsSynced() {
        @Override
        public void onSyncCompleted() {
            new AgentsService(context,onAgentsDownloaded).startDownload();
        }

        @Override
        public void onSyncCompleted(String id) {
            new AgentsService(context,onAgentsDownloaded).startDownload();
        }

        @Override
        public void onSyncFailed() {
            if(progressBarSync!=null)
            progressBarSync.setVisibility(View.GONE);
        }
    };
}
