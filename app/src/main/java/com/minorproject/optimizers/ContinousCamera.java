package com.minorproject.optimizers;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ContinousCamera extends AppCompatActivity {

Button buttonStart,buttonStop;
View view;
static Boolean stopped=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continous_camera);
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        buttonStart=(Button)findViewById(R.id.buttonStart);
        buttonStop=(Button)findViewById(R.id.buttonStop);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ContinousCamera.this,ContinousCamera.MyService.class);
                startService(intent);
            }

        });
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ContinousCamera.this,ContinousCamera.MyService.class);
                stopService(intent);
            }
        });
    }
    public class MyService extends IntentService {

        public MyService(String name) {
            super(name);
        }

        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
            createPushNotification();
            takeScreenshot();
            }

        }

        private void createPushNotification() {
            Notification.Builder builder=new Notification.Builder(getApplicationContext());
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("Continuous Camera Running");
            builder.setContentText("Your Service is running in the background..");

            NotificationChannel channel=new NotificationChannel("MyChannelId","MyChannelName",NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.BLUE);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            Intent intent1=new Intent(this,MainActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(this,3444,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(1001,builder.build());
        }

        void takeScreenshot(){
            view.setDrawingCacheEnabled(true);
            Bitmap photo=Bitmap.createBitmap(view.getDrawingCache());
            File path=Environment.getExternalStorageDirectory();
            File dir=new File(path+"/save/");
            dir.mkdir();
            File file=new File(dir,"picture.jpg");
            OutputStream outputStream=null;
            try{
                outputStream=new FileOutputStream(file);
                photo.compress(Bitmap.CompressFormat.JPEG,50,outputStream);
                outputStream.flush();
                outputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            Uri uri=Uri.fromFile(file);
            final StorageReference storageReferencePicture=FirebaseStorage.getInstance().getReference().child("Screen Images").child("image");
            storageReferencePicture.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return storageReferencePicture.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    FirebaseDatabase.getInstance().getReference().child("Screen Images").child("image").setValue(task.getResult().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            });
        }
    }


