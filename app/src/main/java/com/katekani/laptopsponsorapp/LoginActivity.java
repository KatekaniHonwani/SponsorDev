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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.google.firebase.auth.FirebaseAuth.getInstance;


public class LoginActivity extends AppCompatActivity implements  View.OnClickListener {

    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnLogin;
    private TextView txtSponsor;
    private TextView txtClient;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private UserInformation users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //if (user != null) {
            // User is signed in
          // startActivity(new Intent(LoginActivity.this, SponsorActivity.class));
        //}

        edtEmail = (EditText)findViewById(R.id.enter_email);

        edtPassword = (EditText)findViewById(R.id.enter_password);
        btnLogin = (Button)findViewById(R.id.buttonlogin);
        txtSponsor = (TextView)findViewById(R.id.txtSignUpSponsor);
        txtClient =(TextView)findViewById(R.id.txtSignUpClient);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = getInstance();

        //set oncliklistners
        btnLogin.setOnClickListener(this);
        txtSponsor.setOnClickListener(this);
        txtClient.setOnClickListener(this);

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
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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