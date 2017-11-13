package com.katekani.laptopsponsorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtqoute1;
    private TextView txtTeam;
    private TextView txtphrase1;
    private ImageView imgView2,imgView3,imgView4,imgView5,imgView6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtqoute1 = findViewById(R.id.txt1);
        txtphrase1 = findViewById(R.id.textPhrase1);
    ;
        imgView2= findViewById(R.id.img2);
        imgView3= findViewById(R.id.img3);
        imgView4= findViewById(R.id.img4);
        imgView5= findViewById(R.id.img5);
        imgView6= findViewById(R.id.img6);

        Picasso.with(this).load(R.drawable.naledi).into(imgView2);
        Picasso.with(this).load(R.drawable.nokuthula).into(imgView3);
        Picasso.with(this).load(R.drawable.makhosi).into(imgView4);
        Picasso.with(this).load(R.drawable.katekani).into(imgView5);
        Picasso.with(this).load(R.drawable.sethu).into(imgView6);

        txtphrase1.setText (" The Brainex we seek to aspire and contribute positivily to the startup developers," +
                "by giving them an oppotunity in this paltform to request for resources like, laptop and PCs.\n" +
                "We have discovered that there are many companies who are willing to sponsor the startup developers.\n" +
                "We aim to reach out to multiple organisations and help donate laptops to the in need developers.");

    }

    @Override
    public void onClick(View view) {

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
