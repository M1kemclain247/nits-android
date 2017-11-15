package com.soj.m1kes.nits;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.soj.m1kes.nits.adapters.spinners.AgentGroupAdapter;
import com.soj.m1kes.nits.models.Agent;
import com.soj.m1kes.nits.models.AgentGroup;
import com.soj.m1kes.nits.service.AgentsService;
import com.soj.m1kes.nits.service.callbacks.OnAgentsSynced;
import com.soj.m1kes.nits.sqlite.adapters.AgentGroupsAdapter;
import com.soj.m1kes.nits.sqlite.adapters.AgentsAdapter;
import com.soj.m1kes.nits.util.ActionBarUtils;

import java.util.List;

public class AddAgent extends AppCompatActivity {


    private EditText editNameAgent,editAddressAgent,editOfficeIDAgent,editHelpDeskPinAgent,editEmailAgent;
    private Button btnAddAgent,btnCancelAgent;
    private Context context = this;
    private Spinner spinner_groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agent);
        ActionBarUtils.setupActionBar("Add Agent",this);
        initGui();
        setOnClicks();
        SetupSpinner();
    }


    private void initGui(){
        editNameAgent = (EditText)findViewById(R.id.editNameAgent);
        editAddressAgent = (EditText)findViewById(R.id.editAddressAgent);
        editOfficeIDAgent = (EditText)findViewById(R.id.editOfficeIDAgent);
        editHelpDeskPinAgent = (EditText)findViewById(R.id.editHelpDeskPinAgent);
        editEmailAgent = (EditText)findViewById(R.id.editEmailAgent);
        btnAddAgent = (Button)findViewById(R.id.btnAddAgent);
        btnCancelAgent = (Button)findViewById(R.id.btnCancelAgent);
        spinner_groups = (Spinner)findViewById(R.id.spinner_groups);
    }

    private void SetupSpinner(){
        List<AgentGroup> agentGroupList = AgentGroupsAdapter.getAllAgentGroups(context);
        AgentGroupAdapter adapter  = new AgentGroupAdapter(context,agentGroupList);
        spinner_groups.setAdapter(adapter);
    }

    private AgentGroup getCurrentSelectedOption(){
       return (AgentGroup)spinner_groups.getSelectedItem();
    }

    private void setOnClicks(){

        btnAddAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate(new EditText[]{editNameAgent,editAddressAgent,editOfficeIDAgent,editHelpDeskPinAgent,editEmailAgent})) {

                    Agent agent = new Agent();
                    agent.setName(editNameAgent.getText().toString());
                    agent.setAddress(editAddressAgent.getText().toString());
                    agent.setOfficeID(editOfficeIDAgent.getText().toString());
                    agent.setHelpDeskPin(editHelpDeskPinAgent.getText().toString());
                    agent.setSynced(false);
                    agent.setGroup(getCurrentSelectedOption());
                    AgentsAdapter.addAgent(agent,context);
                    new AgentsService(context, onAgentsSynced, AgentsAdapter.getUnsyncedAgents(context)).startPostSync();
                    startActivity(new Intent(context,MainActivity.class));
                }
            }
        });

        btnCancelAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Cancelled",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(context,MainActivity.class));
            }
        });

    }

    OnAgentsSynced onAgentsSynced = new OnAgentsSynced() {
        @Override
        public void onSyncCompleted() {
            Toast.makeText(context,"Agents Synced with Server",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSyncCompleted(String id) {
            Toast.makeText(context,"Agents Synced with Server",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSyncFailed() {
            Toast.makeText(context,"Failed Syncing Agents with Server",Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(context, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validate(EditText[] fields) {
        for (int i = 0; i < fields.length; i++) {
            EditText currentField = fields[i];
            if (currentField.getText().toString().length() <= 0) {
                currentField.setError("Required Field");
                return false;
            }
        }
        return true;
    }



}
