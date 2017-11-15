package com.soj.m1kes.nits.sqlite.adapters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.soj.m1kes.nits.models.Agent;
import com.soj.m1kes.nits.models.AgentContact;
import com.soj.m1kes.nits.models.AgentGroup;
import com.soj.m1kes.nits.models.Job;
import com.soj.m1kes.nits.sqlite.providers.DatabaseContentProvider;
import com.soj.m1kes.nits.sqlite.tables.AgentsTable;
import com.soj.m1kes.nits.sqlite.tables.JobsTable;
import com.soj.m1kes.nits.util.DBUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class AgentsAdapter {


    public static List<Agent> getAllAgents(Context context){


        List<Agent> agents = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query (DatabaseContentProvider.AGENTS_CONTENT_URI , null, null, null, null);

        if (null != cursor && !(cursor.getCount() < 1)) {
            while (cursor.moveToNext()) {

               Agent agent = new Agent();

                int id = cursor.getInt(cursor.getColumnIndex(AgentsTable.ID));
                String agentName = cursor.getString(cursor.getColumnIndex(AgentsTable.NAME));
                String address = cursor.getString(cursor.getColumnIndex(AgentsTable.ADDRESS));
                String officeID = cursor.getString(cursor.getColumnIndex(AgentsTable.OFFICEID));
                String helpDeskPin = cursor.getString(cursor.getColumnIndex(AgentsTable.HELPDESKPIN));
                boolean isSynced = DBUtils.convertIntToBool(cursor.getInt(cursor.getColumnIndex(AgentsTable.COLUMN_IS_SYNCED)));

                int group_id = cursor.getInt(cursor.getColumnIndex(AgentsTable.GROUP_ID));

                AgentGroup group =  AgentGroupsAdapter.getAgentGroup(context,group_id);

                agent.setId(id);
                agent.setName(agentName);
                agent.setAddress(address);
                agent.setOfficeID(officeID);
                agent.setHelpDeskPin(helpDeskPin);
                agent.setGroup(group);

                agent.setSynced(isSynced);
                // Show phone number in Logcat
                System.out.println("Loading from DB: " + agent.toString());
                // end of while loop

                agents.add(agent);
            }
            cursor.close();
        }

        return agents;
    }

    public static List<Agent> getUnsyncedAgents(Context context){

        String dateTimeParseFormat = "dd-MM-yyyy HH:mm:ss";
        List<Agent> agents = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query (DatabaseContentProvider.AGENTS_CONTENT_URI , null, AgentsTable.COLUMN_IS_SYNCED + " = " + 0 + "", null, null);

        if (null != cursor && !(cursor.getCount() < 1)) {
            while (cursor.moveToNext()) {
                // Gets the value from the column.
                Agent agent = new Agent();

                int id = cursor.getInt(cursor.getColumnIndex(AgentsTable.ID));
                String agentName = cursor.getString(cursor.getColumnIndex(AgentsTable.NAME));
                String address = cursor.getString(cursor.getColumnIndex(AgentsTable.ADDRESS));
                String officeID = cursor.getString(cursor.getColumnIndex(AgentsTable.OFFICEID));
                String helpDeskPin = cursor.getString(cursor.getColumnIndex(AgentsTable.HELPDESKPIN));
                boolean isSynced = DBUtils.convertIntToBool(cursor.getInt(cursor.getColumnIndex(AgentsTable.COLUMN_IS_SYNCED)));

                int group_id = cursor.getInt(cursor.getColumnIndex(AgentsTable.GROUP_ID));

                AgentGroup group =  AgentGroupsAdapter.getAgentGroup(context,group_id);

                agent.setId(id);
                agent.setName(agentName);
                agent.setAddress(address);
                agent.setOfficeID(officeID);
                agent.setHelpDeskPin(helpDeskPin);
                agent.setGroup(group);

                agent.setSynced(isSynced);
                // Show phone number in Logcat
                System.out.println("Loading from DB: " + agent.toString());
                // end of while loop

                agents.add(agent);
            }
            cursor.close();
        }

        return agents;
    }


    public static void addAgent(Agent agent, Context context) {

        String format = "yyyy-MM-dd HH:mm:ss";

        ContentValues initialValues = new ContentValues();
        initialValues.put(AgentsTable.ID, agent.getId());
        initialValues.put(AgentsTable.NAME,agent.getName());
        initialValues.put(AgentsTable.ADDRESS, agent.getAddress());
        initialValues.put(AgentsTable.OFFICEID, agent.getOfficeID());
        initialValues.put(AgentsTable.HELPDESKPIN, agent.getHelpDeskPin());
        initialValues.put(AgentsTable.GROUP_ID, agent.getGroup().getId());
        initialValues.put(AgentsTable.COLUMN_IS_SYNCED,DBUtils.convertBoolToInt(agent.isSynced()));

        System.out.println("Adding Agent : "+agent.toString());
        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI, AgentsTable.TABLE_AGENTS);
        Uri resultUri = context.getContentResolver().insert(contentUri, initialValues);

        AgentContactsAdapter.addAll(agent.getContacts(),context);

    }

    public static void addAgents(List<Agent> agents,Context context){
        deleteAll(context);
        for(Agent a : agents){
            addAgent(a,context);
        }
    }

    public static int updateAgent(Agent agent,Context context){

        ContentValues updateValues = new ContentValues();

        ContentValues initialValues = new ContentValues();
        initialValues.put(AgentsTable.ID, agent.getId());
        initialValues.put(AgentsTable.NAME,agent.getName());
        initialValues.put(AgentsTable.ADDRESS, agent.getAddress());
        initialValues.put(AgentsTable.OFFICEID, agent.getOfficeID());
        initialValues.put(AgentsTable.HELPDESKPIN, agent.getHelpDeskPin());
        initialValues.put(AgentsTable.GROUP_ID, agent.getGroup().getId());
        initialValues.put(AgentsTable.COLUMN_IS_SYNCED,DBUtils.convertBoolToInt(agent.isSynced()));

        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI,AgentsTable.TABLE_AGENTS);

        int rowsAffected =  context.getContentResolver().update(contentUri,updateValues,
                AgentsTable.ID+"=?",new String[]{String.valueOf(agent.getId())});

        if(rowsAffected>0){
            System.out.println("Successfully Updated Agent ");
        }else{
            System.out.println("Failed to Update Agent");
        }

        return rowsAffected;


    }

    public static int setAgentSynced(Agent agent, Context context){

        ContentValues updateValues = new ContentValues();
        updateValues.put(AgentsTable.COLUMN_IS_SYNCED, DBUtils.convertIntToBool(1));
        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI,AgentsTable.TABLE_AGENTS);

        int rowsAffected =  context.getContentResolver().update(contentUri,updateValues,
                AgentsTable.NAME+"=?",new String[]{agent.getName()});

        if(rowsAffected>0){
            System.out.println("Successfully set Agent to Synced ");
        }else{
            System.out.println("Failed to set Agent to synced");
        }

        return rowsAffected;
    }

    public static int setAgentUnSynced(int agentID, Context context){

        ContentValues updateValues = new ContentValues();
        updateValues.put(AgentsTable.COLUMN_IS_SYNCED, DBUtils.convertIntToBool(0));
        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI,AgentsTable.TABLE_AGENTS);

        int rowsAffected =  context.getContentResolver().update(contentUri,updateValues,
                AgentsTable.ID+"=?",new String[]{String.valueOf(agentID)});

        if(rowsAffected>0){
            System.out.println("Successfully set Agent to UnSynced ");
        }else{
            System.out.println("Failed to set Agent to Unsynced");
        }

        return rowsAffected;
    }


    private static void deleteAll(Context context){

        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI, AgentsTable.TABLE_AGENTS);
        int result = context.getContentResolver().delete(contentUri,null,null);
        System.out.println("Deleted status: "+result);
    }
}
