package com.katekani.laptopsponsorapp;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        public TextView name,status,model,screenSize,storage;
       public ImageView img;
        RelativeTimeTextView tvTimestamp;

        public MyViewHolder(View view) {
            super(view);

            name =  view.findViewById(R.id.deciveName);
            img= view.findViewById(R.id.deviceImg);
            status = view.findViewById(R.id.decivestatus);
            model = view.findViewById(R.id.deciveModel);
            screenSize = view.findViewById(R.id.deciveScreenSize);
            storage = view.findViewById(R.id.deciveStorage);
            tvTimestamp = view.findViewById(R.id.timestamp);
        }
    }


    public DevicesAdapter(Context context,List<Devices> deviceList) {
        this.deviceList = deviceList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_row_xml, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Devices devices = deviceList.get(position);

        holder.name.setText(devices.getDevice_name());
        holder.status.setText(devices.getStatus());
        holder.model.setText(devices.getDevice_model());
        holder.screenSize.setText(devices.getScreen_size());
        holder.storage.setText(devices.getStorage());

        if (devices.getImage().isEmpty()) {
            holder.img.setImageResource(R.drawable.user_photo);
        }else {
            Picasso.with(context).load(Uri.parse(devices.getImage())).into(holder.img );
        }

       // holder.tvTimestamp.setReferenceTime(1511958012000L);

       holder.tvTimestamp.setReferenceTime(devices.getTimestamp());

    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

}
