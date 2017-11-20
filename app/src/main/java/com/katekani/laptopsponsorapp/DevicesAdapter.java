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
 * Created by codetribe on 11/16/2017.
 */

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.MyViewHolder> {

    private List<Devices> deviceList;

    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, role;
        public CircleImageView imageView;

        public MyViewHolder(View view) {
            super(view);

            name =  view.findViewById(R.id.tvName);
            role =  view.findViewById(R.id.tvRole);
            imageView= view.findViewById(R.id.img4);
        }
    }

    public DevicesAdapter(Context context,List<Devices> deviceList) {
        this.deviceList = deviceList;
        this.context=context;
    }

    @Override
    public DevicesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_layout, parent, false);

        return new DevicesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Devices devices = deviceList.get(position);
        holder.name.setText(devices.getDevice_name());
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }
}
