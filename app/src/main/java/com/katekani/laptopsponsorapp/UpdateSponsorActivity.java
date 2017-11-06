package com.katekani.laptopsponsorapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateSponsorActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editCompanyName,editEmail,editPassword,editContacts,editAddress,editQuantity;
    private Button btnRegister;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private Firebase mRootRef,mRootUserRef;
    private String userID;
    private DatabaseReference mRef;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sponsor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new ProgressDialog(this);

        editCompanyName = findViewById(R.id.editCompanyname);
        editEmail = findViewById(R.id.editemail);
        editPassword=findViewById(R.id.editPasswordSponsor);
        editContacts = findViewById(R.id.editContact);
        editAddress = findViewById(R.id.editAddress);
        editQuantity = findViewById(R.id.editQuantity);
        btnRegister = findViewById(R.id.buttonsignup);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference();

        mRootRef = new Firebase("https://laptopsponsorapplication.firebaseio.com/Sponsor");


        btnRegister.setOnClickListener(this);


    }



    @Override
    public void onClick(View view) {


        // Stores email and password entered
         String useremail = editEmail.getText().toString();
        final String userpassword = editPassword.getText().toString();


        if (TextUtils.isEmpty(useremail)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userpassword)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userpassword.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering user please wait....");
        progressDialog.show();

        //create user
        firebaseAuth.createUserWithEmailAndPassword(useremail, userpassword)
                .addOnCompleteListener(UpdateSponsorActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(UpdateSponsorActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        // progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (!task.isSuccessful()) {
                            Toast.makeText(UpdateSponsorActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String companyname = editCompanyName.getText().toString();
                            String address = editAddress.getText().toString();
                            String contacts = editContacts.getText().toString();
                            int quantity = Integer.parseInt(editQuantity.getText().toString());
                            String email = editEmail.getText().toString();
                            String type  = "Sponsor";

                            if (!companyname.equals("") & !address.equals("")) {

                                UserInformation uinfo = new UserInformation(companyname,email,contacts, address, quantity,type);

                                mRootRef.child(task.getResult().getUser().getUid()).setValue(uinfo);

                                mRootUserRef.child(task.getResult().getUser().getUid()).setValue(type);

                                //mRootRef.child("User_type").child(userID).setValue("Sponsor");

                                startActivity(new Intent(UpdateSponsorActivity.this, SponsorActivity.class));
                                finish();
                                progressDialog.dismiss();
                            }
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