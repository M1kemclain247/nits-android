package com.soj.m1kes.nits.sqlite.adapters;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.soj.m1kes.nits.adapters.recyclerview.models.PairChildObject;
import com.soj.m1kes.nits.adapters.recyclerview.models.PairParentObject;
import com.soj.m1kes.nits.models.Agent;
import com.soj.m1kes.nits.models.IPair;
import com.soj.m1kes.nits.sqlite.providers.DatabaseContentProvider;
import com.soj.m1kes.nits.sqlite.tables.AgentPairTable;
import com.soj.m1kes.nits.sqlite.tables.AgentsTable;
import com.soj.m1kes.nits.util.DBUtils;

import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

public class AgentPairsAdapter {


    public static List<IPair> getAllAgentIPS(Context context){

        List<IPair> pairs = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query (DatabaseContentProvider.AGENT_IPS_CONTENT_URI , null, null, null, null);

        if (null != cursor && !(cursor.getCount() < 1)) {
            while (cursor.moveToNext()) {
                IPair pair = new IPair();

                int id = cursor.getInt(cursor.getColumnIndex(AgentPairTable.ID));
                String name = cursor.getString(cursor.getColumnIndex(AgentPairTable.NAME));
                String ip = cursor.getString(cursor.getColumnIndex(AgentPairTable.IP));
                String model = cursor.getString(cursor.getColumnIndex(AgentPairTable.MODEL));
                String connection_modes = cursor.getString(cursor.getColumnIndex(AgentPairTable.CONNECTION_MODES));
                String port = cursor.getString(cursor.getColumnIndex(AgentPairTable.PORT));
                boolean isSynced = DBUtils.convertIntToBool(cursor.getInt(cursor.getColumnIndex(AgentPairTable.IS_SYNCED)));

                pair.setId(id);
                pair.setName(name);
                pair.setIp(ip);
                pair.setModel(model);
                pair.setConnectionModes(connection_modes);
                pair.setPort(port);
                pair.setSynced(isSynced);

                System.out.println("Loading from DB: " + pair.toString());


                pairs.add(pair);
            }
            cursor.close();
        }

        return pairs;
    }

    public static List<PairChildObject> getAllAgentIPSRecycler(Context context){

        List<PairChildObject> pairs = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query (DatabaseContentProvider.AGENT_IPS_CONTENT_URI , null, null, null, null);

        if (null != cursor && !(cursor.getCount() < 1)) {
            while (cursor.moveToNext()) {
                PairChildObject pair = new PairChildObject();

                int id = cursor.getInt(cursor.getColumnIndex(AgentPairTable.ID));
                String name = cursor.getString(cursor.getColumnIndex(AgentPairTable.NAME));
                String ip = cursor.getString(cursor.getColumnIndex(AgentPairTable.IP));
                String model = cursor.getString(cursor.getColumnIndex(AgentPairTable.MODEL));
                String connection_modes = cursor.getString(cursor.getColumnIndex(AgentPairTable.CONNECTION_MODES));
                String port = cursor.getString(cursor.getColumnIndex(AgentPairTable.PORT));

                pair.setId(id);
                pair.setName(name);
                pair.setIp(ip);
                pair.setModel(model);
                pair.setConnectionModes(connection_modes);
                pair.setPort(port);

                System.out.println("Loading from DB: " + pair.toString());

                pairs.add(pair);
            }
            cursor.close();
        }
        return pairs;
    }

    public static List<IPair> getUnsyncedPairs(Context context){

        List<IPair> pairs = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query (DatabaseContentProvider.AGENT_IPS_CONTENT_URI , null, AgentsTable.COLUMN_IS_SYNCED + " = " + 0 + "", null, null);

        if (null != cursor && !(cursor.getCount() < 1)) {
            while (cursor.moveToNext()) {
                IPair pair = new IPair();

                int id = cursor.getInt(cursor.getColumnIndex(AgentPairTable.ID));
                String name = cursor.getString(cursor.getColumnIndex(AgentPairTable.NAME));
                String ip = cursor.getString(cursor.getColumnIndex(AgentPairTable.IP));
                String model = cursor.getString(cursor.getColumnIndex(AgentPairTable.MODEL));
                String connection_modes = cursor.getString(cursor.getColumnIndex(AgentPairTable.CONNECTION_MODES));
                String port = cursor.getString(cursor.getColumnIndex(AgentPairTable.PORT));
                boolean isSynced = DBUtils.convertIntToBool(cursor.getInt(cursor.getColumnIndex(AgentPairTable.IS_SYNCED)));

                pair.setId(id);
                pair.setName(name);
                pair.setIp(ip);
                pair.setModel(model);
                pair.setConnectionModes(connection_modes);
                pair.setPort(port);
                pair.setSynced(isSynced);

                System.out.println("Loading from DB: " + pair.toString());

                pairs.add(pair);
            }
            cursor.close();
        }
        return pairs;
    }


