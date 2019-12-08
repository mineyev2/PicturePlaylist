package com.example.testingimagerecognition;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
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



    SpotifyApi api = new SpotifyApi();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        token = getIntent().getStringExtra("token");
        keywords = (HashMap<String, Float>) getIntent().getSerializableExtra("keywords");
        songs = new ArrayList<String>();


        api.setAccessToken(token);
        new SongSearchTask().execute(keywords.keySet());


    }

    private class SongSearchTask extends AsyncTask<Set<String>, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Set<String>... lists) {
            try {
                for (String word: keywords.keySet()) {
                    SpotifyService spotify = api.getService();
                    TracksPager result = spotify.searchTracks(word);
                    List<Track> trackResult = result.tracks.items;
                    for (Track track : trackResult) {
                        songs.add(track.id);
                    }
                }
            } catch (Exception e) {
                System.err.println(e);
            }
            return songs;
        }

        protected void onPostExecute(List<String> result) {
            System.out.println("songs after executing: " + songs);
        }

    }
}