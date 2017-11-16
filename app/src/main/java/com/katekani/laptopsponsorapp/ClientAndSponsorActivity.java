package com.katekani.laptopsponsorapp;

import android.util.Log;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;
import android.view.MenuItem;
import android.content.Intent;
import android.content.Context;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.support.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import static com.google.firebase.auth.FirebaseAuth.getInstance;

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
    TextView tvNameAndSurname;
    TextView tvEmail;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_and_sponsor);

        context = getBaseContext();
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.recycler_view);
        navigationView = findViewById(R.id.nav_view);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        firebaseAuth = getInstance();
        user = firebaseAuth.getCurrentUser();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //recyclerView.setAdapter(cAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                UserInformation userInformation = allUsers.get(position);
                Toast.makeText(context, userInformation.getUserSurname() + " is selected!", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(ClientAndSponsorActivity.this, UserProfileActivity.class));
                Intent intent = new Intent(ClientAndSponsorActivity.this, UserProfileActivity.class);
                intent.putExtra("UserProfile", userInformation);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot1) {

                firebaseAuth = FirebaseAuth.getInstance();
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
                                allUsers = new ArrayList<>();

                                if("Sponsor".equalsIgnoreCase(userInformation.getType())){

                                    tvNameAndSurname.setText(userInformation.getCompanyName());
                                    tvEmail.setText(userInformation.getEmail());

                                    for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                                        //Log.v("Ygritteeee", dataSnapshot.toString());
                                        userInformation = snapshot1.getValue(UserInformation.class);
                                        if ("Client".equalsIgnoreCase(userInformation.getType())) {
                                            allUsers.add(userInformation);
                                        }
                                    }
                                    cAdapter = new ClientAdapter(ClientAndSponsorActivity.this,allUsers);
                                }else if("Client".equalsIgnoreCase(userInformation.getType())){

                                    tvNameAndSurname.setText(userInformation.getUserName());
                                    tvEmail.setText(userInformation.getEmail());

                                    navigationView = findViewById(R.id.nav_view);
                                    Menu nav_Menu = navigationView.getMenu();
                                    nav_Menu.findItem(R.id.nav_addItem).setVisible(false);

                                    tvNameAndSurname.setText(userInformation.getUserName() + " " + userInformation.getUserSurname());
                                    tvEmail.setText(userInformation.getEmail());




                                }
                                recyclerView.setAdapter(cAdapter);
                            }
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

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    userID = user.getUid();
                    mRef = FirebaseDatabase.getInstance().getReference("Mobile").child(userID);
                    mRef.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {

                                userInformation = dataSnapshot.getValue(UserInformation.class);
                                assert userInformation != null;
                                //for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Log.i("Ygritte", dataSnapshot.toString());

                                if ("Client".equalsIgnoreCase(userInformation.getType()))
                                {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                        userInformation = snapshot.getValue(UserInformation.class);
                                        if ("Client".equalsIgnoreCase(userInformation.getType())) {
                                            allUsers.add(userInformation);
                                        }
                                    }
                                    cAdapter = new ClientAdapter(ClientAndSponsorActivity.this,allUsers);
                                }
                                recyclerView.setAdapter(cAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value

                        }
                    });
                }
            }
        };
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navReaderView = navigationView.getHeaderView(0);
        tvNameAndSurname = (TextView)navReaderView.findViewById(R.id.tvNameAndSurname) ;
        tvEmail = navReaderView.findViewById(R.id.Email);


        mUsersDatabaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUsersDatabaseReference.addValueEventListener(valueEventListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mUsersDatabaseReference.removeEventListener(valueEventListener);

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
                            Log.i("Ygritte", dataSnapshot.toString());

                            if ("Sponsor".equalsIgnoreCase(userInformation.getType())) {

                                startActivity(new Intent(ClientAndSponsorActivity.this, UpdateSponsorActivity.class));
                            }
                            if ("Client".equalsIgnoreCase(userInformation.getType())) {


                                startActivity(new Intent(ClientAndSponsorActivity.this, UpdateClientProfileActivity.class));

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });
                }
        } else if (id == R.id.nav_about_us) {
            startActivity(new Intent(ClientAndSponsorActivity.this, AboutUsActivity.class));
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
