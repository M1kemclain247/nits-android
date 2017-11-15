package com.soj.m1kes.nits.sqlite.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soj.m1kes.nits.sqlite.tables.objects.Table;


public class AgentPairTable implements Table {

    public static final String TABLE_AGENT_IPS = "agent_ips";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String IP = "ip";
    public static final String MODEL = "model";
    public static final String CONNECTION_MODES = "connection_modes";
    public static final String PORT = "port";

    public static final String IS_SYNCED = "synced";

    private static final String TABLE_CREATE = "create table "
            + TABLE_AGENT_IPS
            + "("
            + ID + " integer , "
            + NAME + " text, "
            + IP + " text ,"
            + MODEL + " text ,"
            + CONNECTION_MODES + " text ,"
            + PORT + " text ,"
            + IS_SYNCED + " integer "
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
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENT_IPS);
        onCreate(database);
    }


}
