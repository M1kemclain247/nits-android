package com.soj.m1kes.nits.sqlite.tables.objects;


import android.database.sqlite.SQLiteDatabase;

public interface Table {
    void onCreate(SQLiteDatabase db);
    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
