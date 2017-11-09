package com.katekani.laptopsponsorapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //Variebles
    private EditText edtName;
    private EditText edtSurname;
    private EditText edtEmal;
    private EditText edtpassword;
    private Button buttonRegister;
    private String userID;
    private EditText editAddress,editContacts;
    private RadioButton male, female;
    private RadioButton client,sponsor;
    private  EditText edtQuantity;
    private EditText edtCompanyName;
    private EditText editReg;
    private  RadioGroup rgType;
    FirebaseUser user;

    //add firebase data stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private Firebase mRootRef;
    private DatabaseReference mRef;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String useremail, userpassword, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editAddress= findViewById(R.id.editAddress);
        editContacts = findViewById(R.id.edtContact);
        edtName = findViewById(R.id.editname);
        edtSurname = findViewById(R.id.editsurname);
        edtEmal = findViewById(R.id.editemail);
        edtpassword = findViewById(R.id.editpassword);
        buttonRegister = findViewById(R.id.buttonsignup);
        male = findViewById(R.id.rdMale);
        female = findViewById(R.id.rdFemale);
        edtCompanyName = findViewById(R.id.editCompanyname);
        edtQuantity =  findViewById(R.id.editQuantity);
        editReg = findViewById(R.id.editRegno);
        progressDialog = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mRef = mFirebaseDatabase.getReference().child("Users");
         user = mAuth.getCurrentUser();

        mAuth = FirebaseAuth.getInstance();

        //dialog
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Do you want to sign up as? ");
        builder1.setTitle("Registration");
        builder1.setCancelable(true);

        builder1.setNegativeButton(
                "Client",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onStart();

                        edtName.setVisibility(View.VISIBLE);
                        edtSurname.setVisibility(View.VISIBLE);
                        female.setVisibility(View.VISIBLE);
                        male.setVisibility(View.VISIBLE);
                        edtCompanyName.setVisibility(View.INVISIBLE);
                        editReg.setVisibility(View.INVISIBLE);
                        edtQuantity.setVisibility(View.INVISIBLE);


                    }

                });

        builder1.setNeutralButton("Sponsor",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        edtCompanyName.setVisibility(View.VISIBLE);
                        edtQuantity.setVisibility(View.VISIBLE);
                        editReg.setVisibility(View.VISIBLE);
                        edtName.setVisibility(View.INVISIBLE);
                        edtSurname.setVisibility(View.INVISIBLE);
                        female.setVisibility(View.INVISIBLE);
                        male.setVisibility(View.INVISIBLE);


                        // startActivity(new Intent(RegisterActivity.this, UpdateClientProfileActivity.class));

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {

                    String name = edtName.getText().toString();
                    String surname = edtSurname.getText().toString();
                    String address = editAddress.getText().toString();
                    String contacts = editContacts.getText().toString();
                    String type  = "Client";

                    userID = user.getUid();
                    if (!name.equals("") & !surname.equals("")) {
                        UserInformation uinfo = new UserInformation(name, surname, user.getEmail() ,address,contacts,gender, type);
                        //Log.v("dkjvdsjk", uinfo.getUserName().toString());

                        mRef.child(userID).setValue(uinfo, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(RegisterActivity.this, "Data could not be saved " + databaseError.getMessage(),Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Data saved successfully.",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                    progressDialog.dismiss();
                                }


                            }
                        });

                    }
                }
            }
        };
        buttonRegister.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {

        // Stores email and password entered
        useremail = edtEmal.getText().toString().trim();
        userpassword = edtpassword.getText().toString().trim();

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

        if(male.isChecked())
        {
            gender = "Male";
        }else if(female.isChecked()){
            gender = "Female";
        }

        progressDialog.setMessage("Registering user please wait....");
        progressDialog.show();
        //create user
        mAuth.createUserWithEmailAndPassword(useremail, userpassword)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        // progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    String companyname= edtCompanyName.getText().toString();
                                    String address = editAddress.getText().toString();
                                    String contacts = editContacts.getText().toString();
                                    int quantity = edtQuantity.getInputType();
                                    String email = edtEmal.getText().toString();
                                    String regno = editReg.getText().toString();
                                    String type  = "Sponsor";

                                    if (!companyname.equals("") & !address.equals("")) {
                                        UserInformation uinfo = new UserInformation(companyname,email,contacts, address, quantity,type,regno);
                                        mRef.child(userID).setValue(uinfo, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                if (databaseError != null) {
                                                    Toast.makeText(RegisterActivity.this, "Data could not be saved " + databaseError.getMessage(),Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, "Data saved successfully.",Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                    progressDialog.dismiss();
                                                    finish();
                                                }
                                            }
                                        });


                                    }
                                }
                            }
                        });



    }
//    public void verifyEmail()
//    {
//        final boolean isToast = false;
//        final FirebaseUser user = mAuth.getCurrentUser();
//        user.sendEmailVerification()
//                .addOnCompleteListener(this, new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//
//                        if (task.isSuccessful()) {
//                            if(isToast) {
//                                Toast.makeText(RegisterActivity.this,
//                                        "Verification email sent to " + user.getEmail(),Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Log.e("Log", "sendEmailVerification", task.getException());
//                            Toast.makeText(RegisterActivity.this,
//                                    "Failed to send verification email.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//    }

    public void forgotPassword(String email){
        FirebaseAuth.getInstance().sendPasswordResetEmail("user@example.com")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "Email sent.");
                        }
                    }
                });
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

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }
}