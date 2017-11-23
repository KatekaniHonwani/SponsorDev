package com.katekani.laptopsponsorapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewSwitcher;

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
    private  EditText edtRole;
    private EditText edtCompanyName;
    private EditText editReg;
    private  RadioGroup rgType;

    //add firebase data stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private Firebase mRootRef;
    private DatabaseReference mRef;
    private ProgressDialog progressDialog;
    private StorageReference mStorageRef ;
    private static final int GALLERY_INTENT=2;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String useremail, userpassword, gender;
    private Validation validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editAddress= findViewById(R.id.editAddress);
        editContacts = findViewById(R.id.edtContact);
        edtName = findViewById(R.id.edt_name);
        edtSurname = findViewById(R.id.editsurname);
        edtEmal = findViewById(R.id.editemail);
        edtpassword = findViewById(R.id.editpassword);
        buttonRegister = findViewById(R.id.buttonsignup);
        male = findViewById(R.id.rdMale);
        female = findViewById(R.id.rdFemale);
        edtCompanyName = findViewById(R.id.editCompanyname);
        edtRole =  findViewById(R.id.edtRole);
        editReg = findViewById(R.id.editRegno);
        progressDialog = new ProgressDialog(this);
        validation = new Validation();


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mRef = mFirebaseDatabase.getReference().child("Users");
        FirebaseUser user = mAuth.getCurrentUser();

        mAuth = FirebaseAuth.getInstance();


        //dialog
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Do you want to sign up as? ");
        builder1.setTitle("Registration");
        builder1.setCancelable(true);
        final ViewSwitcher viewSwitcher1;
        final ViewSwitcher viewSwitcher2;

        final LinearLayout myNameView;
        final LinearLayout myCompanyNameView;
        final LinearLayout myRegNoView;
        final LinearLayout mySurnameView;

        viewSwitcher1 =   findViewById(R.id.viewSwitcher1);
        viewSwitcher2 =   findViewById(R.id.viewSwitcher2);
        myNameView= findViewById(R.id.view_name);
        myCompanyNameView = findViewById(R.id.view_company_name);
        myRegNoView= findViewById(R.id.view_reg_no);
        mySurnameView = findViewById(R.id.view_surname);
        builder1.setNegativeButton(
                "developer",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onStart();




                        //Start

                        if (viewSwitcher1.getCurrentView() != myNameView && viewSwitcher2.getCurrentView()!=mySurnameView){
                            viewSwitcher1.showPrevious();
                            viewSwitcher2.showPrevious();
                        }

                    }

                });

        builder1.setNeutralButton("Sponsor",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                       female.setVisibility(View.INVISIBLE);
                       male.setVisibility(View.INVISIBLE);
                       edtRole.setVisibility(View.INVISIBLE);

                        // startActivity(new Intent(RegisterActivity.this, UpdateClientProfileActivity.class));
                        if (viewSwitcher1.getCurrentView() != myCompanyNameView && viewSwitcher2.getCurrentView()!=myRegNoView){

                            viewSwitcher1.showNext();
                            viewSwitcher2.showNext();
                        }
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    String name = edtName.getText().toString();
                    validation.isNameSurnameValid(name);

                    String surname = edtSurname.getText().toString();
                    validation.isNameSurnameValid(name);

                    String address = editAddress.getText().toString();
                    String contacts = editContacts.getText().toString();
                    validation.isPhoneValid(contacts);
                    validation.isPhoneValid2(contacts);
                    String role = edtRole.getText().toString();
                    String type  = "Client";

                    userID = user.getUid();
                    if (!name.equals("") & !surname.equals("")) {
                        UserInformation uinfo = new UserInformation(name, surname, user.getEmail() ,address,contacts,gender, role,type);
                        //Log.v("dkjvdsjk", uinfo.getUserName().toString());

                        mRef.child(userID).setValue(uinfo, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(RegisterActivity.this, "Data could not be saved " + databaseError.getMessage(),Toast.LENGTH_LONG).show();
                                } else {
                                    System.out.println("Data saved successfully.");
                                    Toast.makeText(RegisterActivity.this, "Data saved successfully.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        startActivity(new Intent(RegisterActivity.this,ClientActivity.class));
                        progressDialog.dismiss();
                    }
                }
            }
        };


        mStorageRef= FirebaseStorage.getInstance().getReference();



        buttonRegister.setOnClickListener(this);

    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        FirebaseUser user = mAuth.getCurrentUser();
        if(requestCode==GALLERY_INTENT&& resultCode==RESULT_OK) {

            progressDialog.setMessage("Uploading image...");
            progressDialog.show();
            Uri uri=data.getData();
            StorageReference filepath=mStorageRef.child("Photo").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this,"Uploading done",Toast.LENGTH_LONG).show();


                }
            });
        }
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
                                    int quantity = edtRole.getInputType();
                                    String email = edtEmal.getText().toString();
                                    String regno = editReg.getText().toString();
                                    String type  = "Sponsor";

                                    if (!companyname.equals("") & !address.equals("")) {

                                        UserInformation uinfo = new UserInformation(companyname,email,contacts, address,type,regno);
                                        mRef.child(userID).setValue(uinfo, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                if (databaseError != null) {
                                                    Toast.makeText(RegisterActivity.this, "Data could not be saved " + databaseError.getMessage(),Toast.LENGTH_LONG).show();
                                                } else {
                                                    System.out.println("Data saved successfully.");
                                                    Toast.makeText(RegisterActivity.this, "Data saved successfully.",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                        startActivity(new Intent(RegisterActivity.this, ClientAndSponsorActivity.class));
                                        finish();
                                        progressDialog.dismiss();
                                    }
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