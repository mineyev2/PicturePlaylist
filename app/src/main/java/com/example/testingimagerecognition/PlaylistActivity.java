package com.example.testingimagerecognition;

import android.os.AsyncTask;
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

        for (String word: keywords.keySet()) {
            new SongSearchTask().execute(word);
        }
    }

    private class SongSearchTask extends AsyncTask<String, Void, List<String>> {


        protected List<String> doInBackground(String... strings) {

            try {
                SpotifyService spotify = api.getService();
                TracksPager result = spotify.searchTracks(strings[0]);
                List<Track> trackResult = result.tracks.items;
                for (Track track : trackResult) {
                    songs.add(track.id);
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