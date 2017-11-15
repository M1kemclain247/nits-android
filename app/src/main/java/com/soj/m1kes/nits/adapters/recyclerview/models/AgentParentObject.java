package com.soj.m1kes.nits.adapters.recyclerview.models;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;



public class AgentParentObject implements ParentObject {


    private String name;


    private List<Object> mChildrenList;

    public AgentParentObject(String name) {
        this.name = name;
    }

    /**
     * Your constructor and any other accessor
     * methods should go here.
     */



    @Override
    public List<Object> getChildObjectList() {
        return mChildrenList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        mChildrenList = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
