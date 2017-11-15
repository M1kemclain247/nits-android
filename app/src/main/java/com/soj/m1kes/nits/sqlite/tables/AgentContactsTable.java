package com.soj.m1kes.nits.sqlite.tables;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soj.m1kes.nits.sqlite.tables.objects.Table;

public class AgentContactsTable implements Table{

    public static final String TABLE_CONTACTS = "agent_contacts";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String NUMBER = "number";
    public static final String AGENT_ID = "agent_id";


    public static final String COLUMN_IS_SYNCED = "synced";

    private static final String TABLE_CREATE = "create table "
            + TABLE_CONTACTS
            + "("
            + ID + " integer , "
            + NAME + " text, "
            + EMAIL + " text ,"
            + NUMBER + " text ,"
            + AGENT_ID + " integer ,"
            + COLUMN_IS_SYNCED + " integer "
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
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(database);
    }

}
