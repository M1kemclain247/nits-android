package com.soj.m1kes.nits.sqlite.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soj.m1kes.nits.sqlite.tables.objects.Table;

/**
 * Created by m1kes on 7/21/2017.
 */

public class JobsTable implements Table{
    // Database table
    public static final String TABLE_JOBS = "jobs";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DATE = "date";

    public static final String DESCRIPTION = "description";
    public static final String PRIORITY = "priority";

    public static final String DETAILS = "details";
    public static final String IS_SYNCED = "synced";

    // Database creation SQL statement
    private static final String TABLE_CREATE = "create table "
            + TABLE_JOBS
            + "("
            + ID + " integer , "
            + NAME + " text, "
            + DATE + " text , "
            + DESCRIPTION + " text,"
            + PRIORITY + " text,"
            + DETAILS + " text,"
            + IS_SYNCED+" integer"
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
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_JOBS);
        onCreate(database);
    }


}
