package com.katekani.laptopsponsorapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class UpdateClientProfileActivity extends AppCompatActivity {

    private final String TAG = UpdateClientProfileActivity.class.getName();
    UserInformation userInfo;
    EditText etdName,etdSurname,edtContacts, etdEmail, edtAddress, edtGender,edtImage;
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
        setContentView(R.layout.activity_update_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mStorageRef= FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        getSupportActionBar().setTitle("Personal Details");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                Intent intent = new Intent(UpdateClientProfileActivity.this, ClientAndSponsorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        etdName = findViewById(R.id.name);
        etdSurname = findViewById(R.id.surname);
        edtContacts = findViewById(R.id.contact);
        etdEmail = findViewById(R.id.edit_email);
        edtAddress = findViewById(R.id.edit_address);
        edtGender = findViewById(R.id.gender);
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
                    etdName.setText(userInfo.getUserName());
                    etdSurname.setText(userInfo.getUserSurname());
                    etdEmail.setText(userInfo.getEmail());
                    edtContacts.setText(userInfo.getContacts());
                    edtAddress.setText(userInfo.getAddress());
                    edtGender.setText(userInfo.getGender());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("Ygritte", "loadPost:onCancelled", databaseError.toException());
                    // ...

                }
            };
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*" );
                startActivityForResult(intent,GALLERY_INTENT);
                //Toast.makeText(UpdateClientProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
            }
        });
        Button mButtonUpdate =findViewById(R.id.btn_update);
        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etdName.getText().toString().isEmpty()&&!etdSurname.getText().toString().isEmpty()&&!etdEmail.getText().toString().isEmpty()&&!edtAddress.getText().toString().isEmpty()&&!edtContacts.getText().toString().isEmpty()&&!edtGender.getText().toString().isEmpty()) {
                    String name = etdName.getText().toString();
                    String surname = etdSurname.getText().toString();
                    String userEmail = etdEmail.getText().toString();
                    String userAddress = edtAddress.getText().toString();
                    String userContact = edtContacts.getText().toString();
                    String userGender = edtGender.getText().toString();
                    final String type= userInfo.getType();

                    writeNewPost(name,surname,userEmail,userAddress,userContact,userGender,type);
                    Toast.makeText(UpdateClientProfileActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UpdateClientProfileActivity.this,ClientAndSponsorActivity.class));
                } else {
                    Toast.makeText(UpdateClientProfileActivity.this, "Please fill the empty field", Toast.LENGTH_SHORT).show();
                }
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

    private void writeNewPost(String name, String surname, String email,String address,String contacts, String gender, String type) {
        //String key = mUserDatabaseReference.child("Users").push().getKey();
        Log.i("Ygritte", name);
        UserInformation userInformation = new UserInformation(name,surname,email,address,contacts,gender,type);
        Map<String, Object> userInforValues = userInformation.toMap();
        //Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put("/Users/" + uuid, userInforValues);
        mUserDatabaseReference.updateChildren(userInforValues);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {

        if(requestCode==GALLERY_INTENT&& resultCode==RESULT_OK) {

//            try{
//                progressDialog.setMessage("Uploading image...");
//                progressDialog.show();
//                Uri uri=data.getData();
////                final InputStream imageStream=getContentResolver().openInputStream(uri);
////                final Bitmap selectedImage= BitmapFactory.decodeStream(imageStream);
//               final UploadTask uploadTask =mStorageRef.child(userId).putFile(uri);
//
//                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        Uri downloadUri=taskSnapshot.getDownloadUrl();
////                        imageURL=downloadUri.toString();
//                        //value.setProfileImage(imageURL);
//                        //mRef.setValue(value);
//                        imageView.setImageURI(downloadUri);
//                        progressDialog.dismiss();
//                        Toast.makeText(UpdateClientProfileActivity.this,"Uploading done",Toast.LENGTH_LONG).show();
//
//                        displayProfilePic(downloadUri);
//                        UserProfileChangeRequest profileUpdates=new UserProfileChangeRequest.Builder().setPhotoUri(downloadUri).build();
//
//                        if(user!=null)
//                        {
//                            user.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    if (uploadTask.isSuccessful())
//                                    {
//                                        Log.d("Profile Updated","User profile updated");
//                                    }
//                                }
//                            });
//
//                        }
//                    }
//                });

                  progressDialog.setMessage("Uploading image...");
                progressDialog.show();
                Uri uri=data.getData();
                StorageReference filepath=mStorageRef.child(userId).child(uri.getLastPathSegment());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateClientProfileActivity.this,"Uploading done",Toast.LENGTH_LONG).show();
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
//            catch(FileNotFoundException e){
//
//            }

        }


    private void displayProfilePic(Uri downloadUri) {
        if (downloadUri != null) {
            Picasso.with(UpdateClientProfileActivity.this).load(downloadUri).fit().centerCrop().into(imageView);
        }
    }

}
