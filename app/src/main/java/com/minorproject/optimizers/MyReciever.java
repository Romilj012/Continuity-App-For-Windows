package com.minorproject.optimizers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class MyReciever extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        FirebaseDatabase.getInstance().getReference().child("Notifications").child("calls").setValue(new Random().nextInt()+"").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
}
