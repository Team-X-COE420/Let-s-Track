package com.example.teamx.letstrack.Activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by arany on 08/12/2017.
 */

public class ContactRowAdapter extends ArrayAdapter {


    public ContactRowAdapter(@NonNull Context context, String[] emails) {
        super(context, R.layout.contact_row, emails);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(getContext());
        View customview = inflator.inflate(R.layout.contact_row, parent, false);

        String email = (String) getItem(position);
        TextView txtemail = customview.findViewById(R.id.txtEmail);
        Button accept = customview.findViewById(R.id.btnAccept);
        Button reject = customview.findViewById(R.id.btnReject);

        txtemail.setText(email);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return customview;
    }
}
