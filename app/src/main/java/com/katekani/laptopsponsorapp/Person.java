package com.katekani.laptopsponsorapp;

import android.app.Application;


import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Code Tribe on 2017/08/24.
 */

public class Person extends Application {

    @Override
    public void onCreate() {super.onCreate();

     Firebase.setAndroidContext(this);

    }
}
