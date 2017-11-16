package com.katekani.laptopsponsorapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by codetribe on 11/16/2017.
 */

public class DeveloperAnswers implements Parcelable {

    String site_name, adress_link, current_computer, developer_bio, new_device, qualification, skills;

    public DeveloperAnswers() {

    }

    public DeveloperAnswers(String site_name, String adress_link, String current_computer, String developer_bio, String new_device, String qualification, String skills) {
        this.site_name = site_name;
        this.adress_link = adress_link;
        this.current_computer = current_computer;
        this.developer_bio = developer_bio;
        this.new_device = new_device;
        this.qualification = qualification;
        this.skills = skills;
    }

    protected DeveloperAnswers(Parcel in) {
        site_name =in.readString();
        adress_link =in.readString();
        current_computer =in.readString();
        developer_bio  = in.readString();
        new_device = in.readString();
        qualification = in.readString();
        skills =in.readString();
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getAdress_link() {
        return adress_link;
    }

    public void setAdress_link(String adress_link) {
        this.adress_link = adress_link;
    }

    public String getCurrent_computer() {
        return current_computer;
    }

    public void setCurrent_computer(String current_computer) {
        this.current_computer = current_computer;
    }

    public String getDeveloper_bio() {
        return developer_bio;
    }

    public void setDeveloper_bio(String developer_bio) {
        this.developer_bio = developer_bio;
    }

    public String getNew_device() {
        return new_device;
    }

    public void setNew_device(String new_device) {
        this.new_device = new_device;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public static final Creator<DeveloperAnswers> CREATOR = new Creator<DeveloperAnswers>() {
        @Override
        public DeveloperAnswers createFromParcel(Parcel in) {
            return new DeveloperAnswers(in);
        }

        @Override
        public DeveloperAnswers[] newArray(int size) {
            return new DeveloperAnswers[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
