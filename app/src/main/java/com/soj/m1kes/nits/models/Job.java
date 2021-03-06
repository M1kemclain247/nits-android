package com.soj.m1kes.nits.models;

/**
 * Created by m1kes on 7/20/2017.
 */

public class Job {

    private int id;
    private String name;
    private String date;
    private String description;
    private String priority;
    private String details;
    private boolean isSynced;

    public Job() {
    }

    public Job(int id, String name, String date, String description, String priority, String details, boolean isSynced) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.description = description;
        this.priority = priority;
        this.details = details;
        this.isSynced = isSynced;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", priority='" + priority + '\'' +
                ", details='" + details + '\'' +
                ", isSynced=" + isSynced +
                '}';
    }
}
