package com.katekani.laptopsponsorapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Code Tribe on 2017/08/28.
 */

public class ClientAdapter extends ArrayAdapter<UserInformation> {



    public ClientAdapter(Context context, int resource, List<UserInformation> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.adapter_layout, null);
        }

        //ImageView clientImage = (ImageView) v.findViewById(R.id.imageView);
        TextView clientName = (TextView) v.findViewById(R.id.tvName);
        TextView gender = (TextView) v.findViewById(R.id.tvgender);

        UserInformation userInformation = getItem(position);
        //boolean isPhoto = message.getPhotoUrl() != null;

        //if (isPhoto) {

            clientName.setVisibility(View.GONE);
            gender.setVisibility(View.GONE);
            //clientImage.setVisibility(View.VISIBLE);
            //Glide.with(clientImage.getContext())
                    //.load(userInformation.getPhotoUrl())
                    //.into(clientImage);
        //} else {
            clientName.setVisibility(View.VISIBLE);
            gender.setVisibility(View.VISIBLE);
            //clientImage.setVisibility(View.GONE);
            clientName.setText(userInformation.getUserName());
            gender.setText(userInformation.getGender());

        //}


           /* if (clientImage != null) {
                //clientImage.setId(userInformation.getImage());
            }

            if (clientName != null) {

            }

            if (gender != null) {

            }
        }*/

        return v;
    }


}