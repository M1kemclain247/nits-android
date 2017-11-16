package com.soj.m1kes.nits.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by m1kes on 11/14/2017.
 */

public class AgentContact implements Parcelable {

    private int id;
    private String name;
    private String number;
    private String email;
    private int agent_id;


    public AgentContact() {
    }

    public AgentContact(int id, String name, String number, String email, int agent_id) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.email = email;
        this.agent_id = agent_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    //write object values to parcel for storage
    public void writeToParcel(Parcel dest, int flags){
        //write all properties to the parcle
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(email);
        dest.writeInt(agent_id);
    }

    //constructor used for parcel
    public AgentContact(Parcel parcel){
        //read and set saved values from parcel
        id = parcel.readInt();
        name = parcel.readString();
        number = parcel.readString();
        email = parcel.readString();
        agent_id = parcel.readInt();
    }

    //creator - used when un-parceling our parcle (creating the object)
    public static final Parcelable.Creator<AgentContact> CREATOR = new Parcelable.Creator<AgentContact>(){

        @Override
        public AgentContact createFromParcel(Parcel parcel) {
            return new AgentContact(parcel);
        }

        @Override
        public AgentContact[] newArray(int size) {
            return new AgentContact[0];
        }
    };

    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }

    @Override
    public String toString() {
        return "AgentContact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", email='" + email + '\'' +
                ", agent_id=" + agent_id +
                '}';
    }

}
