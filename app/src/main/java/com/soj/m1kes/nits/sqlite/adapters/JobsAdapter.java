package com.soj.m1kes.nits.sqlite.adapters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.soj.m1kes.nits.models.Agent;
import com.soj.m1kes.nits.models.Job;
import com.soj.m1kes.nits.sqlite.providers.DatabaseContentProvider;
import com.soj.m1kes.nits.sqlite.tables.AgentsTable;
import com.soj.m1kes.nits.sqlite.tables.JobsTable;
import com.soj.m1kes.nits.util.DBUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m1kes on 7/21/2017.
 */

public class JobsAdapter {



    public static List<Job> getAllJobs(Context context){

        List<Job> jobs = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query (DatabaseContentProvider.JOBS_CONTENT_URI , null, null, null, null);

        if (null != cursor && !(cursor.getCount() < 1)) {
            while (cursor.moveToNext()) {
                // Gets the value from the column.
                Job job = new Job();

                int id = cursor.getInt(cursor.getColumnIndex(JobsTable.ID));
                String name = cursor.getString (cursor.getColumnIndex (JobsTable.NAME));
                String date = cursor.getString (cursor.getColumnIndex (JobsTable.DATE));
                String description = cursor.getString (cursor.getColumnIndex (JobsTable.DESCRIPTION));
                String priority = cursor.getString (cursor.getColumnIndex (JobsTable.PRIORITY));
                String details = cursor.getString (cursor.getColumnIndex (JobsTable.DETAILS));
                boolean isSynced = DBUtils.convertIntToBool(cursor.getInt(cursor.getColumnIndex(JobsTable.IS_SYNCED)));

                job.setId(id);
                job.setName(name);
                job.setDate(date);
                job.setDescription(description);
                job.setPriority(priority);
                job.setDetails(details);
                job.setSynced(isSynced);

                System.out.println("Loading from DB: "+job.toString());

                jobs.add(job);
            }
            cursor.close();
        }

        return jobs;
    }


    public static List<Job> getAllUnsyncedJobs(Context context){

        List<Job> jobs = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query (DatabaseContentProvider.JOBS_CONTENT_URI , null,
                JobsTable.IS_SYNCED + " = " + 0 + "", null, null);

        if (null != cursor && !(cursor.getCount() < 1)) {
            while (cursor.moveToNext()) {
                Job job = new Job();

                int id = cursor.getInt(cursor.getColumnIndex(JobsTable.ID));
                String name = cursor.getString (cursor.getColumnIndex (JobsTable.NAME));
                String date = cursor.getString (cursor.getColumnIndex (JobsTable.DATE));
                String description = cursor.getString (cursor.getColumnIndex (JobsTable.DESCRIPTION));
                String priority = cursor.getString (cursor.getColumnIndex (JobsTable.PRIORITY));
                String details = cursor.getString (cursor.getColumnIndex (JobsTable.DETAILS));
                boolean isSynced = DBUtils.convertIntToBool(cursor.getInt(cursor.getColumnIndex(JobsTable.IS_SYNCED)));

                job.setId(id);
                job.setName(name);
                job.setDate(date);
                job.setDescription(description);
                job.setPriority(priority);
                job.setDetails(details);
                job.setSynced(isSynced);

                System.out.println("Loading from DB: "+job.toString());

                jobs.add(job);
            }
            cursor.close();
        }

        return jobs;
    }


    public static void addJob(Job job, Context context) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(JobsTable.ID, job.getId());
        initialValues.put(JobsTable.NAME,job.getName());
        initialValues.put(JobsTable.DATE, job.getDate());
        initialValues.put(JobsTable.DESCRIPTION, job.getDescription());
        initialValues.put(JobsTable.PRIORITY, job.getPriority());
        initialValues.put(JobsTable.DETAILS, job.getDetails());
        initialValues.put(JobsTable.IS_SYNCED,DBUtils.convertBoolToInt(job.isSynced()));

        System.out.println("Adding Job : "+job.toString());
        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI, JobsTable.TABLE_JOBS);
        Uri resultUri = context.getContentResolver().insert(contentUri, initialValues);

        System.out.println("Added An Job Successfully");

    }

    public static void addJobs(List<Job> jobs,Context context){
        deleteAll(context);
        for(Job j : jobs){
            addJob(j,context);
        }
    }

    public static void deleteAll(Context context){

        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI, JobsTable.TABLE_JOBS);
        int result = context.getContentResolver().delete(contentUri,null,null);
        System.out.println("Deleted status: "+result);
    }

    public static int setJobSynced(Job job,Context context){

        ContentValues updateValues = new ContentValues();
        updateValues.put(JobsTable.IS_SYNCED,DBUtils.convertIntToBool(1));
        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI,JobsTable.TABLE_JOBS);

        int rowsAffected =  context.getContentResolver().update(contentUri,updateValues,
                JobsTable.NAME+"=?",new String[]{job.getName()});

        if(rowsAffected>0){
            System.out.println("Successfully set Job to Synced ");
        }else{
            System.out.println("Failed to set Job to synced");
        }

        return rowsAffected;
    }

    public static boolean deleteJob(Job job,Context context){
        System.out.println("Trying to delete Job from local db with Job Name : "+job.getName());
        Uri contentUri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI, JobsTable.TABLE_JOBS);
        int result = context.getContentResolver().delete(contentUri,
                JobsTable.NAME+"=?",new String[]{job.getName()});
        System.out.println("Rows Affected: "+result);
        if(result>0){
            System.out.println("Successfully deleted Job from local DB");
            return true;
        }else{
            System.out.println("Failed to delete Job from local DB");
            return false;
        }
    }

}
