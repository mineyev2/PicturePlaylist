package com.example.testingimagerecognition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.DialogCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public static final int GALLERY_REQUEST_CODE  = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    public Bitmap bitmap;
    private String token;
    private String playlistName;

    HashMap<String, Float> keywords;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button confirm = findViewById(R.id.confirm);
        confirm.setVisibility(View.INVISIBLE);
        Drawable d = ((ImageView) findViewById(R.id.imageView)).getDrawable();
        bitmap = ((BitmapDrawable) d).getBitmap();

        token = getIntent().getStringExtra("token");
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


        Button camera = findViewById(R.id.takePicture);
        PackageManager pm = getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            camera.setEnabled(false);
        } else {
            camera.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    takePic();
                }
            });
        }


    }


    private void takePic() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }



    //starts an intent which allows user to select only specific image types
    // I think that's what android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI is specifying
    private void pickFromGallery() {
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
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }




        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
        }

        Button confirm = findViewById(R.id.confirm);
        confirm.setVisibility(View.VISIBLE);
        detectLabelsInImage();
    }

    public void detectLabelsInImage() {
        keywords = new HashMap<String, Float>();
        FirebaseVisionImage imageToCheck = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                .getOnDeviceImageLabeler();

        labeler.processImage(imageToCheck).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {

                        for (FirebaseVisionImageLabel label: labels) {
                            System.out.println(label.getText());
                            keywords.put(label.getText(), label.getConfidence());
                        }

                        Button confirm = findViewById(R.id.confirm);

                        confirm.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                // Do something in response to button click
                                EditText editText = findViewById(R.id.editText);

                                String txt = editText.getText().toString();
                                if (txt == null || txt.length() == 0) {
                                    playlistName = "Spoterator Playlist";
                                } else {
                                    playlistName = txt;
                                }

                                finish();
                                Intent intent = new Intent(MainActivity.this, PlaylistActivity.class);
                                intent.putExtra("keywords", keywords);
                                intent.putExtra("token", token);
                                intent.putExtra("playlistName", playlistName);

                                startActivity(intent);
                            }
                        });




                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                        System.err.println("Error with firebase ML kit");
                    }
                });

    }
}



































