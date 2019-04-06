package com.minorproject.optimizers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Random;

public class PhotoActivity extends AppCompatActivity {
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        imageView=(ImageView) findViewById(R.id.imageView);
        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,345);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==345&&resultCode==RESULT_OK)
        {

            Bitmap photo=(Bitmap)data.getExtras().get("data");
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


            imageView.setImageBitmap(photo);
            final String photoName=new Random().nextInt()+"";
            final StorageReference photoRef=FirebaseStorage.getInstance().getReference().child("Pictures").child(photoName);

            photoRef.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (task.isSuccessful()){
                        return  photoRef.getDownloadUrl();
                    }
                    else
                        throw task.getException();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    DatabaseReference photoDBref=FirebaseDatabase.getInstance().getReference().child("Pictures").child("photo");
                    photoDBref.setValue(task.getResult().toString());
                }
            });
        }
    }
}
