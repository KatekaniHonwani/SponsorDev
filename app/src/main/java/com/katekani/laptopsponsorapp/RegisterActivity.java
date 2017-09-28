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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "addToDatabase";

    //Variebles
    private EditText edtName;
    private EditText edtSurname;
    private EditText edtEmal;
    private EditText edtpassword;
    private EditText edtconfirmpassword;
    private Button buttonRegister;
    private String userID;
    private EditText editAddress,editContacts;
    private RadioButton male, female;

    //add firebase data stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private Firebase mRootRef;
    private DatabaseReference mRef;
    private ProgressDialog progressDialog;
    private Firebase mRootUserRef;

    private String gender;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String useremail;
    String userpassword;
    String confirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editAddress= findViewById(R.id.editAddress);
        editContacts = findViewById(R.id.edtCellNo);
        edtName = findViewById(R.id.editname);
        edtSurname = findViewById(R.id.editsurname);
        edtEmal = findViewById(R.id.editemail);
        edtpassword = findViewById(R.id.editpassword);
        edtconfirmpassword = findViewById(R.id.editconfirm);
        buttonRegister = findViewById(R.id.buttonsignup);
        male = findViewById(R.id.rdMale);
        female = findViewById(R.id.rdFemale);

        progressDialog = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        mRootRef = new Firebase("https://laptopsponsorapplication.firebaseio.com/Users");
        mRootUserRef = new Firebase("https://laptopsponsorapplication.firebaseio.com/user_type");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    String name = edtName.getText().toString();
                    String surname = edtSurname.getText().toString();
                    String address = editAddress.getText().toString();
                    String contacts = editContacts.getText().toString();
                    String user_role = "Client";

                    userID = user.getUid();
                    if (!name.equals("") & !surname.equals("")) {
                        UserInformation uinfo = new UserInformation(name, surname, user.getEmail() ,address,contacts,gender, user_role);
                        mRootRef.child(userID).child("profile").setValue(uinfo);
                        mRootUserRef.child(userID).setValue(user_role);
                        startActivity(new Intent(RegisterActivity.this,ClientActivity.class));
                        progressDialog.dismiss();

                    }

                }
            }
        };

        buttonRegister.setOnClickListener(this);


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


    @Override
    public void onClick(View view) {


        // Stores email and password entered
        useremail = edtEmal.getText().toString().trim();
        userpassword = edtpassword.getText().toString().trim();
        confirmpassword = edtconfirmpassword.getText().toString().trim();

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

        if (userpassword.equals(confirmpassword)) {

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

                            }
                        }
                    });
        } else {
                Toast.makeText(RegisterActivity.this, "password do not match failed.",
                        Toast.LENGTH_SHORT).show();

        }
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
}