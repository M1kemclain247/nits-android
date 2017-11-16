package com.soj.m1kes.nits;

import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soj.m1kes.nits.adapters.recyclerview.models.AgentChildObject;
import com.soj.m1kes.nits.fragments.dialogs.AddContactDialog;
import com.soj.m1kes.nits.models.AgentContact;
import com.soj.m1kes.nits.service.AgentContactsService;
import com.soj.m1kes.nits.service.objects.ServiceCallback;
import com.soj.m1kes.nits.service.objects.ServiceManager;
import com.soj.m1kes.nits.sqlite.adapters.AgentContactsAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.soj.m1kes.nits.util.ActionBarUtils.setupActionBar;

public class AgentDetails extends AppCompatActivity implements AddContactDialog.ClickListener{

    private TextView address,officeID,helpDeskPin;
    private AgentChildObject agentChildObject;
    private Context context = this;
    private AgentContactsService service = null;
    private RelativeLayout rootAddress,rootOfficeID,rootHelpDeskPin,rootContacts,rootAddContact;
    private final int PICK_CONTACT_REQUEST = 2003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_details);

        if(getAgentDetails()) {

            initGui();
            initListeners();

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
        rootAddContact = (RelativeLayout)findViewById(R.id.rootAddContact);


        address = (TextView)findViewById(R.id.txtAddress);
        officeID = (TextView)findViewById(R.id.txtOfficeID);
        helpDeskPin = (TextView)findViewById(R.id.txtHelpDeskPin);


        address.setText(agentChildObject.getAddress());
        officeID.setText(agentChildObject.getOfficeID());
        helpDeskPin.setText(agentChildObject.getHelpDeskPin());


        setupOnlicks();
    }



    private void initListeners(){
        service = new AgentContactsService(context);
    }

    private void addFabButton(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    private void setupOnlicks(){

        rootAddress.setOnClickListener(v -> {
            String uri = "http://maps.google.co.in/maps?q=" + agentChildObject.getAddress();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        });

        rootOfficeID.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("OfficeID", agentChildObject.getOfficeID());
            if(clipboard != null) {
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "OfficeID Copied to Clipboard!", Toast.LENGTH_SHORT).show();
            }
        });

        rootHelpDeskPin.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("HelpDeskPin", agentChildObject.getHelpDeskPin());
            if(clipboard != null) {
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "HelpDeskPin Copied to Clipboard!", Toast.LENGTH_SHORT).show();
            }
        });

        rootContacts.setOnClickListener(v -> {
            showContactSelection();
        });

        rootAddContact.setOnClickListener(v -> {
            showAddContactOptions();
        });
    }

    private void showCreateDialog(String ...args){
        DialogFragment dialog = new AddContactDialog();

        if ( args.length == 3 ){
            String name = args[0];
            String number = args[1];
            int agent_id = Integer.parseInt(args[2]);

            Bundle b = new Bundle();
            b.putString("name",name);
            b.putString("number",number);
            b.putInt("agent_id",agent_id);
            dialog.setArguments(b);
        }
        dialog.show(getFragmentManager(), "AddContact");
    }

    private void showAddContactOptions() {

        CharSequence[] options = new CharSequence[]{"Add New", "Choose from Contacts"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose")
                .setItems(options, (dialog, which) -> {
                    switch (which) {

                        case 0 : showCreateDialog();
                            break;
                        case 1 : pickContact();
                            break;
                        default:

                    }
                }).show();
    }


    private void showContactSelection(){

        if(agentChildObject.getContacts().size() == 0) return;



        int size  = agentChildObject.getContacts().size();

       // System.out.println("Size of contacts "+(size -1));
        CharSequence[] options = new CharSequence[size];
        boolean[] isSelected = new boolean[size];

        List<AgentContact> selectedContacts = new ArrayList<>();

        for(int i = 0 ; i < agentChildObject.getContacts().size();i++ ){
            AgentContact c = agentChildObject.getContacts().get(i);
            System.out.println("C New : "+c);
            options[i] = c.getName();
            isSelected[i] = false;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select contact")
                .setMultiChoiceItems(options, isSelected, (dialog, which, isChecked) -> {

                    if(isChecked){
                        selectedContacts.add(agentChildObject.getContacts().get(which));
                    }else if(selectedContacts.contains(agentChildObject.getContacts().get(which))){
                        selectedContacts.remove(agentChildObject.getContacts().get(which));
                    }

                })
                .setPositiveButton("Ok", (dialog, which) -> {

                    if(selectedContacts.size() == 0){
                        Toast.makeText(context,"Please choose at least 1 contact!",Toast.LENGTH_SHORT).show();
                    }

                    if(selectedContacts.size() == 1){
                        //Show them the option to do something with a single contact

                        showSingleOptions(selectedContacts.get(0));
                        dialog.dismiss();
                    }else{
                        showMultipleOptions(selectedContacts);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();


    }

    private void showSingleOptions(AgentContact c){

        System.out.println("Contact details : "+c);

        CharSequence[] OPTIONS = new CharSequence[]{"Details","Call","Email","Export Details"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Options")
                .setItems(OPTIONS, (dialog, which) -> {
                    switch (which){

                        case 0 : showDetails(c);
                            break;
                        case 1 : showCallIntent(c.getNumber());
                            break;
                        case 2 : sendEmailIntent(c.getEmail());
                            break;
                        case 3 :

                            StringBuilder sb = new StringBuilder();
                                sb.append(c.getName()).append("\n")
                                        .append(c.getNumber()).append("\n")
                                        .append(c.getEmail()).append("\n");
                                sb.append("\n\n");

                            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("ExportDetailsSingle", sb.toString());
                            if(clipboard != null) {
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(context, "Content Copied to Clipboard!", Toast.LENGTH_SHORT).show();
                            }

                            break;
                        default:
                    }
                    dialog.dismiss();
                })
                .show();

    }

    private void showDetails(AgentContact c){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Details")
                .setMessage("Name   :    " + c.getName() + "\n" +
                            "Number :    " + c.getNumber() + "\n" +
                            "Email  :    " + c.getEmail() + "\n");
        builder.setNeutralButton("Close", (dialog, which) -> {
            dialog.dismiss();
        }).show();
    }

    private void showMultipleOptions(List<AgentContact> contacts){

        CharSequence[] OPTIONS = new CharSequence[]{"Email","Skype","Export Details"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Options")
                .setItems(OPTIONS, (dialog, which) -> {
                  switch (which){

                      case 0 :
                                break;

                      case 1 :
                                break;
                      case 2 :
                                StringBuilder sb = new StringBuilder();
                                for(AgentContact c : contacts){
                                    sb.append(c.getName()).append("\n")
                                            .append(c.getNumber()).append("\n")
                                            .append(c.getEmail()).append("\n");
                                    sb.append("\n\n");
                                }
                              ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                              ClipData clip = ClipData.newPlainText("ExportDetails", sb.toString());
                              if(clipboard != null) {
                                  clipboard.setPrimaryClip(clip);
                                  Toast.makeText(context, "Content Copied to Clipboard!", Toast.LENGTH_SHORT).show();
                              }


                                break;
                      default:
                  }
                  dialog.dismiss();
                })
                .show();
    }

    private void pickContact() {
        Intent pickContactIntent = new Intent( Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI );
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    private void showCallIntent(String phoneNo){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        System.out.println("Phone number is : "+phoneNo);
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
        //collect our intent
        Intent intent = getIntent();
        AgentChildObject agentContact = null;

        boolean notNull = true;
        try {
            agentContact = intent.getParcelableExtra("agent_details");
            this.agentChildObject = agentContact;
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
                        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        String number = cursor.getString(phoneIndex);
                        String name = cursor.getString(nameIndex);
                        if(number!=null && name != null){
                            if(!number.equalsIgnoreCase("") && !name.equalsIgnoreCase("")){
                                showCreateDialog(name,number,""+agentChildObject.getId());
                            }
                        }
                    }
                    cursor.close();
                }else{
                    System.out.println("Failed to get the number cursor is null");
                }
            }
        }
    }

    @Override
    public void onOk(DialogFragment dialog, AgentContact contact) {

        service.addCallback(response -> {
            if(response.contains("Added Contact Successfully")){
                Toast.makeText(context,"Contacted Added!",Toast.LENGTH_SHORT).show();
            }else if(response.contains("Failed to add new Contact")){
                Toast.makeText(context,"Failed to add contact!",Toast.LENGTH_SHORT).show();
            }
        });

        service.addContact(contact);

        dialog.dismiss();
    }

    @Override
    public void onCancel(DialogFragment dialog) {
        dialog.dismiss();
    }
}
