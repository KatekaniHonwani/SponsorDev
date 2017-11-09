package com.katekani.laptopsponsorapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClientAndSponsorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView notification_badge;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private String userID;
    private DatabaseReference mRef;
    private UserInformation userInformation;
    private static final String TAG = "ClientAndSponsor;";

    private TextView txtqoute1;
    private TextView txtTeam;
    private TextView txtphrase1;
    private ImageView imgView1,imgView2,imgView3,imgView4,imgView5,imgView6;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_and_sponsor);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtqoute1 = findViewById(R.id.txt1);
        txtphrase1 = findViewById(R.id.textPhrase1);
        imgView2= findViewById(R.id.img2);
        imgView3= findViewById(R.id.img3);
        imgView4= findViewById(R.id.img4);
        imgView5= findViewById(R.id.img5);
        imgView6= findViewById(R.id.img6);


        imgView2.setImageDrawable(getResources().getDrawable(R.drawable.naledi));
        imgView3.setImageDrawable(getResources().getDrawable(R.drawable.nokuthula));
        imgView4.setImageDrawable(getResources().getDrawable(R.drawable.makhosi));
        imgView5.setImageDrawable(getResources().getDrawable(R.drawable.katekani));
        imgView6.setImageDrawable(getResources().getDrawable(R.drawable.sethu));

        txtphrase1.setText (" As The Brainex we seek to aspire and contribute positivily to the startup developers," +
                "by giving them an oppotunity in this paltform to request for resources like, laptop and PCs.\n" +
                "We have discovered that there are many companies who are willing to sponsor the startup developers.\n" +
                "We aim to reach out to multiple organisations and help donate laptops to the in need developers.");



    DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    userID = user.getUid();
                    mRef = FirebaseDatabase.getInstance().getReference("Users").child(userID);
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {

                                userInformation = dataSnapshot.getValue(UserInformation.class);
                                assert userInformation != null;
                                //for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                                Menu menuNav = navigationView.getMenu();
                                if ("Client".equalsIgnoreCase(userInformation.getType())) {
                                    Log.i("Ygritte", userInformation.getType());
                                    MenuItem client_list = menuNav.findItem(R.id.nav_about_us);


                                } else if("Sponsor".equalsIgnoreCase(userInformation.getType())) {
                                    Log.i("Ygritte", userInformation.getType());
                                    MenuItem notification = menuNav.findItem(R.id.nav_notification);
                                    notification.setVisible(false);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                }
            }
        };




        notification_badge=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_notification));

        setNotificationCountDrawer();
    }

    private void setNotificationCountDrawer(){


        notification_badge.setGravity(Gravity.CENTER_VERTICAL);
        notification_badge.setTypeface(null, Typeface.BOLD);
        notification_badge.setTextColor(getResources().getColor(R.color.colorAccent));
        //count is added
        notification_badge.setText("7");
    }



    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.client_and_sponsor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.nav_update)
        {
            if("Client".equalsIgnoreCase(userInformation.getType()))
            {
                startActivity(new Intent(ClientAndSponsorActivity.this, UpdateClientProfileActivity.class));
            }
            else if("Sponsor".equalsIgnoreCase(userInformation.getType()))
            {
                startActivity(new Intent(ClientAndSponsorActivity.this,UpdateSponsorActivity.class));
            }

        } else if (id == R.id.nav_about_us) {
            startActivity(new Intent(ClientAndSponsorActivity.this, AboutUsActivity.class));
        }else if(id == R.id.nav_notification){

            startActivity(new Intent(ClientAndSponsorActivity.this, Notification.class));

        }else if (id == R.id.nav_signout) {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ClientAndSponsorActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
