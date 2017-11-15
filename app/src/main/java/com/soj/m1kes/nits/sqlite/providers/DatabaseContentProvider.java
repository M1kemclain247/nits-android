package com.soj.m1kes.nits.sqlite.providers;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.soj.m1kes.nits.sqlite.helper.DatabaseHelper;
import com.soj.m1kes.nits.sqlite.tables.AgentContactsTable;
import com.soj.m1kes.nits.sqlite.tables.AgentGroupTable;
import com.soj.m1kes.nits.sqlite.tables.AgentPairTable;
import com.soj.m1kes.nits.sqlite.tables.AgentsTable;
import com.soj.m1kes.nits.sqlite.tables.JobsTable;


public class DatabaseContentProvider extends ContentProvider {

    private DatabaseHelper dbHelper ;

    public static final String AUTHORITY = "nitsProviderAuthorities"; //specific for our our app, will be specified in manifest
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    //Create Content Uri's for the adapters to access
    public static final Uri AGENTS_CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + AgentsTable.TABLE_AGENTS);

    public static final Uri AGENT_CONTACTS_CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + AgentContactsTable.TABLE_CONTACTS);

    public static final Uri JOBS_CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + JobsTable.TABLE_JOBS);

    public static final Uri AGENT_GROUPS_CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + AgentGroupTable.TABLE_AGENT_GROUPS);

    public static final Uri AGENT_IPS_CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + AgentPairTable.TABLE_AGENT_IPS);


    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String table = getTableName(uri);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor =database.query(table,  projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        String table = getTableName(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long value = database.insert(table, null, initialValues);
        if(value>0){
            System.out.println("Successfully Inserted Using Content Provider Rows Affected: "+value);
        }else{
            System.out.println("Failed to insert record in content provider");
        }

        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(value));
    }

    @Override
    public int delete(Uri uri, String where, String[] args) {
        String table = getTableName(uri);
        SQLiteDatabase dataBase=dbHelper.getWritableDatabase();
        return dataBase.delete(table, where, args);
    }

    @Override
    public int update(Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
        String table = getTableName(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        return database.update(table, values, whereClause, whereArgs);
    }

    public static String getTableName(Uri uri){
        String value = uri.getPath();
        value = value.replace("/", "");//we need to remove '/'
        return value;
    }


}

