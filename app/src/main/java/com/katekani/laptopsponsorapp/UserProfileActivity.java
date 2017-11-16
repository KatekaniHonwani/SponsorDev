package com.katekani.laptopsponsorapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

    UserInformation userInfo;
    TextView fullname, contacts, email, address, tvAnswer1, tvAnswer2, tvAnswer3, tvAnswer4, tvAnswer5;
    Button submitConfirmation,sendComment,viewMoreClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();
        userInfo = intent.getParcelableExtra("user_profile");

        fullname = findViewById(R.id.fullnames);
        contacts = findViewById(R.id.contacts);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
       submitConfirmation= findViewById(R.id.submitConfirmation);


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
        tvAnswer1.setText(userInfo.getAnswer1());
        tvAnswer2.setText(userInfo.getAnswer2());
        tvAnswer3.setText(userInfo.getAnswer3());
        tvAnswer4.setText(userInfo.getAnswer4());
        tvAnswer5.setText(userInfo.getAnswer5());

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

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, "Recommendation ");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        viewMoreClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(UserProfileActivity.this,SponsorActivity.class));
            }
        });*/

    }
}
