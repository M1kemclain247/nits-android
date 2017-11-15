package com.soj.m1kes.nits;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.soj.m1kes.nits.adapters.listview.adapters.CustomOthersAdapter;
import com.soj.m1kes.nits.util.ActionBarUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class OthersActivity extends AppCompatActivity {

    private ListView lv ;
    private Context context= this;
    // Create and populate a List of planet names.
    String[] optionsText = new String[] { "Agent IP Pairs","Other Option 2","Other Option 3","Other Option 4","Other Option 5","Other Option 6"};
    String[] descriptions = new String[] { "View a list of Agent IP's","Description of Option 2","Description of Option 3","Description of Option 4",
            "Description of Option 5","Description of Option 6"};
    int[] optionsImages = new int[] {R.drawable.ic_file_document_box_black_48dp,R.drawable.ic_briefcase_black_48dp,R.drawable.ic_briefcase_black_48dp,
            R.drawable.ic_information_outline_black_48dp,R.drawable.ic_magnify_black_48dp,R.drawable.ic_water_pump_black_48dp};
    Class[] optionMenus = new Class[]{PairedAgents.class,null,null,null,null,null};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);
        ActionBarUtils.setupActionBar("Others",this);
        lv = (ListView) findViewById( R.id.listviewOtherOptions );
        lv=(ListView) findViewById(R.id.listviewOtherOptions);
        lv.setAdapter(new CustomOthersAdapter(this, optionsText,optionsImages,optionMenus,callback,descriptions));
    }

    CustomOthersAdapter.OnClickListItem callback = new CustomOthersAdapter.OnClickListItem() {
        @Override
        public void onClick(Class aClass) {
            if(aClass!=null) {
                startActivity(new Intent(context, aClass));
            }else{
                Toast.makeText(context,"No Activity to Start!",Toast.LENGTH_SHORT).show();
            }
        }
    };



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(context,MenuScreen.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
