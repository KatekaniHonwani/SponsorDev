package com.katekani.laptopsponsorapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    // private Button btnContinue;
    private static int SPLASH_TIME_OUT = 3000;
    private ProgressBar progressBar;
    private int progressBarStatus = 0;
    private Handler handler = new Handler();


    private FirebaseAuth firebaseAuth;
    private UserInformation userInformation;
    private DatabaseReference mRefDeveloper;
    private DatabaseReference mRef;
    private static final String TAG = "MyActivity";
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser user;
    private String userID;
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);


        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userID = user.getUid();
            mRef = FirebaseDatabase.getInstance().getReference("Users").child(userID);
            mRefDeveloper = FirebaseDatabase.getInstance().getReference("Developer_answers").child(userID);
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {

                        userInformation = dataSnapshot.getValue(UserInformation.class);
                        //assert userInformation != null;
                        if ("Client".equalsIgnoreCase(userInformation.getType())) {
                            mRefDeveloper.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot1) {

                                    Log.v("Ygritte", dataSnapshot1.toString());

                                    if (dataSnapshot1.getChildren() != null) {
                                        if (dataSnapshot1.hasChild("site_name") && dataSnapshot1.hasChild("adress_link") && dataSnapshot1.hasChild("current_computer") && dataSnapshot1.hasChild("developer_bio") && dataSnapshot1.hasChild("new_device") && dataSnapshot1.hasChild("qualification") && dataSnapshot1.hasChild("skills")) {
                                            startActivity(new Intent(MainActivity.this, ClientAndSponsorActivity.class));
                                        } else {
                                            startActivity(new Intent(MainActivity.this, ClientActivity.class));
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, "Returns empty values", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        } else if ("Sponsor".equalsIgnoreCase(userInformation.getType())) {
                            startActivity(new Intent(MainActivity.this, ClientAndSponsorActivity.class));
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value

                }
            });
        } else {
             /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    MainActivity.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);

        }

        if (!isConnected(MainActivity.this))builder(MainActivity.this).show();
        else
        {

            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            },SPLASH_TIME_OUT);

            //setContentView(R.layout.activity_main);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (progressBarStatus<1000)
                    {
                        progressBarStatus++;
                        android.os.SystemClock.sleep(50);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(progressBarStatus);
                            }
                        });
                    }

                }
            }).start();
            */
        }

    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null & wifi.isConnectedOrConnecting()))
                return true;

            else return false;
        } else {
            return false;
        }
    }

    public AlertDialog.Builder builder(Context c) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(c);
        alertBuilder.setTitle("No internet connection");
        alertBuilder.setMessage("You have to be connected to the internet to use the app.Press Ok to Exit");

        alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                finish();
            }
        });
        return alertBuilder;

    }

}
