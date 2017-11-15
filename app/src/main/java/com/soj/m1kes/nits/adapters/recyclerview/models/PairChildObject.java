package com.soj.m1kes.nits.adapters.recyclerview.models;


import com.soj.m1kes.nits.models.ConnectionMode;

import java.util.Arrays;

public class PairChildObject {


    private int id;
    private String name;
    private String ip;
    private String model;
    private String connectionModes;
    private String port;

    public PairChildObject() {
    }

    public PairChildObject(int id, String name, String ip) {
        this.id = id;
        this.name = name;
        this.ip = ip;
    }

    public PairChildObject(int id, String name, String ip, String model, String connectionModes, String port) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.model = model;
        this.connectionModes = connectionModes;
        this.port = port;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getConnectionModes() {
        return connectionModes;
    }

    public void setConnectionModes(String connectionModes) {
        this.connectionModes = connectionModes;
    }

    @Override
    public String toString() {
        return "PairChildObject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", model='" + model + '\'' +
                ", connectionModes='" + connectionModes + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}