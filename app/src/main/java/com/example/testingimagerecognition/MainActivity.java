package com.example.testingimagerecognition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final int GALLERY_REQUEST_CODE  = 1;
    public Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //checking whether accessing the external storage stuff is allowed
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }

        Button selectImage = findViewById(R.id.selectImage);

        selectImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                pickFromGallery();
            }
        });


        //FirebaseVisionImage imageToCheck = FirebaseVisionImage.fromBitmap(bitmap);
    }


    private void pickFromGallery(){
        try{
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        }catch(Exception exp){
            Log.i("Error",exp.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ImageView toDisplay = (ImageView) findViewById(R.id.imageView);
                toDisplay.setImageBitmap(bitmap);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //image_view.setImageBitmap(bitmap);

            //to know about the selected image width and height
            //Toast.makeText(MainActivity.this, image_view.getDrawable().getIntrinsicWidth()+" & "+image_view.getDrawable().getIntrinsicHeight(), Toast.LENGTH_SHORT).show();

        }
    }

    public void detectLabelsInImage() {

    }
}
