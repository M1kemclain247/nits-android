package com.soj.m1kes.nits.service;


import android.content.Context;
import android.os.AsyncTask;

import com.soj.m1kes.nits.models.Agent;
import com.soj.m1kes.nits.models.ConnectionMode;
import com.soj.m1kes.nits.models.IPair;
import com.soj.m1kes.nits.service.callbacks.OnAgentPairsDownloaded;
import com.soj.m1kes.nits.service.callbacks.OnAgentPairsSynced;
import com.soj.m1kes.nits.sqlite.adapters.AgentPairsAdapter;
import com.soj.m1kes.nits.sqlite.adapters.AgentsAdapter;
import com.soj.m1kes.nits.util.WebResources;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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
import java.net.MalformedURLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AgentPairsService implements SyncService {

    private OnAgentPairsDownloaded response;
    private OnAgentPairsSynced syncResponse;
    private Context context;



    public AgentPairsService(OnAgentPairsDownloaded response,Context context){
        this.response = response;
        this.context = context;
    }

    public AgentPairsService(Context context,OnAgentPairsSynced syncResponse){
        this.context = context;
        this.syncResponse = syncResponse;
    }


    public void StartSync(){
        new SyncPairs().execute(WebResources.getUrlAgentPairs(context));
    }

    public void StartPostUnSynced(){
        new PairPostService().execute();
    }

    private class SyncPairs extends AsyncTask<String,Void,List<IPair>>{


        public SyncPairs(){

        }

        @Override
        protected List<IPair> doInBackground(String... params) {

            String url = params[0];
            System.out.println("Requesting URL :"+url);
            String response =  requestContent(url);

            List<IPair> pairs = new ArrayList<>();

            try {

                if(response==null){
                    throw new JSONException("Response is Null");
                }

                JSONArray array =  new JSONArray(response);



                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);

                    int id =  jsonObject.getInt("id");
                    String name =  jsonObject.getString("name");
                    String ip =  jsonObject.getString("ip");
                    String model = jsonObject.getString("model");
                    JSONArray arrJson = jsonObject.getJSONArray("connectionModes");
                    ConnectionMode[] connectionModes = new ConnectionMode[arrJson.length()];
                    for(int j = 0; j < arrJson.length(); j++)
                        connectionModes[j] = ConnectionMode.valueOf(arrJson.getString(j));

                    String port = jsonObject.getString("port");
                    int start = 0;
                    StringBuilder sb = new StringBuilder();
                    for(ConnectionMode c : connectionModes){
                        start++;
                        if(start!=1)
                            sb.append(",");
                        sb.append(c);
                    }

                    IPair pair = new IPair(id,name,ip,model,sb.toString(),port,true);
                    pairs.add(pair);
                }

            } catch (JSONException e) {
                // manage exceptions
                System.out.println("Exception when parsing Json: "+e);
            }

            return pairs;
        }

        @Override
        protected void onPostExecute(List<IPair> iPairs) {

            if(iPairs!=null&&!iPairs.isEmpty()){
                AgentPairsAdapter.addAgents(iPairs,context);
                response.onDownloaded();
            }else{
                response.onFailed();
            }

        }
    }


    private class PairPostService extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... urls) {


            List<IPair> unsyncedAgents = AgentPairsAdapter.getUnsyncedPairs(context);


            for(IPair pair : unsyncedAgents) {

                JSONObject jsonData = new JSONObject();
                try {
                    jsonData.put("id",pair.getId());
                    jsonData.put("name", pair.getName());
                    jsonData.put("ip", pair.getIp());

                    System.out.println("Successfully parsed Agent to JSON");
                    HttpClient httpclient;

                    System.out.println("Sending Json : ");
                    System.out.println(jsonData.toString());

                    try {

                        //Set the timeout to 6 Seconds
                        final HttpParams httpParams = new BasicHttpParams();
                        HttpConnectionParams.setConnectionTimeout(httpParams, 4000);
                        HttpConnectionParams.setSoTimeout(httpParams,4000);

                        httpclient = new DefaultHttpClient(httpParams);
                        HttpPost postRequest = new HttpPost(WebResources.postAgentPairUrl(context));

                        StringEntity input = new StringEntity(jsonData.toString());
                        input.setContentType("application/json");
                        postRequest.setEntity(input);

                        HttpResponse response = httpclient.execute(postRequest);

                        BufferedReader br = new BufferedReader(
                                new InputStreamReader((response.getEntity().getContent())));

                        String output = "";
                        String s;
                        System.out.println("Output from Server .... \n");
                        while ((s = br.readLine()) != null) {
                            output += s;
                        }
                        System.out.println(output);
                        httpclient.getConnectionManager().shutdown();

                        if (output.contains("OK")) {
                            AgentPairsAdapter.setAgentPairSynced(pair, context);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            List<IPair> agentsTemp = AgentPairsAdapter.getUnsyncedPairs(context);

            if(agentsTemp!=null&&agentsTemp.isEmpty()){
                //Successfully Synced all unsynced Jobs
                syncResponse.onSyncComplete();
            }else{
                //Failed to sync some jobs
                syncResponse.onSyncFailed();
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
            HttpConnectionParams.setConnectionTimeout(httpParams, 4000);
            HttpConnectionParams.setSoTimeout(httpParams, 4000);

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
