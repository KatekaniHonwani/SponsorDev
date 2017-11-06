package com.katekani.laptopsponsorapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SponsorActivity extends AppCompatActivity {

    private ClientAdapter clientAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private ValueEventListener valueEventListener;
    Context context;

    UserInformation userInformation;
    List<UserInformation> allUsers = new ArrayList<>();
    private RecyclerView recyclerView;
    private ClientAdapter cAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = getApplicationContext();
        //Toast.makeText(this, "sponsor", Toast.LENGTH_SHORT).show();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                UserInformation userInformation = allUsers.get(position);
                Toast.makeText(getApplicationContext(), userInformation.getUserSurname() + " is selected!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SponsorActivity.this, UserProfileActivity.class));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userInformation = snapshot.getValue(UserInformation.class);
                    if ("Client".equalsIgnoreCase(userInformation.getType())) {
                        allUsers.add(userInformation);
                    }
                    //Log.i("Ygritte", userInformation.toString());
                    //Log.i("Ygritte", userInformation.getUserName());
                    allUsers.add(userInformation);
                }

                cAdapter = new ClientAdapter(allUsers);
                recyclerView.setAdapter(cAdapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mUsersDatabaseReference.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mUsersDatabaseReference.removeEventListener(valueEventListener);
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

