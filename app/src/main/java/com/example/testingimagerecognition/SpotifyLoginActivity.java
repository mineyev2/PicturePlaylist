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
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.net.HttpURLConnection;

public class SpotifyLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spotifyAuthentification();
            }
        });
        try {
            String urlBase = "https://accounts.spotify.com/authorize";
            URL url = new URL(urlBase + "?client_id=a0f1241498384db5ba98d07fd0ec4b99"
                    + "&response_type=code" + "&redirect_uri=https://cs125.cs.illinois.edu/"
                    + "scope=user-read-private user-read-email");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            //DataOutputStream out = new DataOutputStream(url);
            //out.writeBytes(ParameterStringBuilder);
        } catch (Exception e) {
            Log.i("Error", e.toString());
        }



    }

    private void spotifyAuthentification() {
        System.out.println("Sign In Button");
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, 1);
    }

}










