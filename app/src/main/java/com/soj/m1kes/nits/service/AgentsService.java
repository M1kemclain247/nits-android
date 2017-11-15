package com.soj.m1kes.nits.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.soj.m1kes.nits.MainActivity;
import com.soj.m1kes.nits.models.Agent;
import com.soj.m1kes.nits.models.AgentContact;
import com.soj.m1kes.nits.models.AgentGroup;
import com.soj.m1kes.nits.models.Job;
import com.soj.m1kes.nits.service.callbacks.OnAgentUpdateDownloaded;
import com.soj.m1kes.nits.service.callbacks.OnAgentsDownloaded;
import com.soj.m1kes.nits.service.callbacks.OnAgentsSynced;
import com.soj.m1kes.nits.sqlite.adapters.AgentsAdapter;
import com.soj.m1kes.nits.sqlite.adapters.JobsAdapter;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;



public class AgentsService {

    private Context context;
    private OnAgentsSynced onAgentsSynced;
    private List<Agent> agents;
    private OnAgentsDownloaded onAgentsDownloaded;
    private OnAgentUpdateDownloaded onAgentUpdateDownloaded;


    public AgentsService(Context context,OnAgentsSynced onAgentsSynced,List<Agent> agents){
        this.context = context;
        this.onAgentsSynced = onAgentsSynced;
        this.agents = agents;
    }

    public AgentsService(Context context, OnAgentsDownloaded onAgentsDownloaded){
        this.context = context;
        this.onAgentsDownloaded = onAgentsDownloaded;
    }

    public AgentsService(Context context, OnAgentUpdateDownloaded onAgentUpdateDownloaded){
        this.context = context;
        this.onAgentUpdateDownloaded = onAgentUpdateDownloaded;
    }

    public void startUpdate(String id){
        new DownloadAgent().execute(WebResources.getAgentUrl(context),id);
    }


    public void startDownload(){
        new DownloadAgents().execute(WebResources.getAgentsUrl(context));
    }

    public void startPostSync(){
        new AgentsPostService().execute(agents);
    }

    private class AgentsPostService extends AsyncTask<List<Agent>,Void,Void> {

