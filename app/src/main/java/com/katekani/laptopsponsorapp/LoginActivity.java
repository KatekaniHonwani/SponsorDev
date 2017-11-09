package com.katekani.laptopsponsorapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.google.firebase.auth.FirebaseAuth.getInstance;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //Widgets
    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnLogin;
    private TextView txtSignUp;
    private TextView txtForgotPassword;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private UserInformation userInformation;
    private DatabaseReference mRef;
    private FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser user;
    private String userID;
    private ProgressDialog progressDialog;

    private ChildEventListener mChildEventListener;
    //google buttons
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtEmail = findViewById(R.id.enter_email);
        edtPassword = findViewById(R.id.enter_password);
        btnLogin = findViewById(R.id.buttonlogin);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtSignUp = findViewById(R.id.txtSignUp);
        progressDialog = new ProgressDialog(this);


        firebaseAuth = getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
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
                                //for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Log.i("Ygritte", dataSnapshot.toString());

                                if ("Client".equalsIgnoreCase(userInformation.getType()))
                                {
                                    //Log.i("Ygritte", userInformation.getType());
                                    if(dataSnapshot.hasChild("answer1") && dataSnapshot.hasChild("answer2") && dataSnapshot.hasChild("answer3") && dataSnapshot.hasChild("answer4") && dataSnapshot.hasChild("answer5"))
                                    {
                                        startActivity(new Intent(LoginActivity.this, ClientAndSponsorActivity.class));
                                    }else {
                                    startActivity(new Intent(LoginActivity.this, ClientActivity.class));
                                    }

                                }else if("Sponsor".equalsIgnoreCase(userInformation.getType())){
                                    startActivity(new Intent(LoginActivity.this, ClientAndSponsorActivity.class));
                                }


//                                }else  if("Sponsor".equalsIgnoreCase(userInformation.getType())) {
//                                    startActivity(new Intent(LoginActivity.this, ClientAndSponsorActivity.class));
//                                }
                                //}
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

        btnLogin.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);

    }




    public void LoginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Logging in....Please wait");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    startActivity(new Intent(LoginActivity.this, ClientAndSponsorActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Login unsuccesfully ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view == btnLogin) {
            LoginUser();
        } else if (view == txtSignUp) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        } else if (view == txtForgotPassword) {
            startActivity(new Intent(this, ForgetPasswordActivity.class));
        }

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
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}