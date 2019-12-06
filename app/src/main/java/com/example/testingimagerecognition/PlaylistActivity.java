package com.example.testingimagerecognition;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
        SpotifyApi api = new SpotifyApi();

        api.setAccessToken(token);
        SpotifyService spotify = api.getService();

        spotify.getAlbum("2dIGnmEIy1WZIcZCFSj6i8", new Callback<Album>() {
            @Override
            public void success(Album album, Response response) {
                System.out.println("Album success" + album.name);
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("Album failure" + error.toString());
            }
        });
        

    }

    private List<String> getSongs(Map<String, Float> queries) {
        return null;
    }
}
