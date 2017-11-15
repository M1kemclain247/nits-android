package com.soj.m1kes.nits.util;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by m1kes on 7/19/2017.
 */

public class ActionBarUtils {

    public static void setupActionBar(String title, AppCompatActivity appCompatActivity) {
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(title);
        }
    }

    public static void setupTitleOnlyActionBar(String title, AppCompatActivity appCompatActivity) {
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setTitle(title);
        }
    }

}
