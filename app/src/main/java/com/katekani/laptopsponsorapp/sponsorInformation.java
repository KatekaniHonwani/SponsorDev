package com.katekani.laptopsponsorapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class sponsorInformation extends AppCompatActivity {

    private EditText edtAnswer1, edtAnswer2, edtAnswer3, edtAnswer4;
    private ImageView images;
    private Button submit;
    private FirebaseUser firebaseUser;
    DatabaseReference mCurrentUserRef;
    private String userID;
    private StorageReference mStorageRef;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog progressDialog;
    private Uri downloadUri;
    private DatabaseReference databaseReference;
    //FirebaseUser user;
    UserInformation userInfo;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_information2);

        userInfo = new UserInformation();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Device Details");
        edtAnswer1 = findViewById(R.id.answer1);
        edtAnswer2 = findViewById(R.id.answer2);
        edtAnswer3 = findViewById(R.id.answer3);
        edtAnswer4 = findViewById(R.id.answer4);
        submit = findViewById(R.id.submit);
        images = findViewById(R.id.laptopImage);
        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Devices");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
        }

        mCurrentUserRef = firebaseDatabase.getInstance().getReference();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String answer1 = edtAnswer1.getText().toString();
                String answer2 = edtAnswer2.getText().toString();
                String answer3 = edtAnswer3.getText().toString();
                String answer4 = edtAnswer4.getText().toString();


                if (TextUtils.isEmpty(answer1)) {
                    Toast.makeText(getApplicationContext(), "provide answer for question 1", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(answer2)) {
                    Toast.makeText(getApplicationContext(), "provide answer for question 2", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(answer3)) {
                    Toast.makeText(getApplicationContext(), "provide answer for question 3", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(answer4)) {
                    Toast.makeText(getApplicationContext(), "provide answer for question 4", Toast.LENGTH_SHORT).show();
                    return;
                }

                upload(view);

                if (!"".equals(answer1) && !"".equals(answer2) && !"".equals(answer3) && !"".equals(answer4)) {
                    //mCurrentUserRef.child("Users").child(userID);
                    startActivity(new Intent(sponsorInformation.this, ClientAndSponsorActivity.class));
                    Toast.makeText(getApplicationContext(), "UUID : " + userID, Toast.LENGTH_SHORT).show();
                    userInfo = new UserInformation(answer1, answer2, answer3, answer4);
                    mCurrentUserRef.child("Devices").child(userID).push().setValue(userInfo);
                    progressDialog.dismiss();
                }

            }
        });
//        images.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    public void upload(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {


            progressDialog.setMessage("Uploading image...");
            progressDialog.show();
            Uri uri = data.getData();
            Log.d("picture", userID);

            StorageReference filepath = mStorageRef.child(userID).child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(sponsorInformation.this, "Uploading done", Toast.LENGTH_LONG).show();
                    downloadUri = taskSnapshot.getDownloadUrl();
                    //Picasso.with(UpdateClientProfileActivity.this).load(downloadUri).fit().centerCrop().into(imageView);

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(downloadUri).build();

                    if (firebaseUser != null) {
                        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    userInfo.setImage(String.valueOf(downloadUri));
                                    mCurrentUserRef.child("Devices").child(userID).child("image").setValue(userInfo.getImage());

                                    displayProfilePic(downloadUri);
                                    //databaseReference.child(userID).setValue(userInfo.getImage());
                                    //Log.d(TAG, "User profile updated.");
                                }
                            }
                        });
                    }
                }
            });
        }
//            catch(FileNotFoundException e){
//
//            }

    }

    private void displayProfilePic(Uri downloadUri) {
        if (downloadUri != null) {
            Picasso.with(sponsorInformation.this).load(downloadUri).fit().centerCrop().into(images);
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
}
