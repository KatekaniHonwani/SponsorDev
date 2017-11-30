package com.katekani.laptopsponsorapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by codetribe on 11/16/2017.
 */

public class Devices implements Parcelable{

    String  uId, device_name, device_model, screen_size,storage,status,image;
    boolean isDonated =false;
    public long timestamp;
    public Devices(){

    }

    public Devices(String device_name, String device_model, String screen_size, String storage, String status, String image, boolean isDonated, long timestamp) {
        this.device_name = device_name;
        this.device_model = device_model;
        this.screen_size = screen_size;
        this.storage = storage;
        this.status = status;
        this.image = image;
        this.isDonated = isDonated;
        this.timestamp = timestamp;
    }

    public Map<String, Object> mapDevice() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uId", uId);
        result.put("device_model",device_model);
        result.put("device_name",device_name);
        result.put("donated", isDonated);
        result.put("screen_size", screen_size);
        result.put("status", status);
        result.put("storage", storage);
        return result;
    }

    protected Devices(Parcel in) {
        device_name= in.readString();
        device_model = in.readString();
        screen_size = in.readString();
        storage=in.readString();
        status=in.readString();


    }
    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(device_name);
        dest.writeString(device_model);
        dest.writeString(screen_size);
        dest.writeString(storage);
        dest.writeString(status);
        dest.writeString(image);
        dest.writeString(image);
        dest.writeString(image);
        dest.writeString(image);

    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_model() {
        return device_model;
    }

    public void setDevice_model(String device_description) {
        this.device_model = device_description;
    }

    public String getScreen_size() {
        return screen_size;
    }

    public void setScreen_size(String screen_size) {
        this.screen_size = screen_size;
    }
    public String getStorage() {
        return storage;
    }

    public void setStorage(String screen_size) {
        this.storage = screen_size;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String screen_size) {
        this.status = screen_size;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public boolean isDonated() {
        return isDonated;
    }

    public void setDonated(boolean donated) {
        isDonated = donated;
    }
}
