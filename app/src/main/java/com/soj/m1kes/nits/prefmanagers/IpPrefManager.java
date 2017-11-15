package com.soj.m1kes.nits.prefmanagers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;



public class IpPrefManager {

    private static final String IP_PREFS = "IpSettings";
    private static final String KEY_IP = "IpAddress";

    public static final String HOME_IP = "Home";
    public static final String WORK_IP = "Work";
    public static final String MY_PC_WORK = "WORK_MY_PC";

    public static final Map<String, String> IPS = Collections.unmodifiableMap(
            new HashMap<String, String>() {{
                put(HOME_IP, "192.168.100.10");
                put(WORK_IP, "10.29.12.38");
                put(MY_PC_WORK,"10.29.8.27");
            }});

    public static void setIpAddress(String ipAddress ,Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(IP_PREFS, MODE_PRIVATE).edit();
        editor.putString(KEY_IP,ipAddress);
        editor.apply();
    }

    public static String getIpAddress(Context context){
        SharedPreferences prefs = context.getSharedPreferences(IP_PREFS, MODE_PRIVATE);
        return prefs.getString(KEY_IP, IPS.get(MY_PC_WORK));
    }



}
