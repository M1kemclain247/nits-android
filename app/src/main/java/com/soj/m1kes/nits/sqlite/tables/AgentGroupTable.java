package com.soj.m1kes.nits.sqlite.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soj.m1kes.nits.sqlite.tables.objects.Table;

/**
 * Created by Michael on 7/31/2017.
 */

public class AgentGroupTable implements Table {


    // Database table
    public static final String TABLE_AGENT_GROUPS = "agent_groups";

    public static final String ID = "id";
    public static final String NAME = "name";

    // Database creation SQL statement
    private static final String TABLE_CREATE = "create table "
            + TABLE_AGENT_GROUPS
            + "("
            + ID + " integer , "
            + NAME + " text"
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
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENT_GROUPS);
        onCreate(database);

    }
}
