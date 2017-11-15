package com.soj.m1kes.nits.sqlite.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.soj.m1kes.nits.sqlite.tables.AgentContactsTable;
import com.soj.m1kes.nits.sqlite.tables.AgentGroupTable;
import com.soj.m1kes.nits.sqlite.tables.AgentPairTable;
import com.soj.m1kes.nits.sqlite.tables.AgentsTable;
import com.soj.m1kes.nits.sqlite.tables.JobsTable;
import com.soj.m1kes.nits.sqlite.tables.objects.Table;

import java.util.HashMap;
import java.util.Map;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "nits_db.db";
    private static final int DATABASE_VERSION = 1;

    private Map<String,Table> tables = new HashMap<>();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        load();
    }


    public void load(){
        tables.put("agents",new AgentsTable());
        tables.put("agent_groups",new AgentGroupTable());
        tables.put("agent_pairs",new AgentPairTable());
        tables.put("agent_contacts",new AgentContactsTable());
        tables.put("jobs",new JobsTable());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(Table t : tables.values()){
            t.onCreate(db);
        }
        System.out.println("Database's Created");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        for(Table t : tables.values()){
            t.onUpgrade(db,i,i1);
        }
        System.out.println("Database's Upgraded");
    }

}
