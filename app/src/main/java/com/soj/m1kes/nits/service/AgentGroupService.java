package com.soj.m1kes.nits.service;

import android.content.Context;
import android.os.AsyncTask;


import com.soj.m1kes.nits.models.AgentGroup;
import com.soj.m1kes.nits.service.callbacks.OnAgentGroupsDownloaded;
import com.soj.m1kes.nits.sqlite.adapters.AgentGroupsAdapter;

import com.soj.m1kes.nits.util.WebResources;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 7/31/2017.
 */

public class AgentGroupService {

    private Context context;
    private OnAgentGroupsDownloaded onAgentGroupsDownloaded;

    public AgentGroupService(){

    }

    public AgentGroupService(Context context,OnAgentGroupsDownloaded onAgentGroupsDownloaded){
        this.context = context;
        this.onAgentGroupsDownloaded = onAgentGroupsDownloaded;
    }

    public void SyncGroups(){
        new SyncAgentGroups().execute(WebResources.getAgentGroupsUrl(context));
    }


    private class SyncAgentGroups extends AsyncTask<String,Void,List<AgentGroup>>{

        @Override
        protected List<AgentGroup> doInBackground(String... urls) {

            String response =  requestContent(urls[0]);

            List<AgentGroup> agentGroups = new ArrayList<AgentGroup>();

            try {

                if(response==null){
                    throw new JSONException("Response is Null");
                }

                JSONArray array =  new JSONArray(response);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);

                    int id =  jsonObject.getInt("id");
                    String name =  jsonObject.getString("groupName");
                    AgentGroup group = new AgentGroup(id,name);
                    agentGroups.add(group);
                }

            } catch (JSONException e) {
                // manage exceptions
                System.out.println("Exception when parsing Json: "+e);
            }

            return agentGroups;
        }

        @Override
        protected void onPostExecute(List<AgentGroup> agentGroups) {

            if(agentGroups!=null&&!agentGroups.isEmpty()){
                System.out.println("Adding AgentGroups to db");
                AgentGroupsAdapter.addAgentGroups(agentGroups,context);
                onAgentGroupsDownloaded.onDownloaded();
            }else{
                onAgentGroupsDownloaded.onDownloadFailed();
            }

        }


    }

    public String requestContent(String url) {

        HttpClient httpclient;
        String result = null;
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = null;
        InputStream instream = null;

        try {

            //Set the timeout to 6 Seconds
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
            HttpConnectionParams.setSoTimeout(httpParams, 3000);

            httpclient = new DefaultHttpClient(httpParams);

            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                instream = entity.getContent();
                result = convertStreamToString(instream);
            }

        } catch (Exception e) {
            // manage exceptions
        } finally {
            if (instream != null) {
                try {
                    instream.close();
                } catch (Exception exc) {

                }
            }
        }

        return result;
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }

        return sb.toString();
    }


}

