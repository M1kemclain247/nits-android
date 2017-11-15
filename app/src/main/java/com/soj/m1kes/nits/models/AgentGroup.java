package com.soj.m1kes.nits.models;

import java.io.Serializable;

/**
 * Created by Michael on 7/31/2017.
 */

public class AgentGroup implements Serializable{

    private int id;
    private String groupName;

    public AgentGroup() {
    }

    public AgentGroup(int id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "AgentGroup{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
