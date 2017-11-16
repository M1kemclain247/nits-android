package com.soj.m1kes.nits.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class AgentGroup implements Parcelable {

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


    //write object values to parcel for storage
    public void writeToParcel(Parcel dest, int flags){
        //write all properties to the parcle
        dest.writeInt(id);
        dest.writeString(groupName);
    }

    //constructor used for parcel
    public AgentGroup(Parcel parcel){
        //read and set saved values from parcel
        id = parcel.readInt();
        groupName = parcel.readString();
    }

    //creator - used when un-parceling our parcle (creating the object)
    public static final Parcelable.Creator<AgentGroup> CREATOR = new Parcelable.Creator<AgentGroup>(){

        @Override
        public AgentGroup createFromParcel(Parcel parcel) {
            return new AgentGroup(parcel);
        }

        @Override
        public AgentGroup[] newArray(int size) {
            return new AgentGroup[0];
        }
    };

    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }

    @Override
    public String toString() {
        return "AgentGroup{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
