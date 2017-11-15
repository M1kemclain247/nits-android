package com.soj.m1kes.nits.models;

import java.io.Serializable;

/**
 * Created by m1kes on 11/14/2017.
 */

public class AgentContact implements Serializable {

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
