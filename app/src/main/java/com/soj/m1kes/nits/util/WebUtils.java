package com.soj.m1kes.nits.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by m1kes on 7/21/2017.
 */

public class WebUtils {

    public static String requestContent(String url,int timeout) {

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
            // manage exceptions
            e.printStackTrace();
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

    public static String convertStreamToString(InputStream is) {
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
            }
        }

        return sb.toString();
    }

}
