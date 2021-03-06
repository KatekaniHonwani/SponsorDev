package com.katekani.laptopsponsorapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientAndSponsorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    private TextView notification_badge;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private String userID;
    private DatabaseReference mUserLoggedRef;
    private static final String TAG = "ClientAndSponsor;";

    Context context;
    private ClientAdapter clientAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDevicesReference;
    private DatabaseReference mDeviceDatabaseReference;
    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    UserInformation userInformation;

    List<UserInformation> allUsers = new ArrayList<>();
    List<Devices> allDEvices = new ArrayList<>();
    List<String> allDeviceId = new ArrayList<>();
    List<String> allUsersId = new ArrayList<>();
    private RecyclerView recyclerView;
    private ClientAdapter cAdapter;
    private DevicesAdapter mAdapter;
    TextView tvNameAndSurname;
    TextView tvEmail;
    //private RoundedImageView roundedImageView;
    String user_type, uuid, duid;


    DrawerLayout drawer;
    NavigationView navigationView;
    Devices devices;
    Menu nav_Menu;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_and_sponsor);

        context = getBaseContext();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.recycler_view);
        navigationView = findViewById(R.id.nav_view);

        //nav_Menu.findItem(R.id.nav_myItem).setVisible(false);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mDeviceDatabaseReference = FirebaseDatabase.getInstance().getReference("Devices");

        //recyclerView.setAdapter(cAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);



        navigationView.setNavigationItemSelectedListener(this);


        //tvNameAndSurname = navigationView.getHeaderView(0).findViewById(R.id.tvNameAndSurname);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.Email);
        //roundedImageView = navigationView.getHeaderView(0).findViewById(R.id.imageView);
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if (user != null) {

            userID = user.getUid();
            tvEmail.setText(user.getEmail());
          //tvNameAndSurname.setText(user.getDisplayName());
             //roundedImageView.setImageURI(user.getPhotoUrl());

            databaseReference = FirebaseDatabase.getInstance().getReference();
            mUserLoggedRef = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("type");

            mUserLoggedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        user_type = dataSnapshot.getValue().toString();
                        if ("Sponsor".equalsIgnoreCase(user_type)) {

                            Query myClientsQuery = databaseReference.child("Users").orderByChild("type").equalTo("Client");

                            myClientsQuery.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    allUsers = new ArrayList<>();
                                    allUsersId = new ArrayList<>();
                                    allDeviceId = new ArrayList<>();

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                        userInformation = snapshot.getValue(UserInformation.class);
                                       // tvNameAndSurname.setText(userInformation.getCompanyName());
                                        Log.i("Ygritte", dataSnapshot.toString());
                                        //tvEmail.setText(userInformation.getEmail());

                                        if ("Client".equalsIgnoreCase(userInformation.getType())) {
                                            allUsers.add(userInformation);
                                            allUsersId.add(snapshot.getKey().toString());
                                        }
                                    }
                                    cAdapter = new ClientAdapter(ClientAndSponsorActivity.this,allUsers);
                                    recyclerView.setAdapter(cAdapter);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    UserInformation userInformation = allUsers.get(position);
                                    String uuid = allUsersId.get(position);
                                    Intent intent = new Intent(ClientAndSponsorActivity.this, UserProfileActivity.class);
                                    intent.putExtra("UserProfile", userInformation);
                                    intent.putExtra("userProfileId",uuid);
                                    startActivity(intent);
                                }

                                @Override
                                public void onLongClick(View view, int position) {

                                }
                            }));

                        } else if ("Client".equalsIgnoreCase(user_type)) {
                           // Toast.makeText(context, "I a client with user email " + user.getEmail() + " :  user type => " + user_type, Toast.LENGTH_LONG).show();
                            nav_Menu = navigationView.getMenu();
                            nav_Menu.findItem(R.id.nav_myItem).setVisible(false);

                            valueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    allUsersId = new ArrayList<>();
                                    allDeviceId = new ArrayList<>();
                                    allDEvices = new ArrayList<>();
                                    for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                                        allUsersId.add(snapshot1.getKey().toString());


                                        for(DataSnapshot dataSnapshot1 : snapshot1.getChildren())
                                        {

                                            devices = dataSnapshot1.getValue(Devices.class);

                                            if(devices.isDonated()) {
                                                allDeviceId.add(dataSnapshot1.getKey().toString());
                                                allDEvices.add(devices);
                                            }
                                        }

                                        mAdapter = new DevicesAdapter(ClientAndSponsorActivity.this,allDEvices);
                                        recyclerView.setAdapter(mAdapter);

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            mDeviceDatabaseReference.addValueEventListener(valueEventListener);

                            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    devices = allDEvices.get(position);
                                    uuid = allUsersId.get(position);
                                    duid = allDeviceId.get(position);
                                   //Log.v("yugjksd",uuid);
                                    //Log.v("yugjksdss",duid);
                                    Intent intent = new Intent(ClientAndSponsorActivity.this, DeviceFullProfileActivity.class);
                                    intent.putExtra("deviceInfo", devices);
                                    intent.putExtra("userProfileId",uuid);
                                    intent.putExtra("deviceProfileId",duid);
                                    startActivity(intent);
                                }

                                @Override
                                public void onLongClick(View view, int position) {

                                }
                            }));
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

       // mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Users");

    }

    @Override
    protected void onStart() {
        super.onStart();
        //mUsersDatabaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mUsersDatabaseReference.removeEventListener(valueEventListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        if (id == R.id.nav_update) {

            if ("Sponsor".equalsIgnoreCase(user_type)) {
                startActivity(new Intent(ClientAndSponsorActivity.this, UpdateSponsorActivity.class));
            } else if ("Client".equalsIgnoreCase(user_type)) {
             
                startActivity(new Intent(ClientAndSponsorActivity.this, UpdateClientProfileActivity.class));
            }

        } else if (id == R.id.nav_about_us) {
            startActivity(new Intent(ClientAndSponsorActivity.this, AboutUsActivity.class));
        } else if (id == R.id.nav_myItem) {
            startActivity(new Intent(ClientAndSponsorActivity.this, SponsorAddItemActivity.class));
        } else if (id == R.id.nav_signout) {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ClientAndSponsorActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
