package com.soj.m1kes.nits;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soj.m1kes.nits.adapters.recyclerview.models.AgentChildObject;


import static com.soj.m1kes.nits.util.ActionBarUtils.setupActionBar;

public class AgentDetails extends AppCompatActivity {

    private TextView address,officeID,helpDeskPin;
    private AgentChildObject agentChildObject;
    private Context context = this;
    private RelativeLayout rootAddress,rootOfficeID,rootHelpDeskPin,rootContacts;
    private final int PICK_CONTACT_REQUEST = 2003;
    private final int PICK_CONTACT_EDIT = 2005;
    private String phoneNo = "";
    EditText edt=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_details);

        if(getAgentDetails()) {

            initGui();
            setupActionBar(agentChildObject.getName(), this);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(agentChildObject.getName());
            setSupportActionBar(toolbar);
            if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            addFabButton();

        }else{
            startActivity(new Intent(context, MainActivity.class));
        }


    }





    private void initGui(){

        rootAddress = (RelativeLayout)findViewById(R.id.rootAddress);
        rootOfficeID = (RelativeLayout)findViewById(R.id.rootOfficeID);
        rootHelpDeskPin = (RelativeLayout)findViewById(R.id.rootHelpDeskPin);
        rootContacts = (RelativeLayout) findViewById(R.id.rootContacts);

        address = (TextView)findViewById(R.id.txtAddress);
        officeID = (TextView)findViewById(R.id.txtOfficeID);
        helpDeskPin = (TextView)findViewById(R.id.txtHelpDeskPin);


        address.setText(agentChildObject.getAddress());
        officeID.setText(agentChildObject.getOfficeID());
        helpDeskPin.setText(agentChildObject.getHelpDeskPin());


        setupOnlicks();
    }

    private void addFabButton(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    private void setupOnlicks(){

        rootAddress.setOnClickListener(v -> {
            Toast.makeText(context,"Clicked!",Toast.LENGTH_SHORT).show();
        });

        rootOfficeID.setOnClickListener(v -> {
            Toast.makeText(context,"Clicked!",Toast.LENGTH_SHORT).show();
        });

        rootHelpDeskPin.setOnClickListener(v -> {
            Toast.makeText(context,"Clicked!",Toast.LENGTH_SHORT).show();
        });

        rootContacts.setOnClickListener(v -> {
            Toast.makeText(context,"Clicked!",Toast.LENGTH_SHORT).show();
        });


    }


    private void pickContact(int OPTION) {
        Intent pickContactIntent = new Intent( Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI );
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, OPTION);
    }

    private void showCallIntent(String phoneNo){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+phoneNo));
            startActivity(intent);
    }

    private void sendEmailIntent(String email){

        String mailto = "mailto:"+email +
                "?cc=" + "alice@example.com" +
                "&subject=" + Uri.encode("") +
                "&body=" + Uri.encode("");
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));
        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
        }
    }

    private boolean getAgentDetails(){
        Bundle b = this.getIntent().getExtras();
        boolean notNull = true;
        try {
            if (b != null)
                agentChildObject = (AgentChildObject) b.getSerializable("agent_details");
            else
                notNull = false;
        }catch (Exception e){
            notNull = false;
            e.printStackTrace();
        }

        return notNull;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent ) {
        super.onActivityResult( requestCode, resultCode, intent );
        if (requestCode == PICK_CONTACT_REQUEST ) {
            if ( resultCode == RESULT_OK ) {
                Uri uri = intent.getData();
                // handle the picked phone number in here.
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if(cursor!=null) {
                    if (cursor.moveToFirst()) {
                        int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        phoneNo = cursor.getString(phoneIndex);
                        if(phoneNo!=null){
                            if(!phoneNo.equalsIgnoreCase("")){

                                //ADD Contact to agent

                            }
                        }
                    }
                    cursor.close();
                    System.out.println(phoneNo);
                    Toast.makeText(context, "Added a new Number: " + phoneNo, Toast.LENGTH_SHORT).show();
                }else{
                    System.out.println("Failed to get the number cursor is null");
                }
            }
        }else if (requestCode == PICK_CONTACT_EDIT){
            if ( resultCode == RESULT_OK ) {
                Uri uri = intent.getData();
                // handle the picked phone number in here.
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        phoneNo = cursor.getString(phoneIndex);

                    }

                }
                if (cursor != null)
                    cursor.close();

                Toast.makeText(context,phoneNo,Toast.LENGTH_SHORT).show();
                edt.setText(phoneNo);
            }
        }
    }

}