        public AgentsPostService(){

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(List<Agent>... agents) {


            List<Agent> unsyncedAgents = agents[0];

            for(Agent agent : unsyncedAgents) {

                JSONObject jsonData = new JSONObject();
                try {
                    jsonData.put("id",agent.getId());
                    jsonData.put("name", agent.getName());
                    jsonData.put("address", agent.getAddress());
                    jsonData.put("officeID", agent.getOfficeID());
                    jsonData.put("helpDeskPin", agent.getHelpDeskPin());

                    System.out.println("Successfully parsed Agent to JSON");
                    HttpClient httpclient;

                    System.out.println("Sending Json : ");
                    System.out.println(jsonData.toString());

                    try {

                        //Set the timeout to 6 Seconds
                        final HttpParams httpParams = new BasicHttpParams();
                        HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
                        HttpConnectionParams.setSoTimeout(httpParams,8000);

                        httpclient = new DefaultHttpClient(httpParams);
                        HttpPost postRequest = new HttpPost(WebResources.postAgentUrl(context));

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
                            AgentsAdapter.setAgentSynced(agent, context);
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

            List<Agent> agentsTemp = AgentsAdapter.getUnsyncedAgents(context);

            if(agentsTemp!=null&&agentsTemp.isEmpty()){
                //Successfully Synced all unsynced Jobs

                if(agents.size()==1){
                    onAgentsSynced.onSyncCompleted(String.valueOf(agents.get(0).getId()));
                }else{
                    onAgentsSynced.onSyncCompleted();
                }

            }else{
                //Failed to sync some jobs
                onAgentsSynced.onSyncFailed();
            }
        }
    }



    private class DownloadAgents  extends AsyncTask<String, Void, List<Agent>> {

        protected void onPreExecute() {

        }

        // Call after onPreExecute method
        protected List<Agent> doInBackground(String... urls) {

            System.out.println(urls[0]);

            String response =  requestContent(urls[0]);

            List<Agent> agents = new ArrayList<Agent>();

            try {

                if(response==null){
                    throw new JSONException("Response is Null");
                }

                JSONArray array = new JSONArray(response);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);

                    int id = jsonObject.getInt("id");
                    String name = jsonObject.getString("name");
                    String address = jsonObject.getString("address");
                    String officeID = jsonObject.getString("officeID");
                    String helpDeskPin = jsonObject.getString("helpDeskPin");
                    JSONObject obj = jsonObject.getJSONObject("group");
                    AgentGroup group = new AgentGroup(obj.getInt("id"), obj.getString("groupName"));

                    List<AgentContact> contacts = new ArrayList<>();

                    JSONArray contactArray = jsonObject.getJSONArray("contacts");
                    for(int j = 0; j < contactArray.length(); j++){

                        JSONObject contactObject = contactArray.getJSONObject(j);

                        int contact_id = contactObject.getInt("id");
                        String contact_name = contactObject.getString("name");
                        String contact_email = contactObject.getString("email");
                        String contact_number = contactObject.getString("number");
                        int agent_id = contactObject.getInt("agent_id");

                        contacts.add(new AgentContact(contact_id,contact_name,contact_email,contact_number,agent_id));
                    }

                    agents.add(new Agent(id, name, address, officeID, helpDeskPin, group, contacts,true));
                }

            } catch (JSONException e) {
                // manage exceptions
                System.out.println("Exception when parsing Json: "+e);
            }

            return agents;
        }

        protected void onPostExecute(List<Agent> agents) {


            if(agents!=null&&!agents.isEmpty()){
                System.out.println("Adding agents to db");
                AgentsAdapter.addAgents(agents,context);
                onAgentsDownloaded.onDownloadComplete();
            }else{
                onAgentsDownloaded.onDownloadFailed();
            }
        }





    }

    private class DownloadAgent extends AsyncTask<String, Void, Agent> {

        @Override
        protected void onPreExecute() {

        }

        // Call after onPreExecute method
        @Override
        protected Agent doInBackground(String... urls) {

            System.out.println(urls[0]);
            String response = doPostID(urls[0],urls[1]);

            Agent agent = null;
            try {

                if (response == null || response.equalsIgnoreCase("")) {
                    throw new JSONException("Response is Null");
                }

                JSONArray array = new JSONArray(response);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);

                    int id = jsonObject.getInt("id");
                    String name = jsonObject.getString("name");
                    String address = jsonObject.getString("address");
                    String officeID = jsonObject.getString("officeID");
                    String helpDeskPin = jsonObject.getString("helpDeskPin");
                    JSONObject obj = jsonObject.getJSONObject("group");
                    AgentGroup group = new AgentGroup(obj.getInt("id"), obj.getString("groupName"));

                    List<AgentContact> contacts = new ArrayList<>();

                    JSONArray contactArray = jsonObject.getJSONArray("contacts");
                    for(int j = 0; j < contactArray.length(); j++){

                        JSONObject contactObject = contactArray.getJSONObject(j);

                        int contact_id = contactObject.getInt("id");
                        String contact_name = contactObject.getString("name");
                        String contact_email = contactObject.getString("email");
                        String contact_number = contactObject.getString("number");
                        int agent_id = contactObject.getInt("agent_id");

                        contacts.add(new AgentContact(contact_id,contact_name,contact_email,contact_number,agent_id));
                    }

                   agent = new Agent(id, name, address, officeID, helpDeskPin, group, contacts,true);
                }

            } catch (JSONException e) {
                // manage exceptions
                System.out.println("Exception when parsing Json: " + e);
            }

            return agent;
        }

        @Override
        protected void onPostExecute(Agent agent) {
            if (agent != null) {
                System.out.println("Updating agent in DB");
                int updatedRows = AgentsAdapter.updateAgent(agent, context);
                if(updatedRows>0){
                    onAgentUpdateDownloaded.onDownloaded(agent);
                }else{
                    onAgentUpdateDownloaded.onFailedDownload();
                }
            } else {
                onAgentUpdateDownloaded.onFailedDownload();
            }
        }

        public String doPostID(String url , String id){

            String output = "";
            JSONObject jsonData = new JSONObject();
            try {
                jsonData.put("id",Integer.parseInt(id));
                System.out.println("Successfully parsed Agent to JSON");
                HttpClient httpclient;

                System.out.println("Sending Json : ");
                System.out.println(jsonData.toString());

                try {

                    //Set the timeout to 6 Seconds
                    final HttpParams httpParams = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
                    HttpConnectionParams.setSoTimeout(httpParams,8000);

                    httpclient = new DefaultHttpClient(httpParams);
                    HttpPost postRequest = new HttpPost(url);

                    StringEntity input = new StringEntity(jsonData.toString());
                    input.setContentType("application/json");
                    postRequest.setEntity(input);

                    HttpResponse response = httpclient.execute(postRequest);

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader((response.getEntity().getContent())));


                    String s;
                    System.out.println("Output from Server .... \n");
                    while ((s = br.readLine()) != null) {
                        output += s;
                    }
                    System.out.println(output);
                    httpclient.getConnectionManager().shutdown();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return output;

            } catch (JSONException e) {
                e.printStackTrace();
                return output;
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
            HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
            HttpConnectionParams.setSoTimeout(httpParams, 8000);

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
