package com.soj.m1kes.nits.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.soj.m1kes.nits.R;
import com.soj.m1kes.nits.prefmanagers.IpPrefManager;

/**
 * Created by m1kes on 7/19/2017.
 */

public class ChangeIPDialog extends DialogFragment {


    public interface ClickListener {
        public void onChangeClick(DialogFragment dialog, EditText editIp);
        public void onCancelClick(DialogFragment dialog);
    }

    ClickListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ClickListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement AuthorizeUserDialogListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_dialog_ip, container, false);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = null;
        Dialog d = null;

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final EditText editIp ;
        TextView txtCurrentLoggedInUser,marshalUp_txtVehiclesIn;
        Button btnWork,btnHome;


        View view = layoutInflater.inflate(R.layout.custom_dialog_ip, null);
        editIp = (EditText) view.findViewById(R.id.editIp);
        editIp.setText(IpPrefManager.getIpAddress(getActivity()));

        btnHome =(Button)view.findViewById(R.id.btnHome);
        btnWork = (Button)view.findViewById(R.id.btnWork);


        btnHome.setOnClickListener(v -> editIp.setText(IpPrefManager.IPS.get(IpPrefManager.HOME_IP)));
        btnWork.setOnClickListener(v -> editIp.setText(IpPrefManager.IPS.get(IpPrefManager.WORK_IP)));


        builder.setView(view);
        builder.setIcon(R.drawable.ic_web_black_24dp);
        builder.setTitle("IP Address");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(false);

        d = builder.create();
        return d;
    }

    @Override
    public void onStart() {
        super.onStart();

        final AlertDialog d = (AlertDialog) getDialog();
        if(d!=null){

            Button positiveButton = d.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editIp = (EditText) d.findViewById(R.id.editIp);
                    mListener.onChangeClick(ChangeIPDialog.this,editIp);
                }
            });

            Button negativeButton = d.getButton(AlertDialog.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCancelClick(ChangeIPDialog.this);
                }
            });


        }


    }

}
