package com.katekani.laptopsponsorapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.snapshot.BooleanNode;

/**
 * Created by codetribe on 11/16/2017.
 */

public class Devices implements Parcelable{

    String device_name, device_description, screen_size;

    public Devices(){

    }
    public Devices (String device_name,String device_description,String screen_size) {
        this.device_name = device_name;
        this.device_description = device_description;
        this.screen_size = screen_size;

    }

    protected Devices(Parcel in) {
        device_name= in.readString();
        device_description = in.readString();
        screen_size = in.readString();

    }
    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(device_name);
        dest.writeString(device_description);
        dest.writeString(screen_size);
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_description() {
        return device_description;
    }

    public void setDevice_description(String device_description) {
        this.device_description = device_description;
    }

    public String getScreen_size() {
        return screen_size;
    }

    public void setScreen_size(String screen_size) {
        this.screen_size = screen_size;
    }


    public static final Creator<Devices> CREATOR = new Creator<Devices>() {
        @Override
        public Devices createFromParcel(Parcel in) {
            return new Devices(in);
        }

        @Override
        public Devices[] newArray(int size) {
            return new Devices[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


}
