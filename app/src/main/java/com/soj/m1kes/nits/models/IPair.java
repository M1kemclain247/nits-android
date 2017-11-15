package com.soj.m1kes.nits.models;



public class IPair {

    private int id;
    private String name;
    private String ip;
    private String model;
    private String connectionModes;
    private String port;
    private boolean isSynced;

    public IPair() {
    }

    public IPair(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public IPair(int id, String name, String ip) {
        this.id = id;
        this.name = name;
        this.ip = ip;
    }

    public IPair(int id, String name, String ip,boolean isSynced) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.isSynced = isSynced;
    }

    public IPair(int id, String name, String ip, String model, String connection_modes, String port, boolean isSynced) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.model = model;
        this.connectionModes = connection_modes;
        this.port = port;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getConnectionModes() {
        return connectionModes;
    }

    public void setConnectionModes(String connectionModes) {
        this.connectionModes = connectionModes;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }


    @Override
    public String toString() {
        return "IPair{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", model='" + model + '\'' +
                ", connectionModes='" + connectionModes + '\'' +
                ", port='" + port + '\'' +
                ", isSynced=" + isSynced +
                '}';
    }
}
