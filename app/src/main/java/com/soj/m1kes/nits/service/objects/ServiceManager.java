package com.soj.m1kes.nits.service.objects;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.soj.m1kes.nits.service.AgentContactsService;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class ServiceManager {

    private Context context;
    private List<ServiceCallback> callbacks = new ArrayList<>();
    private List<ServiceChildCallback> childCallbacks = new ArrayList<>();


    public ServiceManager(Context context){
        this.context = context;
    }



    public void postJsonObject(String url,JSONObject obj,int timeout){
        new Poster(url,obj,timeout).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @SuppressLint("StaticFieldLeak")
    public class Poster extends AsyncTask<Void,String,String>{

        private JSONObject obj;
        private int timeout;
        private String url;

        public Poster(String url, JSONObject obj,int timeout){
            this.obj = obj;
            this.timeout = timeout;
            this.url = url;
        }

        public Poster(){}

        @Override
        protected String doInBackground(Void... voids) {
            if(obj != null && timeout != 0 && url != null)
                return postJson(url,obj,timeout);
            else
                return null;
        }

        @Override
        protected void onPostExecute(String content) {
            update(content);
        }
    }

    protected String postJson(String url, JSONObject obj,int timeout){

        HttpClient httpclient;

        System.out.println("Sending Json : ");
        System.out.println(obj.toString());
        String output = "";
        try {

            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
            HttpConnectionParams.setSoTimeout(httpParams,timeout);

            httpclient = new DefaultHttpClient(httpParams);
            HttpPost postRequest = new HttpPost(url);

            StringEntity input = new StringEntity(obj.toString());
            input.setContentType("application/json");
            postRequest.setEntity(input);

            HttpResponse response = httpclient.execute(postRequest);

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

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

    }

    protected String requestContent(String url,int timeout) {

        HttpClient httpclient;
        String result = null;
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = null;
        InputStream instream = null;

        try {

            //Set the timeout to 6 Seconds
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
            HttpConnectionParams.setSoTimeout(httpParams, timeout);

            httpclient = new DefaultHttpClient(httpParams);

            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                instream = entity.getContent();
                result = convertStreamToString(instream);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (instream != null) {
                try {
                    instream.close();
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        }

        return result;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public Context getContext() {
        return context;
    }

    public void addCallback(ServiceCallback callback){
        if(callbacks.contains(callback)) return;
        callbacks.add(callback);
    }

    protected void addBackgroundCallback(ServiceChildCallback cb){
        if(childCallbacks.contains(cb)) return;
        childCallbacks.add(cb);
    }

    public void update(String response){

        for(ServiceCallback cb : getCallbacks()){
            if(cb == null ) return;
            cb.onCompleted(response);
        }

        for(ServiceChildCallback cb : childCallbacks){
            if(cb == null) return;
            cb.onResponse(response);
        }
    }

    private List<ServiceCallback> getCallbacks() {
        return callbacks;
    }
}
