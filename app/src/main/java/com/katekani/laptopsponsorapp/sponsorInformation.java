package com.katekani.laptopsponsorapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private EditText edtAnswer1,edtAnswer2,edtAnswer5,edtAnswer4,edtAnswer6;
    private ImageView images;
    private Button submit;
    private FirebaseUser firebaseUser;
    DatabaseReference mCurrentUserRef;
    private String userID;
    private DatabaseReference databaseReference;
    private StorageReference mStorageRef ;
    private static final int GALLERY_INTENT=2;
    private ProgressDialog progressDialog;
    private Uri downloadUri;
   // String userId;
   //FirebaseUser user;
    UserInformation userInfo;
    private final String TAG = sponsorInformation.class.getName();
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_information2);

        mStorageRef= FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Device Details");
        edtAnswer1=findViewById(R.id.answer1);
        edtAnswer2=findViewById(R.id.answer2);
        edtAnswer4=findViewById(R.id.answer4);
        edtAnswer5 = findViewById(R.id.answer5);
        edtAnswer6 = findViewById(R.id.answer6);
        submit=findViewById(R.id.submit);
        images=findViewById(R.id.laptopImage);
        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Devices");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
        }
//
//        if (firebaseUser.getPhotoUrl() != null) {
//            Log.i(TAG, firebaseUser.getPhotoUrl().toString());
//            displayProfilePic(firebaseUser.getPhotoUrl());
//        }
        mCurrentUserRef = firebaseDatabase.getInstance().getReference();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Users");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String device_name = edtAnswer1.getText().toString();
                String device_model = edtAnswer2.getText().toString();
                String scree_size= edtAnswer4.getText().toString();
                String storage = edtAnswer5.getText().toString();
                String status = edtAnswer6.getText().toString();




                if (TextUtils.isEmpty(device_name)) {
                    Toast.makeText(getApplicationContext(), "provide answer for question 1", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(device_model)) {
                    Toast.makeText(getApplicationContext(), "provide answer for question 2", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(scree_size)) {
                    Toast.makeText(getApplicationContext(), "provide answer for question 3", Toast.LENGTH_SHORT).show();
                    return;
                }else  if(TextUtils.isEmpty(storage)) {

                    Toast.makeText(getApplicationContext(), "provide answer for question 4", Toast.LENGTH_SHORT).show();

                }else if(TextUtils.isEmpty(status)){
                    Toast.makeText(getApplicationContext(), "provide answer for question 5", Toast.LENGTH_SHORT).show();

                }


                if (!"".equals(device_name) && !"".equals(device_model) && !"".equals(scree_size)&& !"".equals(storage)&& !"".equals(status)  ) {
                    //mCurrentUserRef.child("Users").child(userID);
                    startActivity(new Intent(sponsorInformation.this, ClientAndSponsorActivity.class));
                    Toast.makeText(getApplicationContext(), "UUID : "+userID, Toast.LENGTH_SHORT).show();
                    Devices devices = new Devices(device_name,device_model,scree_size,storage,status);
                    mCurrentUserRef.child("Devices").child(userID).push().setValue(devices);
                    progressDialog.dismiss();
                }
                    Intent intent = new Intent(sponsorInformation.this,SponsorAddItemActivity.class);
                startActivity(intent);
            }
        });
        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*" );
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {

        if(requestCode==GALLERY_INTENT&& resultCode==RESULT_OK) {



            progressDialog.setMessage("Uploading image...");
            progressDialog.show();
            Uri uri=data.getData();
            StorageReference filepath=mStorageRef.child(userID).child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(sponsorInformation.this,"Uploading done",Toast.LENGTH_LONG).show();
                    downloadUri=taskSnapshot.getDownloadUrl();
                    //Picasso.with(UpdateClientProfileActivity.this).load(downloadUri).fit().centerCrop().into(imageView);

                    UserProfileChangeRequest profileUpdates=new UserProfileChangeRequest.Builder()
                            .setPhotoUri(downloadUri).build();
//
//                    if(user!=null) {
//                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    userInfo.setImage(String.valueOf(downloadUri));
//                                    displayProfilePic(Uri.parse(userInfo.getImage()));
//                                    Log.d(TAG, "User profile updated.");
//                                }
//                            }
//                        });
//                    }

                    if(firebaseUser!=null)
                    {
                        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    displayProfilePic(downloadUri);
                                    //databaseReference.child("image").setValue(userInfo.getImage());
                                    Log.d(TAG, "User profile updated.");
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

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
