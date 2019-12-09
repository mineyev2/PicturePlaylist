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
     * Stores a list of the songs generated as a list of Spotify Track objects
     */
    List<Track> songs;

    /**
     * API token
     */
    private String token;



    SpotifyApi api;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        token = getIntent().getStringExtra("token");
        keywords = (HashMap<String, Float>) getIntent().getSerializableExtra("keywords");
        songs = new ArrayList<Track>();

        api = new SpotifyApi();
        api.setAccessToken(token);
        new SongSearchTask().execute(keywords.keySet());
        new GeneratePlaylistTask().execute();


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

        protected void onPostExecute(List<Track> result) {
            System.out.println("songs after executing: " + songs);
        }

    }

    private class GeneratePlaylistTask extends AsyncTask {

        @Override
        protected Integer doInBackground(Object[] objects) {
            try {
                SpotifyService spotify = api.getService();
                Map<String, Object> createPlaylistOpts = new HashMap<String, Object>();
                createPlaylistOpts.put("name", "PicturePlaylist");
                createPlaylistOpts.put("description", "This is a PicturePlaylist generated playlist!");
                Playlist p = spotify.createPlaylist(spotify.getMe().display_name, createPlaylistOpts);

                Map<String, Object> bodyOpts = new HashMap<String, Object>();
                Map<String, Object> queryOpts = new HashMap<String, Object>();

                List<String> songList = new ArrayList<String>();

                Collections.shuffle(songs);
                if (songs.size() > 50) {
                    songs = songs.subList(0, 50);
                }

                for (Track song: songs) {
                    songList.add(song.uri);
                }

                bodyOpts.put("uris", songList);

                //bodyOpts.put("uris", songList.subList(0, 50));
                spotify.addTracksToPlaylist(spotify.getMe().id, p.id, queryOpts, bodyOpts);
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


}