package com.katekani.laptopsponsorapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class SponsorAddItemActivity extends AppCompatActivity {

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
    private DatabaseReference mDevicesReference;
    private DatabaseReference mUsersDatabaseReference;
    private ValueEventListener valueEventListener;
    UserInformation userInformation;
    List<UserInformation> allUsers = new ArrayList<>();
    List<Devices> allDEvices = new ArrayList<>();
    private RecyclerView recyclerView;
    private ClientAdapter cAdapter;
    private DevicesAdapter mAdapter;
    TextView tvNameAndSurname;
    TextView tvEmail;
    private RecyclerView recyclerViewDevice;

    NavigationView navigationView;
    Devices devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_add_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = findViewById(R.id.fab);

        recyclerViewDevice = findViewById(R.id.recycler_view_device);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Devices");

        firebaseAuth = getInstance();
        user = firebaseAuth.getCurrentUser();



        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot1) {


                recyclerViewDevice.setLayoutManager(new LinearLayoutManager(context));
                recyclerViewDevice.setItemAnimator(new DefaultItemAnimator());
                recyclerViewDevice.addItemDecoration(new DividerItemDecoration(SponsorAddItemActivity.this, LinearLayoutManager.VERTICAL));
                recyclerViewDevice.setHasFixedSize(true);

                firebaseAuth = FirebaseAuth.getInstance();
                user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    userID = user.getUid();
                    mRef = FirebaseDatabase.getInstance().getReference("Devices").child(userID);
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            allDEvices.clear();
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                devices = snapshot.getValue(Devices.class);
                                allDEvices.add(devices);
                                Log.v("hdghjgkh", devices.toString());
                            }
                            mAdapter = new DevicesAdapter(SponsorAddItemActivity.this, allDEvices);
                            recyclerViewDevice.setAdapter(mAdapter);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SponsorAddItemActivity.this,sponsorInformation.class);
                startActivity(intent);
            }
        });

        mUsersDatabaseReference.addValueEventListener(valueEventListener);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