    public static void addAgentPair(IPair pair, Context context) {


        ContentValues initialValues = new ContentValues();
        initialValues.put(AgentPairTable.ID, pair.getId());
        initialValues.put(AgentPairTable.NAME,pair.getName());
        initialValues.put(AgentPairTable.IP, pair.getIp());
        initialValues.put(AgentPairTable.MODEL, pair.getModel());
        initialValues.put(AgentPairTable.CONNECTION_MODES, pair.getConnectionModes());
        initialValues.put(AgentPairTable.PORT, pair.getPort());
        initialValues.put(AgentPairTable.IS_SYNCED, pair.isSynced());

        System.out.println("Adding AgentPair : "+pair.toString());
        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI, AgentPairTable.TABLE_AGENT_IPS);
        Uri resultUri = context.getContentResolver().insert(contentUri, initialValues);
    }

    public static void addAgents(List<IPair> iPairs,Context context){
        deleteAll(context);
        for(IPair a : iPairs){
            addAgentPair(a,context);
        }
    }

    public static int updateAgent(IPair pair,Context context){

        ContentValues updateValues = new ContentValues();

        updateValues.put(AgentPairTable.ID, pair.getId());
        updateValues.put(AgentPairTable.NAME,pair.getName());
        updateValues.put(AgentPairTable.IP, pair.getIp());
        updateValues.put(AgentPairTable.MODEL, pair.getModel());
        updateValues.put(AgentPairTable.CONNECTION_MODES, pair.getConnectionModes());
        updateValues.put(AgentPairTable.PORT, pair.getPort());
        updateValues.put(AgentPairTable.IS_SYNCED, pair.isSynced());


        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI,AgentPairTable.TABLE_AGENT_IPS);

        int rowsAffected =  context.getContentResolver().update(contentUri,updateValues,
                AgentPairTable.ID+"=?",new String[]{String.valueOf(pair.getId())});

        if(rowsAffected>0){
            System.out.println("Successfully Updated Agent Pair");
        }else{
            System.out.println("Failed to Update Agent Pair");
        }

        return rowsAffected;
    }

    public static int setAgentPairSynced(IPair pair, Context context){

        ContentValues updateValues = new ContentValues();
        updateValues.put(AgentPairTable.IS_SYNCED, DBUtils.convertIntToBool(1));
        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI,AgentPairTable.TABLE_AGENT_IPS);

        int rowsAffected =  context.getContentResolver().update(contentUri,updateValues,
                AgentPairTable.NAME+"=?",new String[]{pair.getName()});

        if(rowsAffected>0){
            System.out.println("Successfully set AgentPair to Synced ");
        }else{
            System.out.println("Failed to set AgentPair to synced");
        }

        return rowsAffected;
    }

    public static int setAgentUnSynced(int agentID, Context context){

        ContentValues updateValues = new ContentValues();
        updateValues.put(AgentPairTable.IS_SYNCED, DBUtils.convertIntToBool(0));
        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI,AgentPairTable.TABLE_AGENT_IPS);

        int rowsAffected =  context.getContentResolver().update(contentUri,updateValues,
                AgentPairTable.ID+"=?",new String[]{String.valueOf(agentID)});

        if(rowsAffected>0){
            System.out.println("Successfully set AgentPair to Un-Synced ");
        }else{
            System.out.println("Failed to set AgentPair to Un-Synced");
        }

        return rowsAffected;
    }

    private static void deleteAll(Context context){
        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI, AgentPairTable.TABLE_AGENT_IPS);
        int result = context.getContentResolver().delete(contentUri,null,null);
        System.out.println("Deleted rows: "+result);
    }

}
