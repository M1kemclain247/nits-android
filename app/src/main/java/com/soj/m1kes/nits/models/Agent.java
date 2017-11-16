package com.soj.m1kes.nits.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Agent implements Comparable<Agent>,Parcelable {

    private int id;
    private String name;
    private String address;
    private String officeID;
    private String helpDeskPin;
    private AgentGroup group;
    private List<AgentContact> contacts = new ArrayList<>();

    private boolean isSynced;


    public Agent() { }

    public Agent(int id, String name, String address, String officeID, String helpDeskPin, AgentGroup group, List<AgentContact> contacts, boolean isSynced) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.officeID = officeID;
        this.helpDeskPin = helpDeskPin;
        this.group = group;
        this.contacts = contacts;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOfficeID() {
        return officeID;
    }

    public void setOfficeID(String officeID) {
        this.officeID = officeID;
    }

    public String getHelpDeskPin() {
        return helpDeskPin;
    }

    public void setHelpDeskPin(String helpDeskPin) {
        this.helpDeskPin = helpDeskPin;
    }

    public AgentGroup getGroup() {
        return group;
    }

    public void setGroup(AgentGroup group) {
        this.group = group;
    }

    public List<AgentContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<AgentContact> contacts) {
        this.contacts = contacts;
    }

    public void addContact(AgentContact c){ if(contacts.contains(c)) return; contacts.add(c); }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }


    //write object values to parcel for storage
    public void writeToParcel(Parcel dest, int flags){
        //write all properties to the parcle
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(officeID);
        dest.writeString(helpDeskPin);
        dest.writeParcelable(group,flags);
        dest.writeList(contacts);
    }

    //constructor used for parcel
    public Agent(Parcel parcel){
        //read and set saved values from parcel
        id = parcel.readInt();
        name = parcel.readString();
        address = parcel.readString();
        officeID = parcel.readString();
        helpDeskPin = parcel.readString();
        group = parcel.readParcelable(AgentGroup.class.getClassLoader());
        contacts = new ArrayList<>();
        parcel.readList(contacts,AgentContact.class.getClassLoader());
    }

    //creator - used when un-parceling our parcle (creating the object)
    public static final Parcelable.Creator<Agent> CREATOR = new Parcelable.Creator<Agent>(){

        @Override
        public Agent createFromParcel(Parcel parcel) {
            return new Agent(parcel);
        }

        @Override
        public Agent[] newArray(int size) {
            return new Agent[0];
        }
    };

    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }


    @Override
    public int compareTo(Agent o1) {
        return (this.name).compareTo(o1.name);
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", officeID='" + officeID + '\'' +
                ", helpDeskPin='" + helpDeskPin + '\'' +
                ", group=" + group +
                ", contacts=" + contacts +
                '}';
    }
}
