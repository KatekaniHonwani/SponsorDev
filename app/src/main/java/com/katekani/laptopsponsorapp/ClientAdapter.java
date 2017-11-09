package com.katekani.laptopsponsorapp;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Code Tribe on 2017/08/28.
 */

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.MyViewHolder>{

    private List<UserInformation> usersList;
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, gender;
        public CircleImageView imageView;

        public MyViewHolder(View view) {
            super(view);

            name =  view.findViewById(R.id.tvName);
            gender =  view.findViewById(R.id.tvgender);
            imageView= view.findViewById(R.id.img4);
        }
    }

    public ClientAdapter(Context context,List<UserInformation> usersList) {
        this.usersList = usersList;
        this.context=context;
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
        holder.imageView.setImageURI(Uri.parse(userInformation.getImageResourceId()));

        Log.i("Ygritte", userInformation.getImageResourceId());
        if (userInformation.getImageResourceId().isEmpty()) {
            holder.imageView.setImageResource((R.drawable.user_photo));
        }else {
            Picasso.with(context).load(Uri.parse(userInformation.getImageResourceId())).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

}