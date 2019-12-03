package com.example.testingimagerecognition;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistActivity extends AppCompatActivity {

    /**
     * The keywords and their respective confidences derived from an image which are used to generate the songs.
     */
    Map<String, Float> keywords;

    /**
     * Stores a list of the songs generated as a list of strings each representing a Spotify Song ID
     */
    List<String> songs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        keywords = (HashMap<String, Float>) getIntent().getExtras().get("keywords");
        RequestQueue queue = Volley.newRequestQueue(this);


    }

    private List<String> getSongs(Map<String, Float> queries) {
        return null;
    }
}
