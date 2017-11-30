package com.katekani.laptopsponsorapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class UserProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    UserInformation userInfo;
    String userProfileId;
    TextView fullname, contacts, email, address, site_name, adress_link, current_computer, developer_bio, new_device,qualification, skills;
    Button submitConfirmation;
    ValueEventListener valueEventListener,spinnerValueEventListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef,mDatabase;

    FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String userID;
    List<String> deviceNames;
    List<String> deviceIds;
    List<Devices> deviceList;
    Devices devices;
    ArrayAdapter<String> adapter;
    Spinner spinner;
    Context context;
    boolean donated;
   // private DatabaseReference mUsersDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = getBaseContext();
        Intent intent = getIntent();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userInfo = intent.getParcelableExtra("UserProfile");
        userProfileId = intent.getStringExtra("userProfileId");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRef = mFirebaseDatabase.getReference("Developer_answers").child(userProfileId);
        fullname = findViewById(R.id.fullnames);
        contacts = findViewById(R.id.contacts);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        spinner= findViewById(R.id.devicelist);

        submitConfirmation = findViewById(R.id.submitConfirmation);
        site_name = findViewById(R.id.user_answer1);
        adress_link = findViewById(R.id.user_answer2);
        current_computer = findViewById(R.id.user_answer3);
        developer_bio = findViewById(R.id.user_answer4);
        new_device = findViewById(R.id.user_answer5);
        qualification = findViewById(R.id.user_answer6);
        skills = findViewById(R.id.user_answer7);
//        donated = devices.isDonated();
        // fullnames of the client
        fullname.setText(userInfo.getUserName() + " " + userInfo.getUserSurname());
        contacts.setText(userInfo.getAddress());
        email.setText(userInfo.getGender());
        //===================================================================================

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              DeveloperAnswers developerAnswers = dataSnapshot.getValue(DeveloperAnswers.class);
                site_name.setText(developerAnswers.getSite_name());
                adress_link.setText(developerAnswers.getAdress_link());
                current_computer.setText(developerAnswers.getCurrent_computer());
                developer_bio.setText(developerAnswers.getDeveloper_bio());
                new_device.setText(developerAnswers.getNew_device());
                qualification.setText(developerAnswers.getQualification());
                skills.setText(developerAnswers.getSkills());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef.addValueEventListener(valueEventListener);
        //============================================================================


        //answers of the client
        /*
        site_name.setText(developerAnswers.getSite_name());
        adress_link.setText(developerAnswers.getAdress_link());
        current_computer.setText(developerAnswers.getCurrent_computer());
        developer_bio.setText(developerAnswers.getDeveloper_bio());
        new_device.setText(developerAnswers.getNew_device());
        qualification.setText(developerAnswers.getQualification());
        skills.setText(developerAnswers.getSkills());
        */



        submitConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int select_device = spinner.getSelectedItemPosition();
                String selected_device_id = deviceIds.get(select_device);
                Log.i("Ygritte", selected_device_id);

                mDatabase.child("Devices").child(userID).child(selected_device_id).child("donated").setValue(true);
//                //=-=======================================
//                donated = true;
//                if (donated)
//                {
//                    //Query myClientsQuery = databaseReference.child("Users").orderByChild("type").equalTo("Client");
//                }
//                //=========================================
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, "Confirmation regarding laptop request" );
                intent.putExtra(Intent.EXTRA_TEXT, "Congratulation "  + ",After a long selection process,\n" +
                        " of all the candidates that asked for sponsorship, it is with great pleasure to support and make your dream look great\n" +
                        ". I saw you fit to recieve the laptop that you requested.\n" +
                        "further information will communicate\n" +
                                "\n"+
                        "please lets keep in touch"+
                        "\n" +
                                "Regard\n"+
                        "");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });


        if (user != null) {

            userID = user.getUid();
            mRef = FirebaseDatabase.getInstance().getReference("Devices").child(userID);
            Query myClientsQuery = mRef.orderByChild("donated").equalTo(false);
            myClientsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    deviceNames = new ArrayList<>();
                    deviceIds = new ArrayList<>();
                    deviceList = new ArrayList<>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        if (snapshot.getValue() != null ) {
                            devices = snapshot.getValue(Devices.class);
                            deviceNames.add(devices.getDevice_name());
                            deviceIds.add(snapshot.getKey());
                            deviceList.add(devices);
                        }
                        Log.v("hdghjgkh", snapshot.toString());

                    }

                    adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, deviceNames);
                    //adapter = new ArrayAdapter<String>(context, deviceNames ,  android.R.layout.simple_spinner_item);
                    // Drop down layout style - list view with radio button
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinner.setAdapter(adapter);
                    //spinner.notifyAll();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String item = parent.getItemAtPosition(position).toString();

            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
