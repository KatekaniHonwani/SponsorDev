package com.katekani.laptopsponsorapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
   // private Button btnContinue;
    private static int SPLASH_TIME_OUT=3000;
    private ProgressBar progressBar;
    private int progressBarStatus=0;
    private  Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar=findViewById(R.id.progressBar);
        if (!isConnected(MainActivity.this))builder(MainActivity.this).show();
        else
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            },SPLASH_TIME_OUT);

            //setContentView(R.layout.activity_main);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (progressBarStatus<1000)
                    {
                        progressBarStatus++;
                        android.os.SystemClock.sleep(50);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(progressBarStatus);
                            }
                        });
                    }

                }
            }).start();
        }




    }

    public boolean isConnected(Context context)
    {
        ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=cm.getActiveNetworkInfo();

        if(netInfo!=null && netInfo.isConnectedOrConnecting())
        {
            android.net.NetworkInfo wifi=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((mobile!=null && mobile.isConnectedOrConnecting()) || (wifi!=null & wifi.isConnectedOrConnecting())) return true;

                else return false;
        }
        else
        {
            return false;
        }
    }

    public AlertDialog.Builder builder(Context c)
    {
        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(c);
        alertBuilder.setTitle("No internet connection");
        alertBuilder.setMessage("You have to be connected to the internet to use the app.Press Ok to Exit");

        alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                finish();
            }
        });
        return alertBuilder;

    }

}
