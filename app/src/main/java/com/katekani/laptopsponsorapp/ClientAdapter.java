package com.katekani.laptopsponsorapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Code Tribe on 2017/08/28.
 */

public class ClientAdapter extends ArrayAdapter<UserInformation> {

    Context context;
    private List<UserInformation> items;
    private int resource;

    public ClientAdapter(Context context, int resource, List<UserInformation> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
        this.resource = resource;
    }

    @Override
    public UserInformation getItem(int position) {
        if (this.items != null) {
            return this.items.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        if (this.items != null) {
            return this.items.size();
        } else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, null);
        }


        //ImageView clientImage =  convertView.findViewById(R.id.imageView);
        TextView clientName = convertView.findViewById(R.id.tvName);
        TextView gender = convertView.findViewById(R.id.tvgender);

        UserInformation userInformation = getItem(position);
        Log.i("Ygritte",userInformation.getUserName());
        //boolean isPhoto = message.getPhotoUrl() != null;

        //if (isPhoto) {

        //clientName.setVisibility(View.GONE);
        //gender.setVisibility(View.GONE);
        //clientImage.setVisibility(View.VISIBLE);
        //Glide.with(clientImage.getContext())
        //.load(userInformation.getPhotoUrl())
        //.into(clientImage);
        //} else {
        //clientName.setVisibility(View.VISIBLE);
        //gender.setVisibility(View.VISIBLE);
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

        return convertView;
    }


}