package com.soj.m1kes.nits.service;

import android.content.Context;

import com.soj.m1kes.nits.models.AgentContact;
import com.soj.m1kes.nits.service.objects.ServiceManager;
import com.soj.m1kes.nits.sqlite.adapters.AgentContactsAdapter;
import com.soj.m1kes.nits.util.WebResources;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class AgentContactsService extends ServiceManager{


    public AgentContactsService(Context context) {
        super(context);
    }

    public void addContact(AgentContact a) {

        AgentContactsAdapter.addNewContact(a,getContext());

        JSONObject obj = new JSONObject();
        try {

            obj.put("id", a.getId());
            obj.put("name", a.getName());
            obj.put("number", a.getNumber());
            obj.put("email", a.getEmail());
            obj.put("agent_id", a.getAgent_id());

            postJsonObject(WebResources.postContactUrl(getContext()), obj, 8000);

            addBackgroundCallback(response -> {
                AgentContactsAdapter.setSynced(a,getContext());
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void syncUnsyncedContacts() {

        List<AgentContact> contacts = AgentContactsAdapter.getUnsyncedContacts(getContext());
        if (contacts.size() > 0) {
            for (AgentContact c : contacts) {
                addContact(c);
            }
        }
    }

}
