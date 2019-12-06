package com.example.testingimagerecognition;

import androidx.annotation.NonNull;
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
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import kaaes.spotify.webapi.android.SpotifyApi;

public class MainActivity extends AppCompatActivity {

    public static final int GALLERY_REQUEST_CODE  = 1;
    public Bitmap bitmap;
    private String token;
    HashMap<String, Float> keywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        token = getIntent().getStringExtra("token");
        //startActivity(new Intent(this, SpotifyLoginActivity.class));
        //checking whether accessing the external storage stuff is allowed (dont know if this is necessary)
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }

        //creates an onClickListener which launches pickFromGallery when button is pressed
        Button selectImage = findViewById(R.id.selectImage);

        selectImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                pickFromGallery();
            }
        });



    }


    //starts an intent which allows user to select only specific image types
    // I think that's what android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI is specifying
    private void pickFromGallery(){
        try{
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        } catch(Exception exp){
            Log.i("Error", exp.toString());
        }
    }

    //this runs when a result is received from the user selecting an image
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
                e.printStackTrace();
            }

            detectLabelsInImage();
            Intent intent = new Intent(this, PlaylistActivity.class);
            System.out.println("Keyword from main: " + keywords);
            intent.putExtra("keywords", keywords);
            intent.putExtra("token", token);
            startActivity(intent);

        }
    }

    public void detectLabelsInImage() {
        //maps the confidence of a keyword onto a string representing the keyword

        //CountDownLatch latch = new CountDownLatch(1);

        keywords = new HashMap<>();
        FirebaseVisionImage imageToCheck = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                .getOnDeviceImageLabeler();

        labeler.processImage(imageToCheck)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                        // Task completed successfully
                        // ...
                        TextView results = findViewById(R.id.textView);
                        String output = new String();
                        for (FirebaseVisionImageLabel label: labels) {
                            String text = label.getText();
                            String entityId = label.getEntityId();
                            float confidence = label.getConfidence();
                            keywords.put(text, confidence);
                            //System.out.println(label);
                            //System.out.println(text + entityId + confidence);
                            //System.out.println();
                            output += text + "\n";
                        }
                        results.setText(output);
                        //latch.countDown();
                        //System.out.println(labels);


                        /*
                        List<String> lst = new ArrayList<String>();
                        for (FirebaseVisionImageLabel x: labels) {
                            keywords.put(x.getText(), x.getConfidence());
                        }
                        System.out.println(keywords);

                         */
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                    }
                });

        //latch.await();
        //System.out.println("keywords1345544: " + keywords);
        //eturn keywords;
    }
}



































