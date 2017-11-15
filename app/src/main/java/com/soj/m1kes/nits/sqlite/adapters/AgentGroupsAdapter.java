package com.soj.m1kes.nits.sqlite.adapters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.soj.m1kes.nits.models.Agent;
import com.soj.m1kes.nits.models.AgentGroup;
import com.soj.m1kes.nits.sqlite.providers.DatabaseContentProvider;
import com.soj.m1kes.nits.sqlite.tables.AgentGroupTable;
import com.soj.m1kes.nits.sqlite.tables.AgentsTable;
import com.soj.m1kes.nits.util.DBUtils;

import java.util.ArrayList;
import java.util.List;


public class AgentGroupsAdapter {


    public static List<AgentGroup> getAllAgentGroups(Context context){

        List<AgentGroup> agents = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(DatabaseContentProvider.AGENT_GROUPS_CONTENT_URI , null, null, null, null);

        if (null != cursor && !(cursor.getCount() < 1)) {
            while (cursor.moveToNext()) {

                AgentGroup agentGroup = new AgentGroup();

                int id = cursor.getInt(cursor.getColumnIndex(AgentGroupTable.ID));
                String groupName = cursor.getString(cursor.getColumnIndex(AgentGroupTable.NAME));

                agentGroup.setId(id);
                agentGroup.setGroupName(groupName);

                System.out.println("Loading from DB: " + agentGroup.toString());

                agents.add(agentGroup);
            }
            cursor.close();
        }
        return agents;
    }

    public static AgentGroup getAgentGroup(Context context,int group_id){

        AgentGroup agentGroup = null;
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(DatabaseContentProvider.AGENT_GROUPS_CONTENT_URI , null,
                AgentGroupTable.ID + " = '" + group_id + "'", null, null);

        if (null != cursor && !(cursor.getCount() < 1)) {
            if (cursor.moveToNext()) {

                agentGroup = new AgentGroup();

                int id = cursor.getInt(cursor.getColumnIndex(AgentGroupTable.ID));
                String groupName = cursor.getString(cursor.getColumnIndex(AgentGroupTable.NAME));

                agentGroup.setId(id);
                agentGroup.setGroupName(groupName);

                System.out.println("Loading from DB: " + agentGroup.toString());

            }
            cursor.close();
        }
        return agentGroup;
    }




    public static void addAgentGroup(AgentGroup agentGroup, Context context) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(AgentGroupTable.ID, agentGroup.getId());
        initialValues.put(AgentGroupTable.NAME,agentGroup.getGroupName());

        System.out.println("Adding AgentGroup : "+agentGroup.toString());

        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI, AgentGroupTable.TABLE_AGENT_GROUPS);
        Uri resultUri = context.getContentResolver().insert(contentUri, initialValues);

    }
    private static void deleteAll(Context context){

        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI, AgentGroupTable.TABLE_AGENT_GROUPS);
        int result = context.getContentResolver().delete(contentUri,null,null);
        System.out.println("Deleted status: "+result);
    }

    public static void addAgentGroups(List<AgentGroup> agentGroups,Context context){
        deleteAll(context);
        for(AgentGroup a : agentGroups){
            addAgentGroup(a,context);
        }
    }

}
