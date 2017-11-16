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

public class UserProfileActivity extends AppCompatActivity {

    UserInformation userInfo;
    DeveloperAnswers developerAnswers;
    TextView fullname, contacts, email, address, site_name, adress_link, current_computer, developer_bio, new_device,qualification, skills;
    Button submitConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        userInfo = intent.getParcelableExtra("UserProfile");

        Intent intent1 = getIntent();
        developerAnswers = intent1.getParcelableExtra("UserProfile");

        fullname = findViewById(R.id.fullnames);
        contacts = findViewById(R.id.contacts);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
       submitConfirmation= findViewById(R.id.submitConfirmation);

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

        /*

        tvAnswer1 = findViewById(R.id.user_answer1);
        tvAnswer2 = findViewById(R.id.user_answer2);
        tvAnswer3 = findViewById(R.id.user_answer3);
        tvAnswer4 = findViewById(R.id.user_answer4);
        tvAnswer5 = findViewById(R.id.user_answer5);
    // fullnames of the client
        //fullname.setText(userInfo.getUserName() + " " + userInfo.getUserSurname());
        contacts.setText(userInfo.getAddress());
        email.setText(userInfo.getGender());

        //answers of the client
        site_name.setText(developerAnswers.getSite_name());
        adress_link.setText(developerAnswers.getAdress_link());
        current_computer.setText(developerAnswers.getCurrent_computer());
        developer_bio.setText(developerAnswers.getDeveloper_bio());
        new_device.setText(developerAnswers.getNew_device());
        qualification.setText(developerAnswers.getQualification());
        skills.setText(developerAnswers.getSkills());

        //Log.i("Ygritte", tvAnswer5.toString());

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

        */


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
