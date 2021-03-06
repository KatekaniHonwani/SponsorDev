package com.katekani.laptopsponsorapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DeviceFullProfileActivity extends AppCompatActivity {

    Devices deviceLists;
    String uuid;
    String duid;
    TextView deviceNam, model, screenSize, storage,status;
    Button sendRequest;
    ImageView device_image;
    ValueEventListener valueEventListener,spinnerValueEventListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private DatabaseReference mRefImages;

    FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String userID;
    Devices devices;
    //List<Devices> allDEvices = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Context context;

    private static final String TAG = "DeviceFullProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_full_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        //deviceLists = intent.getParcelableExtra("deviceInfo");

        uuid = intent.getStringExtra("userProfileId");
        duid = intent.getStringExtra("deviceProfileId");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        sendRequest = findViewById(R.id.send_request);
        deviceNam = findViewById(R.id.textDeviceName);
        model = findViewById(R.id.deviceModel);
        screenSize = findViewById(R.id.dviceScreenSize);
        storage = findViewById(R.id.deviceStorage);
        status = findViewById(R.id.deviceStatus);
       device_image = findViewById(R.id.device_image_id);


        if (user != null) {

            mRef = mFirebaseDatabase.getReference("Devices").child(uuid).child(duid);
            mRefImages = mFirebaseDatabase.getReference("Device_Images").child(uuid).child(duid);
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    devices = dataSnapshot.getValue(Devices.class);
                    Picasso.with(context).load(Uri.parse(devices.getImage())).into(device_image);
                    deviceNam.setText("Device name : " + devices.getDevice_name());
                    model.setText("Device Model : " + devices.getDevice_model());
                    screenSize.setText("Device screen size :" + devices.getScreen_size());
                    storage.setText("Device HHD size : " + devices.getStorage());
                    status.setText("Device status :s" + devices.getStatus());


                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
       }


        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, "Confirmation regarding laptop request" );
                intent.putExtra(Intent.EXTRA_TEXT, "Congratulation "  + ",After a long selection process,\n" +
                        " of all the candidates that asked for sponsorship, it is with great pleasure to support and make your dream look great\n" +
                        ". I saw you fit to recieve the laptop that you requested.\n" +
                        "further information will communicate\n" +
                        "\n"+
                        "please lets keep in touch"+
                        "\n" +
                        "Regard\n"+
                        "");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }*/

                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic("news");
                // [END subscribe_topics]

                // Log and toast
                String msg = getString(R.string.msg_subscribed);
                Log.d(TAG, msg);
                Toast.makeText(DeviceFullProfileActivity.this, msg, Toast.LENGTH_SHORT).show();

            }
        });

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
