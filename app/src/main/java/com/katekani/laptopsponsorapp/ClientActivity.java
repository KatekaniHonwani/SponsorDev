package com.katekani.laptopsponsorapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ClientActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtAnswer1;
    private EditText edtAnswer2;
    private EditText edtAnswer3;
    private EditText edtAnswer4;
    private EditText edtAnswer5;
    private Button btnSubmitForm;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private String userID;
    private Firebase mRootRef;
    private DatabaseReference mRef;
    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;


    String editAnswer1, editAnswer2,editAnswer3,editAnswer4,editAnswer5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toast.makeText(this,"client",Toast.LENGTH_SHORT).show();

        edtAnswer1 = findViewById(R.id.answer1);
        edtAnswer2 = findViewById(R.id.answer2);
        edtAnswer3 = findViewById(R.id.answer3);
        edtAnswer4 = findViewById(R.id.answer4);
        edtAnswer5 = findViewById(R.id.answer5);
        btnSubmitForm = findViewById(R.id.submitForm);

        progressDialog = new ProgressDialog(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if ( firebaseUser !=null) {
            userID = firebaseUser.getUid();
        }

        btnSubmitForm.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();

                return true;
        }

        if(R.id.update == item.getItemId())
        {
            startActivity(new Intent(ClientActivity.this,UpdateClientProfileActivity.class));
        }else if(R.id.aboutUs == item.getItemId())
        {
            startActivity(new Intent(ClientActivity.this,AboutUsActivity.class));

        }else if(R.id.signout == item.getItemId()){
            startActivity(new Intent(ClientActivity.this,LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View view) {
        editAnswer1 = edtAnswer1.getText().toString().trim();
        editAnswer2 = edtAnswer2.getText().toString().trim();
        editAnswer3 = edtAnswer3.getText().toString().trim();
        editAnswer4 = edtAnswer4.getText().toString().trim();
        editAnswer5 = edtAnswer5.getText().toString().trim();

        //validation
        if (TextUtils.isEmpty(editAnswer1)) {
            Toast.makeText(getApplicationContext(), "provide answer for question 1", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(editAnswer2)) {
            Toast.makeText(getApplicationContext(), "provide answer for question 2", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(editAnswer3)) {
            Toast.makeText(getApplicationContext(), "provide answer for question 3", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(editAnswer4)) {
            Toast.makeText(getApplicationContext(), "provide answer for question 4", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(editAnswer5)) {
            Toast.makeText(getApplicationContext(), "provide answer for question 5", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering user please wait....");
        progressDialog.show();

        if (!"".equals(editAnswer1) && !"".equals(editAnswer2) && !"".equals(editAnswer3) && !"".equals(editAnswer4) && !"".equals(editAnswer5)) {

            DatabaseReference mCurrentUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("profile");

            Toast.makeText(getApplicationContext(), "UUID : "+userID, Toast.LENGTH_SHORT).show();

            final Map<String, Object> updateUser = new HashMap<>();
            updateUser.put("Answer 1", editAnswer1);
            updateUser.put("Answer 2", editAnswer2);
            updateUser.put("Answer 3", editAnswer3);
            updateUser.put("Answer 4", editAnswer4);
            updateUser.put("Answer 5", editAnswer5);

            mCurrentUserRef.updateChildren(updateUser);

            //dialog
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Wait for the feedback else you can update your profile");
            builder1.setTitle("FEEDBACK");
            builder1.setCancelable(true);

            builder1.setNegativeButton(
                    "SIGN OUT",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(ClientActivity.this, LoginActivity.class));
                            dialog.cancel();
                        }

                    });

            builder1.setNeutralButton("UPDATE PROFILE",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(ClientActivity.this, UpdateClientProfileActivity.class));

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
}
