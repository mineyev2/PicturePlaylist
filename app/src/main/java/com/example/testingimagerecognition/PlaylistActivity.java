package com.example.testingimagerecognition;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Playlist;
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
     * Stores a list of the songs generated as a list of tracks each representing a song
     */
    List<Track> songs;

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
        songs = new ArrayList<Track>();


        api.setAccessToken(token);
        new SongSearchTask().execute(keywords.keySet());
        new GeneratePlaylistTask().execute(songs);


    }

    private class SongSearchTask extends AsyncTask<Set<String>, Void, List<Track>> {

        @Override
        protected List<Track> doInBackground(Set<String>... lists) {
            try {
                for (String word: keywords.keySet()) {
                    SpotifyService spotify = api.getService();
                    TracksPager result = spotify.searchTracks(word);
                    List<Track> trackResult = result.tracks.items;
                    for (Track track : trackResult) {
                        songs.add(track);
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

    private class GeneratePlaylistTask extends AsyncTask<List<Track>, Void, Integer> {

        @Override
        protected Integer doInBackground(List<Track>... lists) {
            try {
                SpotifyService spotify = api.getService();
                Map<String, Object> createPlaylistOpts = new HashMap<String, Object>();
                createPlaylistOpts.put("name", "PicturePlaylist");
                createPlaylistOpts.put("description", "This is a PicturePlaylist generated playlist!");
                Playlist p = spotify.createPlaylist(spotify.getMe().display_name, createPlaylistOpts);

                Map<String, Object> addToPlaylistOpts = new HashMap<String, Object>();
                List<String> songList = new ArrayList<String>();
                for (Track song: songs) {
                    songList.add(song.uri);
                }

                addToPlaylistOpts.put("uris", songs.subList(0, 50));
                spotify.addTracksToPlaylist(spotify.getMe().display_name, p.id, new HashMap<String, Object>(), addToPlaylistOpts);
                return 1;

            } catch (Exception e) {
                System.err.println(e);
                return 0;
            }

        }

        protected void onPostExecute(List<String> result) {
            System.out.println("Created Playlist: " + songs);
        }

    }

    private void filterSongs() {
        List<Integer> numbers = new ArrayList<>();
        Collections.shuffle(songs);
    }






}