package com.soj.m1kes.nits.sqlite.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soj.m1kes.nits.sqlite.tables.objects.Table;

/**
 * Created by m1kes on 7/11/2017.
 */

public class AgentsTable implements Table{

    // Database table
    public static final String TABLE_AGENTS = "agents";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String OFFICEID = "officeID";
    public static final String HELPDESKPIN = "helpDeskPin";
    public static final String GROUP_ID = "emailAddress";

    //used for local uploading state
    public static final String COLUMN_IS_SYNCED = "synced";


    // Database creation SQL statement
    private static final String TABLE_CREATE = "create table "
            + TABLE_AGENTS
            + "("
            + ID + " integer , "
            + NAME + " text, "
            + ADDRESS + " text , "
            + OFFICEID + " text,"
            + HELPDESKPIN + " text,"
            + GROUP_ID + " text,"
            + COLUMN_IS_SYNCED+" integer"
            + ");";

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(AgentsTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENTS);
        onCreate(database);
    }


}
