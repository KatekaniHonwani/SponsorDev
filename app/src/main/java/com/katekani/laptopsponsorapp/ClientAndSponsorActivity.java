package com.katekani.laptopsponsorapp;

import android.content.Context;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientAndSponsorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView notification_badge;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private String userID;
    private DatabaseReference mRef;
    private static final String TAG = "ClientAndSponsor;";

    Context context;
    private ClientAdapter clientAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private ValueEventListener valueEventListener;
    UserInformation userInformation;
    List<UserInformation> allUsers = new ArrayList<>();
    private RecyclerView recyclerView;
    private ClientAdapter cAdapter;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_and_sponsor);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        recyclerView = findViewById(R.id.recycler_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);

        recyclerView.setAdapter(cAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                UserInformation userInformation = allUsers.get(position);
                Toast.makeText(getApplicationContext(), userInformation.getUserSurname() + " is selected!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ClientAndSponsorActivity.this, UserProfileActivity.class));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.v("Ygritteeee", dataSnapshot.toString());
                    userInformation = snapshot.getValue(UserInformation.class);
                    if ("Client".equalsIgnoreCase(userInformation.getType())) {
                        allUsers.add(userInformation);
                    }
                    Log.v("Ygritteeee", userInformation.toString());
                }
                cAdapter = new ClientAdapter(ClientAndSponsorActivity.this,allUsers);
                recyclerView.setAdapter(cAdapter);
                //recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        navigationView.setNavigationItemSelectedListener(this);

        //notification_badge=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_notification));
        mUsersDatabaseReference.addValueEventListener(valueEventListener);
        setNotificationCountDrawer();

    }

    private void setNotificationCountDrawer(){


//        notification_badge.setGravity(Gravity.CENTER_VERTICAL);
  //      notification_badge.setTypeface(null, Typeface.BOLD);
    //    notification_badge.setTextColor(getResources().getColor(R.color.colorAccent));
        //count is added
      //  notification_badge.setText("7");
    }



    @Override
    protected void onStart() {
        super.onStart();
//        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //firebaseAuth.removeAuthStateListener(authStateListener);
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

        }
        else if(id == R.id.nav_addItem){

            startActivity(new Intent(ClientAndSponsorActivity.this, sponsorInformation.class));

        }
        else if (id == R.id.nav_signout) {

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
