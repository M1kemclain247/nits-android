package com.soj.m1kes.nits.util;

import android.content.Context;


import com.soj.m1kes.nits.prefmanagers.IpPrefManager;




public class WebResources {



    /**
     * Urls for each Item request
     * **/
    public static final String URL_AGENTS = "Agents";
    public static final String URL_AGENT_PAIRS = "AgentPairs";
    public static final String URL_GET_AGENT = "GetAgent";
    public static final String URL_JOBS="Jobs";
    public static final String URL_AGENT_GROUPS = "AgentGroups";


    public static final String URL_POST_JOB="AddJob";
    public static final String URL_POST_AGENT = "AddAgent";
    public static final String URL_POST_CONTACT = "AddContact";
    public static final String URL_POST_AGENT_PAIR = "AddAgentPair";
    public static final String URL_UPDATE_AGENT = "UpdateAgent";
    public static final String URL_UPDATE_AGENT_PAIR = "UpdateAgentPair";
    public static final String URL_POST_AGENT_GROUP = "AddAgentGroup";



    public static final String PORT = "8080";
    public static final String URL_START= "http://";

    public static String getAgentsUrl(Context context){
        String ip = IpPrefManager.getIpAddress(context);
        return URL_START + ip + ":"+PORT+ "/"+ URL_AGENTS;
    }
    public static String getJobsUrl(Context context){
        String ip = IpPrefManager.getIpAddress(context);
        return URL_START + ip + ":"+PORT+ "/"+ URL_JOBS;
    }

    public static String getUrlAgentPairs(Context context){
        String ip = IpPrefManager.getIpAddress(context);
        return URL_START + ip + ":"+PORT+ "/"+ URL_AGENT_PAIRS;
    }

    public static String postJobUrl(Context context){
        String ip = IpPrefManager.getIpAddress(context);
        return URL_START + ip + ":"+PORT+ "/"+ URL_POST_JOB;
    }

    public static String postContactUrl(Context context){
        String ip = IpPrefManager.getIpAddress(context);
        return URL_START + ip + ":"+PORT+ "/"+ URL_POST_CONTACT;
    }

    public static String postAgentUrl(Context context){
        String ip = IpPrefManager.getIpAddress(context);
        return URL_START + ip + ":"+PORT+ "/"+ URL_POST_AGENT;
    }
    //Agent Pairs
    public static String postAgentPairUrl(Context context){
        String ip = IpPrefManager.getIpAddress(context);
        return URL_START + ip + ":"+PORT+ "/"+ URL_POST_AGENT_PAIR;
    }

    public static String postAgentUpdateUrl(Context context){
        String ip = IpPrefManager.getIpAddress(context);
        return URL_START + ip + ":"+PORT+ "/"+ URL_UPDATE_AGENT;
    }

    public static String getAgentUrl(Context context){
        String ip = IpPrefManager.getIpAddress(context);
        return URL_START + ip + ":"+PORT+ "/"+ URL_GET_AGENT;
    }

    public static String getAgentGroupsUrl(Context context){
        String ip = IpPrefManager.getIpAddress(context);
        return URL_START + ip + ":"+PORT+ "/"+ URL_AGENT_GROUPS;
    }

    public static String postAgentGroupUrl(Context context){
        String ip = IpPrefManager.getIpAddress(context);
        return URL_START + ip + ":"+PORT+ "/"+ URL_POST_AGENT_GROUP;
    }




}
