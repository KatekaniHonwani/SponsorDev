package com.katekani.laptopsponsorapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static com.google.firebase.auth.FirebaseAuth.getInstance;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,  View.OnClickListener {

    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnLogin;
    private TextView txtSponsor;
    private TextView txtClient;
    private TextView txtForgotPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private UserInformation users;

    //google buttons
    private SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity;";
    private static final int RC_SIGN_IN = 9001;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = getInstance(); authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null)
                {
                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this,"register ore login",Toast.LENGTH_SHORT).show();
                }
            }
        };
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this).
                enableAutoManage(this/*FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this,"wrong",Toast.LENGTH_SHORT).show();
                    }
                }).
                addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //if (user != null) {
        // User is signed in
        // startActivity(new Intent(LoginActivity.this, SponsorActivity.class));
        //}
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        edtEmail = (EditText)findViewById(R.id.enter_email);
        edtPassword = (EditText)findViewById(R.id.enter_password);

        btnLogin = (Button)findViewById(R.id.buttonlogin);

        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtSponsor = (TextView)findViewById(R.id.txtSignUpSponsor);
        txtClient =(TextView)findViewById(R.id.txtSignUpClient);
        progressDialog = new ProgressDialog(this);


        //set oncliklistners
        btnLogin.setOnClickListener(this);
        txtSponsor.setOnClickListener(this);
        txtClient.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);



    }

    public void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String value = edtEmail.getText().toString();
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthGoolge(account);

            }else
            {
                Toast.makeText(this,"auth went wrong",Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            intent.putExtra("client_email", value);
            startActivity(intent);
            Toast.makeText(LoginActivity.this,"client logged succefully",Toast.LENGTH_LONG).show();


        }
    }
    private void firebaseAuthGoolge(GoogleSignInAccount account) {
        AuthCredential crdentials = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(crdentials).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    //updateUI(user);

                }else {
                    Toast.makeText(LoginActivity.this,"authentication failed",Toast.LENGTH_LONG).show();
                    //updateUI(null);
                }
            }
        });
    }


    public void LoginUser()
    {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Enter email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Logging in....");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        }).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this,"Login successfully",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    startActivity(new Intent(LoginActivity.this, SponsorActivity.class));
                } else{
                    Toast.makeText(LoginActivity.this,"Login unsuccesfully ",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }



    @Override
    public void onClick(View view) {



        if(view == btnLogin)
        {
            LoginUser();
        }
        else if(view == txtClient)
        {
            Intent intent = new Intent(this,RegisterActivity.class);
            startActivity(intent);
        }else if(view == txtSponsor)
        {
            startActivity(new Intent(this, UpdateSponsorActivity.class));
        }
        else if(view == txtForgotPassword)
        {
            startActivity(new Intent(this,ForgetPasswordActivity.class));
        }
        else  if(R.id.sign_in_button ==view.getId() ) {
            signIn();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"onConnectionFailed:" + connectionResult);
    }
}