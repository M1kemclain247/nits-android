package com.soj.m1kes.nits.adapters.recyclerview.models;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

/**
 * Created by m1kes on 7/20/2017.
 */

public class JobsParentObject implements ParentObject {

    private String name;


    private List<Object> mChildrenList;

    public JobsParentObject(String name) {
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
