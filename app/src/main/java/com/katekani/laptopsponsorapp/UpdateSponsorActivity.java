package com.katekani.laptopsponsorapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class UpdateSponsorActivity extends AppCompatActivity{
    private final String TAG = UpdateClientProfileActivity.class.getName();
    UserInformation userInfo;
    EditText etdCompanyName,edtCompanyTelephone, etdEmail, edtAddress, edtQuantity,edtImage;
    FirebaseUser user;
    String userId;
    private DatabaseReference mUserDatabaseReference;
    private StorageReference mStorageRef ;
    private static final int GALLERY_INTENT=2;
    private ImageView imageView;
    private ProgressDialog progressDialog;
    private String imageURL;
    ValueEventListener postListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sponsor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mStorageRef= FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etdCompanyName = findViewById(R.id.editCompanyname);
        edtCompanyTelephone = findViewById(R.id.edtCompanyTelephone);
        etdEmail = findViewById(R.id.edit_email);
        edtAddress = findViewById(R.id.edit_address);
        edtQuantity = findViewById(R.id.edtQuantuty);
        imageView = findViewById(R.id.header);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Log.i(TAG, user.getPhotoUrl().toString());
                displayProfilePic(user.getPhotoUrl());
            }
            userId = user.getUid();
            mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
            postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    userInfo = dataSnapshot.getValue(UserInformation.class);
                    // ...
                    etdCompanyName.setText(userInfo.getCompanyName());
                    edtCompanyTelephone.setText(userInfo.getContacts());
                    etdEmail.setText(userInfo.getEmail());
                    edtAddress.setText(userInfo.getAddress());
                    edtQuantity.setText(String.valueOf(userInfo.getQuantity()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("Ygritte", "loadPost:onCancelled", databaseError.toException());
                    // ...

                }
            };
        }

        Button mButtonUpdate =findViewById(R.id.btn_update);
        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etdCompanyName.getText().toString().isEmpty()&&!edtCompanyTelephone.getText().toString().isEmpty()&&!etdEmail.getText().toString().isEmpty()&&!edtAddress.getText().toString().isEmpty()&&!edtQuantity.getText().toString().isEmpty()) {
                    String companyName = etdCompanyName.getText().toString();
                    String companyTelephone = edtCompanyTelephone.getText().toString();
                    String companyEmail = etdEmail.getText().toString();
                    String companyAddress = edtAddress.getText().toString();
                    int quantity= Integer.parseInt(edtQuantity.getText().toString());
                    final String type= userInfo.getType();

                    writeNewPost(companyName,companyTelephone,companyEmail,companyAddress,quantity,type);
                    Toast.makeText(UpdateSponsorActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UpdateSponsorActivity.this,ClientAndSponsorActivity.class));
                } else {
                    Toast.makeText(UpdateSponsorActivity.this, "Please fill the empty field", Toast.LENGTH_SHORT).show();
                }
            }
        });

//Floating
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*" );
                startActivityForResult(intent,GALLERY_INTENT);
                //Toast.makeText(UpdateClientProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mUserDatabaseReference.addValueEventListener(postListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mUserDatabaseReference!=null) {
            mUserDatabaseReference.removeEventListener(postListener);
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
    private void writeNewPost(String companyName, String companyTelephone, String companyEmail,String companyAddress,int quantity, String type) {
        Log.i("Ygritte", companyName);
        UserInformation userInformation = new UserInformation(companyName,companyTelephone,companyEmail,companyAddress,quantity,type);
        Map<String, Object> userInforValues = userInformation.toMap();
        mUserDatabaseReference.updateChildren(userInforValues);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {

        if(requestCode==GALLERY_INTENT&& resultCode==RESULT_OK) {


            progressDialog.setMessage("Uploading image...");
            progressDialog.show();
            Uri uri=data.getData();
            StorageReference filepath=mStorageRef.child(userId).child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(UpdateSponsorActivity.this,"Uploading done",Toast.LENGTH_LONG).show();
                    final Uri downloadUri=taskSnapshot.getDownloadUrl();
                    //Picasso.with(UpdateClientProfileActivity.this).load(downloadUri).fit().centerCrop().into(imageView);

                    UserProfileChangeRequest profileUpdates=new UserProfileChangeRequest.Builder()
                            .setPhotoUri(downloadUri).build();

                    if(user!=null)
                    {
                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    displayProfilePic(downloadUri);
                                    Log.d(TAG, "User profile updated.");
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void displayProfilePic(Uri downloadUri) {
        if (downloadUri != null) {
            Picasso.with(UpdateSponsorActivity.this).load(downloadUri).fit().centerCrop().into(imageView);
        }
    }

}