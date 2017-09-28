package com.katekani.laptopsponsorapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SponsorActivity extends AppCompatActivity {

    private ListView listView;
    private ClientAdapter clientAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private ChildEventListener mChildEventListener;

    Context context;
    List<UserInformation> allUsers = new ArrayList<>();

    UserInformation userInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = getApplicationContext();
        Toast.makeText(this, "sponsor", Toast.LENGTH_SHORT).show();

        listView = findViewById(R.id.list_view);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        mChildEventListener = new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //Log.i("Ygritte", dataSnapshot.toString());
                String user_key = dataSnapshot.getKey();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userInformation = snapshot.getValue(UserInformation.class);
                    allUsers.add(userInformation);
                }
                clientAdapter = new ClientAdapter(context, R.layout.adapter_layout, allUsers);
                listView.setAdapter(clientAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //startActivity(new Intent(SponsorActivity.this,UserProfileActivity.class));
                        UserInformation userInfo = allUsers.get(i);
                        Intent intent = new Intent(SponsorActivity.this, UserProfileActivity.class);
                        // Log.i("Ygritte", userInfo.toString());
                        intent.putExtra("user_profile", userInfo);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                /*String user_key  = dataSnapshot.getKey();
                for ( DataSnapshot snapshot: dataSnapshot.getChildren() ) {

                    for (DataSnapshot profile : snapshot.getChildren()) {
                        if ("type".equals(profile.getKey()) && "Client".equals(profile.getValue())) {
                           userInformation = snapshot.getValue(UserInformation.class);
                            allUsers.add(userInformation);

                        }
                    }
                }
                clientAdapter = new ClientAdapter(context, R.layout.adapter_layout, allUsers);
                listView.setAdapter(clientAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //startActivity(new Intent(SponsorActivity.this,UserProfileActivity.class))
                         UserInformation userInfo =allUsers.get(i);;
                        Intent intent = new Intent(SponsorActivity.this,UserProfileActivity.class);

                        intent.putExtra("user_profile", userInfo);
                        startActivity(intent);
                    }
                });*/


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mUsersDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        if (R.id.update == item.getItemId()) {
            startActivity(new Intent(SponsorActivity.this, UpdateSponsorActivity.class));
        } else if (R.id.aboutUs == item.getItemId()) {
            startActivity(new Intent(SponsorActivity.this, AboutUsActivity.class));

        } else if (R.id.signout == item.getItemId()) {
            startActivity(new Intent(SponsorActivity.this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
