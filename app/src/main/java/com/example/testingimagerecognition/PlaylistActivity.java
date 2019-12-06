package com.example.testingimagerecognition;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

    /**
     * API token
     */
    private String token;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        token = getIntent().getStringExtra("token");

        keywords = (HashMap<String, Float>) getIntent().getSerializableExtra("keywords");



        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.spotify.com/v1/search";

        System.out.println("token: " + token);
        System.out.println("keywords: " + keywords);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error");
            }
        });




    }

    private List<String> getSongs(Map<String, Float> queries) {
        return null;
    }
}
