package com.soj.m1kes.nits.service;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.soj.m1kes.nits.MainActivity;
import com.soj.m1kes.nits.models.Agent;
import com.soj.m1kes.nits.models.Job;
import com.soj.m1kes.nits.service.callbacks.OnJobPosted;
import com.soj.m1kes.nits.service.callbacks.OnJobsDownloaded;
import com.soj.m1kes.nits.service.callbacks.OnPostUnsynced;
import com.soj.m1kes.nits.sqlite.adapters.AgentsAdapter;
import com.soj.m1kes.nits.sqlite.adapters.JobsAdapter;
import com.soj.m1kes.nits.util.WebResources;
import com.soj.m1kes.nits.util.WebUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
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
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m1kes on 7/21/2017.
 */

public class JobsService {


    private Context context;
    private OnJobsDownloaded onJobsDownloaded;
    private OnJobPosted onJobPosted;
    private OnPostUnsynced onPostUnsynced;

    public JobsService(Context context,OnJobsDownloaded onJobsDownloaded){
        this.context = context;
        this.onJobsDownloaded = onJobsDownloaded;
        new SyncJobs().execute(WebResources.getJobsUrl(context));
    }

    public JobsService(Context context, OnJobPosted onJobPosted){
        this.context = context;
        this.onJobPosted = onJobPosted;
    }

    public JobsService(Context context,OnPostUnsynced onPostUnsynced,List<Job> jobs){
        this.context = context;
        this.onPostUnsynced = onPostUnsynced;
        new PostUnsyncedJobs().execute(jobs);
    }


    public void PostJob(Job job){

        //Save into DB Locally
        JobsAdapter.addJob(job,context);

        JSONObject object = new JSONObject();
        try {
            object.put("name",job.getName());
            object.put("date",job.getDate());
            object.put("description",job.getDescription());
            object.put("priority",job.getPriority());
            object.put("details",job.getDetails());

            new PostJob(object,job).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public class SyncJobs extends AsyncTask<String,Void,List<Job>>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Job> doInBackground(String... params) {


            String response = WebUtils.requestContent(params[0],3000);

            List<Job> jobs = new ArrayList<>();

            try {

                if(response==null){
                    throw new JSONException("Response is Null");
                }

                JSONArray array =  new JSONArray(response);


                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);

                    int id =  jsonObject.getInt("id");
                    String name =  jsonObject.getString("name");
                    String date =  jsonObject.getString("date");
                    String description =  jsonObject.getString("description");
                    String priority =  jsonObject.getString("priority");
                    String details =  jsonObject.getString("details");
                    Job job = new Job(id,name,date,description,priority,details,true);

                    jobs.add(job);
                }

            } catch (JSONException e) {
                // manage exceptions
                System.out.println("Exception when parsing Json: "+e);
            }

            return jobs;
        }

        @Override
        protected void onPostExecute(List<Job> jobs) {

            if(jobs!=null&&!jobs.isEmpty()){
//                jobs.forEach(System.out::println);
                System.out.println("Adding Jobs to db");
                JobsAdapter.addJobs(jobs,context);
                Toast.makeText(context,"Jobs Updated!",Toast.LENGTH_SHORT).show();
                onJobsDownloaded.onDownloaded();
            }else{
                onJobsDownloaded.onFailed();
            }

        }
    }

    public class PostJob extends AsyncTask<Void,Void,Boolean>{

        private JSONObject jsonData;
        private Job job;

        public PostJob(JSONObject jsonData,Job job){
            this.jsonData = jsonData;
            this.job = job;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpClient httpclient;

            try {

                //Set the timeout to 6 Seconds
                final HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 4000);
                HttpConnectionParams.setSoTimeout(httpParams, 4000);

                httpclient = new DefaultHttpClient(httpParams);

                HttpPost postRequest = new HttpPost(WebResources.postJobUrl(context));

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

                if(output.contains("OK")){
                    return true;
                }else{
                    return false;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean posted) {
               if(posted){
                   System.out.println("Successfully Posted Job");
                   //Set it to synced
                   JobsAdapter.setJobSynced(job,context);
                   onJobPosted.onPosted();
               }else{
                   System.out.println("Failed to Post Job");
                   onJobPosted.onPostFailed();
               }
        }
    }


    public class PostUnsyncedJobs extends AsyncTask<List<Job>,Void,Void>{

        public PostUnsyncedJobs(){

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(List<Job>... jobs) {

            List<Job> unsyncedJobs = jobs[0];

            for(Job job : unsyncedJobs) {

               JSONObject jsonData = new JSONObject();
               try {
                   jsonData.put("name", job.getName());
                   jsonData.put("date", job.getDate());
                   jsonData.put("description", job.getDescription());
                   jsonData.put("priority", job.getPriority());
                   jsonData.put("details", job.getDetails());

                   System.out.println("Successfully parsed Job to JSON");
                   HttpClient httpclient;

                   try {

                       //Set the timeout to 6 Seconds
                       final HttpParams httpParams = new BasicHttpParams();
                       HttpConnectionParams.setConnectionTimeout(httpParams, 4000);
                       HttpConnectionParams.setSoTimeout(httpParams,4000);

                       httpclient = new DefaultHttpClient(httpParams);
                       HttpPost postRequest = new HttpPost(WebResources.postJobUrl(context));

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
                           JobsAdapter.setJobSynced(job, context);
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
        protected void onPostExecute(Void result) {


            List<Job> jobs = JobsAdapter.getAllUnsyncedJobs(context);

            if(jobs!=null&&jobs.isEmpty()){
                //Successfully Synced all unsynced Jobs
                onPostUnsynced.success();
            }else{
                //Failed to sync some jobs
                onPostUnsynced.failed();
            }
        }
    }





}
