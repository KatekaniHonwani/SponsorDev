package com.katekani.laptopsponsorapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Code Tribe on 2017/08/25.
 */

public class UserInformation extends DeveloperAnswers implements Parcelable {

    String uId;
    String userName;
    String userSurname;
    String email;
    String companyName;
    String address;
    String gender;
    String contacts;
    int quantity;
    String type;
    String skills;
    String qualification;
    String role;
    String regNo;
    String image = "";
    String imageResourceId = "";
    //String variebles that will store user answers





    public UserInformation()
    {

    }


    public UserInformation(String companyName, String contacts,String email,  String address, String type) {
        this.email = email;
        this.companyName = companyName;
        this.address = address;
        this.contacts = contacts;
        this.type = type;
    }

    public UserInformation(String companyName, String email, String contacts, String address, String type, String regNo)
    {
        this.companyName = companyName;
        this.email = email;
        this.contacts = contacts;
        this.address = address;
        this.type = type;
        this.regNo = regNo;
    }
    private HashMap<String, String> stats;


    public UserInformation(String name, String surname, String email,String address,String contacts, String gender,String role, String type)
    {
        this.userName = name;
        this.userSurname = surname;
        this.email = email;
        this.contacts = contacts;
        this.address = address;
        this.gender = gender;
        this.role = role;
        this.type = type;

    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uId", uId);
        result.put("userName",userName);
        result.put("userSurname",userSurname);
        result.put("email", email);
        result.put("contacts", contacts);
        result.put("address", address);
        result.put("gender", gender);
        result.put("type", type);
        result.put("role", role);

        return result;
    }
    public Map<String, Object> map() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uId", uId);
        result.put("email",email);
        result.put("companyName",companyName);
        result.put("contacts", contacts);
        result.put("type", type);

        return result;
    }

    public UserInformation(String name, String surname, String email,String imageResourceId, String address,String contacts, String gender,String regNo, String type)
    {
        this.userName = name;
        this.userSurname = surname;
        this.email = email;
        this.image = imageResourceId;
        this.contacts = contacts;
        this.address = address;
        this.gender = gender;
        this.type = type;
        this.regNo = regNo;


    }



    protected UserInformation(Parcel in) {
        userName = in.readString();
        userSurname = in.readString();
        email = in.readString();
        gender = in.readString();
        contacts =in.readString();
        address = in.readString();
        role = in.readString();
        type = in.readString();
        regNo =in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userSurname);
        dest.writeString(companyName);
        dest.writeString(email);
        dest.writeString(gender);
        dest.writeString(contacts);
        dest.writeString(address);
        dest.writeString(role);
        dest.writeString(type);
        dest.writeString(image);
        dest.writeString(regNo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInformation> CREATOR = new Creator<UserInformation>() {
        @Override
        public UserInformation createFromParcel(Parcel in) {
            return new UserInformation(in);
        }

        @Override
        public UserInformation[] newArray(int size) {
            return new UserInformation[size];
        }
    };
    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {this.email = email;}

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
