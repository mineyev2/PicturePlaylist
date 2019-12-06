package com.example.testingimagerecognition;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
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
        songs = new ArrayList<>();
        SpotifyApi api = new SpotifyApi();

        api.setAccessToken(token);
        SpotifyService spotify = api.getService();

        System.out.println(keywords);
        for (String word: keywords.keySet()) {
            spotify.searchTracks(word, new Callback<TracksPager>() {
                @Override
                public void success(TracksPager pager, Response response) {
                    for (Track track: pager.tracks.items) {
                        songs.add(track.id);
                    }
                    System.out.println(songs);
                }

                @Override
                public void failure(RetrofitError error) {
                    System.err.println("Search failure" + error.toString());
                }
            });
        }




    }

    private List<String> getSongs(Map<String, Float> queries) {
        return null;
    }
}
