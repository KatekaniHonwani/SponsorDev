package com.katekani.laptopsponsorapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.MyViewHolder>{

    private List<UserInformation> usersList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, gender;

        public MyViewHolder(View view) {
            super(view);
            name =  view.findViewById(R.id.tvName);
            gender =  view.findViewById(R.id.tvgender);

        }
    }

    public ClientAdapter(List<UserInformation> usersList) {
        this.usersList = usersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserInformation userInformation = usersList.get(position);
        holder.name.setText(userInformation.getUserName());
        holder.gender.setText(userInformation.getGender());

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

}