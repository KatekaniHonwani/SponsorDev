package com.katekani.laptopsponsorapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Code Tribe on 2017/08/25.
 */

public class UserInformation implements Parcelable {

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
    String regNo;

    //String variebles that will store user answers
    String answer1, answer2, answer3, answer4, answer5;





    public UserInformation()
    {

    }

    public UserInformation(String companyName, String contacts,String email,  String address,int quantity, String type) {
        this.email = email;
        this.companyName = companyName;
        this.address = address;
        this.contacts = contacts;
        this.quantity = quantity;
        this.type = type;
    }

    public UserInformation(String companyName, String email, String contacts, String address, int quantity, String type, String regNo)
    {
        this.companyName = companyName;
        this.email = email;
        this.contacts = contacts;
        this.address = address;
        this.type = type;
        this.quantity= quantity;
        this.regNo = regNo;
    }
    private HashMap<String, String> stats;


    public UserInformation(String name, String surname, String email,String address,String contacts, String gender, String type)
    {
        this.userName = name;
        this.userSurname = surname;
        this.email = email;
        this.contacts = contacts;
        this.address = address;
        this.gender = gender;
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

        return result;
    }

    public UserInformation(String name, String surname, String email,String address,String contacts, String gender,String regNo, String type,String answer1, String answer2,String answer3,String answer4,String answer5,String skills,String qualification)
    {
        this.userName = name;
        this.userSurname = surname;
        this.email = email;
        this.contacts = contacts;
        this.address = address;
        this.gender = gender;
        this.type = type;
        this.skills = skills;
        this.qualification = qualification;
        this.regNo = regNo;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer5 = answer5;

    }



    protected UserInformation(Parcel in) {
        userName = in.readString();
        userSurname = in.readString();
        email = in.readString();
        gender = in.readString();
        contacts =in.readString();
        address = in.readString();
        quantity = in.readInt();
        type = in.readString();
        skills = in.readString();
        qualification = in.readString();
        regNo =in.readString();

        //Information for question
        answer1 = in.readString();
        answer2 = in.readString();
        answer3 = in.readString();
        answer4 = in.readString();
        answer5 = in.readString();


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
        dest.writeInt(quantity);
        dest.writeString(type);
        dest.writeString(skills);
        dest.writeString(qualification);
        //user information regarding questions
        dest.writeString(answer1);
        dest.writeString(answer2);
        dest.writeString(answer3);
        dest.writeString(answer4);
        dest.writeString(answer5);
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getAnswer1() {return answer1;}

    public void setAnswer1(String answer1) {this.answer1 = answer1;}

    public String getAnswer2() {return answer2;}

    public void setAnswer2(String answer2) {this.answer2 = answer2;}

    public String getAnswer3() {return answer3;}

    public void setAnswer3(String answer3) {this.answer3 = answer3;}

    public String getAnswer4() {return answer4;}
    public void setAnswer4(String answer4) {this.answer4 = answer4;}

    public String getAnswer5() {return answer5;}

    public void setAnswer5(String answer5) {this.answer5 = answer5;}

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

}