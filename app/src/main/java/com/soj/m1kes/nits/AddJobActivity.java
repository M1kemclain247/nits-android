package com.soj.m1kes.nits;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.soj.m1kes.nits.models.Job;
import com.soj.m1kes.nits.service.JobsService;
import com.soj.m1kes.nits.service.callbacks.OnJobPosted;
import com.soj.m1kes.nits.util.ActionBarUtils;

public class AddJobActivity extends AppCompatActivity {

    private Context context = this;
    private Button btnAddJob,btnCancelJob;
    private EditText editName,editDate,editDescription,editPriority,editDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        ActionBarUtils.setupActionBar("Add Job",this);
        initGui();
        setupOnClicks();



    }

    private void initGui(){
        btnAddJob = (Button)findViewById(R.id.btnAddJob);
        btnCancelJob = (Button)findViewById(R.id.btnCancelJob);
        editName = (EditText)findViewById(R.id.editName);
        editDate = (EditText)findViewById(R.id.editDate);
        editDescription = (EditText)findViewById(R.id.editDescription);
        editPriority = (EditText)findViewById(R.id.editPriority);
        editDetails = (EditText)findViewById(R.id.editDetails);
    }


    private void setupOnClicks(){

        btnAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate(new EditText[]{editName,editDate,editDescription,editPriority,editDetails})) {
                    Job j = new Job();
                    j.setName(editName.getText().toString());
                    j.setDate(editDate.getText().toString());
                    j.setDescription(editDescription.getText().toString());
                    j.setPriority(editPriority.getText().toString());
                    j.setDetails(editDetails.getText().toString());
                    j.setSynced(false);
                    new JobsService(context, onJobPosted).PostJob(j);
                    startActivity(new Intent(context,JobsActivity.class));
                }
            }
        });

        btnCancelJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Toast.makeText(context,"Cancelled",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context,JobsActivity.class));
            }
        });

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

    OnJobPosted onJobPosted = new OnJobPosted() {
        @Override
        public void onPosted() {
            Toast.makeText(context,"Job Posted!",Toast.LENGTH_SHORT).show();
            //then do nothing else but sync Jobs
        }

        @Override
        public void onPostFailed() {
            Toast.makeText(context,"Failed to Post Job!",Toast.LENGTH_SHORT).show();
        }
    };



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(context,JobsActivity.class));
            return true;
        }else if(id == R.id.idSort){
            //showChangeIPDialog();

        }else if(id == R.id.idAdd){
            //Add new Job
            Toast.makeText(context,"Add a new Job",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(context,AddJobActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }



}
