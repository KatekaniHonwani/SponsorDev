package com.katekani.laptopsponsorapp;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Code Tribe on 2017/08/28
 */

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.MyViewHolder>{

    private List<UserInformation> usersList;
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, role,surname;
        public CircleImageView imageView;

        public MyViewHolder(View view) {
            super(view);

            name =  view.findViewById(R.id.tvName);
            surname = view.findViewById(R.id.tvName);
            role =  view.findViewById(R.id.tvRole);
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
        holder.name.setText(userInformation.getUserName() +" " +userInformation.getUserSurname());
        holder.role.setText(userInformation.getRole());

        if (userInformation.getImage().isEmpty()) {
            holder.imageView.setImageResource((R.drawable.user_photo));
        }else {
            Picasso.with(context).load(Uri.parse(userInformation.getImage())).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

}