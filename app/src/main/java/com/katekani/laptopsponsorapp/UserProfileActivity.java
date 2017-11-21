package com.katekani.laptopsponsorapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    UserInformation userInfo;
    String userProfileId;
    TextView fullname, contacts, email, address, site_name, adress_link, current_computer, developer_bio, new_device,qualification, skills;
    Button submitConfirmation;
    ValueEventListener valueEventListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
   // private DatabaseReference mUsersDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        userInfo = intent.getParcelableExtra("UserProfile");
        userProfileId = intent.getStringExtra("userProfileId");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Developer_answers").child(userProfileId);
        fullname = findViewById(R.id.fullnames);
        contacts = findViewById(R.id.contacts);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);


        submitConfirmation = findViewById(R.id.submitConfirmation);
        site_name = findViewById(R.id.user_answer1);
        adress_link = findViewById(R.id.user_answer2);
        current_computer = findViewById(R.id.user_answer3);
        developer_bio = findViewById(R.id.user_answer4);
        new_device = findViewById(R.id.user_answer5);
        qualification = findViewById(R.id.user_answer6);
        skills = findViewById(R.id.user_answer7);

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

}
