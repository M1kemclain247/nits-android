package com.soj.m1kes.nits.sqlite.adapters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.soj.m1kes.nits.models.AgentContact;
import com.soj.m1kes.nits.sqlite.providers.DatabaseContentProvider;
import com.soj.m1kes.nits.sqlite.tables.AgentContactsTable;
import com.soj.m1kes.nits.sqlite.tables.AgentsTable;

import java.util.ArrayList;
import java.util.List;

public class AgentContactsAdapter {



    public static List<AgentContact> getAllContacts(Context context){

        List<AgentContact> contacts = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(DatabaseContentProvider.AGENT_CONTACTS_CONTENT_URI , null, null, null, null);

        if (null != cursor && !(cursor.getCount() < 1)) {
            while (cursor.moveToNext()) {

                AgentContact c = new AgentContact();

                int id = cursor.getInt(cursor.getColumnIndex(AgentContactsTable.ID));
                String name = cursor.getString(cursor.getColumnIndex(AgentContactsTable.NAME));
                String email = cursor.getString(cursor.getColumnIndex(AgentContactsTable.EMAIL));
                String number = cursor.getString(cursor.getColumnIndex(AgentContactsTable.NUMBER));
                int agent_id = cursor.getInt(cursor.getColumnIndex(AgentContactsTable.AGENT_ID));

                c.setId(id);
                c.setName(name);
                c.setEmail(email);
                c.setNumber(number);
                c.setAgent_id(agent_id);

                System.out.println("Loading from DB: " +  c.toString());

                contacts.add(c);
            }
            cursor.close();
        }
        return contacts;
    }

    public static List<AgentContact>  getContactsForAgent(Context context,int agent_id){

        List<AgentContact> contacts = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(DatabaseContentProvider.AGENT_CONTACTS_CONTENT_URI , null,
                AgentContactsTable.AGENT_ID + " = '" + agent_id + "'", null, null);

        if (null != cursor && !(cursor.getCount() < 1)) {
            while (cursor.moveToNext()) {

                AgentContact c = new AgentContact();

                int id = cursor.getInt(cursor.getColumnIndex(AgentContactsTable.ID));
                String name = cursor.getString(cursor.getColumnIndex(AgentContactsTable.NAME));
                String email = cursor.getString(cursor.getColumnIndex(AgentContactsTable.EMAIL));
                String number = cursor.getString(cursor.getColumnIndex(AgentContactsTable.NUMBER));
                int agent_id2 = cursor.getInt(cursor.getColumnIndex(AgentContactsTable.AGENT_ID));

                c.setId(id);
                c.setName(name);
                c.setEmail(email);
                c.setNumber(number);
                c.setAgent_id(agent_id2);

                System.out.println("Loading from DB: " + c.toString());

                contacts.add(c);
            }
            cursor.close();
        }
        return contacts;
    }




    public static void addContact(AgentContact c, Context context) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(AgentContactsTable.ID, c.getId());
        initialValues.put(AgentContactsTable.NAME,c.getName());
        initialValues.put(AgentContactsTable.EMAIL,c.getEmail());
        initialValues.put(AgentContactsTable.NUMBER,c.getNumber());
        initialValues.put(AgentContactsTable.AGENT_ID,c.getAgent_id());

        System.out.println("Adding Contact : "+c.toString());

        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI, AgentContactsTable.TABLE_CONTACTS);
        Uri resultUri = context.getContentResolver().insert(contentUri, initialValues);

    }
    private static void deleteAll(Context context){

        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI, AgentContactsTable.TABLE_CONTACTS);
        int result = context.getContentResolver().delete(contentUri,null,null);
        System.out.println("Deleted status: "+result);
    }

    public static void addAll(List<AgentContact> contacts,Context context){
        //deleteAll(context);
        for(AgentContact c : contacts){
            deleteForId(context,c.getAgent_id());
            addContact(c,context);
        }
    }

    public static void deleteForId(Context context,int agent_id){

        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI, AgentContactsTable.TABLE_CONTACTS);
        int rowsAffected =  context.getContentResolver().delete(contentUri,
                AgentContactsTable.AGENT_ID+"=?",new String[]{String.valueOf(agent_id)});
    }


}
