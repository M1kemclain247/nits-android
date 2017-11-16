package com.soj.m1kes.nits.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.soj.m1kes.nits.R;
import com.soj.m1kes.nits.models.AgentContact;

public class AddContactDialog extends android.app.DialogFragment {

    public interface ClickListener {
         void onOk(DialogFragment dialog, AgentContact contact);
         void onCancel(DialogFragment dialog);
    }

    private ClickListener mListener;
    private String name = null;
    private String number = null;
    private int agent_id = 0;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AuthorizeUserDialogListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            name = getArguments().getString("name");
            number = getArguments().getString("number");
            agent_id = getArguments().getInt("agent_id");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_contact, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = null;
        Dialog d = null;

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.fragment_add_contact, null);
        final EditText editName,editNumber;

        editName = (EditText)view.findViewById(R.id.txtContactName);
        editNumber = (EditText)view.findViewById(R.id.txtContactNumber);

        if(name != null && number !=null){
            editName.setText(name);
            editNumber.setText(number);
        }

        builder.setView(view);
        builder.setIcon(R.drawable.ic_add_circle_outline_black_36dp);
        builder.setTitle("Add Contact");
        builder.setNegativeButton("Cancel", (dialog, which) -> {

        });
        builder.setPositiveButton("Add Contact", (dialog, which) -> {

        });
        builder.setCancelable(false);

        d = builder.create();
        builder.setCancelable(false);
        return d;
    }

    @Override
    public void onStart() {
        super.onStart();

        final AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {

            Button positiveButton = d.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                EditText editName = (EditText) d.findViewById(R.id.txtContactName);
                EditText editNumber = (EditText) d.findViewById(R.id.txtContactNumber);
                EditText editEmail = (EditText) d.findViewById(R.id.txtContactEmail);

                String name = editName.getText().toString();
                String number = editNumber.getText().toString();
                String email = editEmail.getText().toString();

                mListener.onOk(AddContactDialog.this, new AgentContact(0,name,number,email,agent_id));

            });

            Button negativeButton = d.getButton(AlertDialog.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(v -> mListener.onCancel(AddContactDialog.this));
            d.setCancelable(false);
        }
    }
}

