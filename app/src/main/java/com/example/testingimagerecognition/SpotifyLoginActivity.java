package com.example.testingimagerecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;


public class SpotifyLoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1337;

    //i made my own client id stuff for now for testing purposes
    private static final String REDIRECT_URI = "https://testing.com/callback";
    private static final String CLIENT_ID = "1b2c382ea028460aac34f3b0d1f10f80";

    String token;

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

        Button help = findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpScreen();
            }
        });
    }

    private void spotifyAuthentification() {
        //System.out.println("Sign In Button");


        final AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming"})
                .build();


        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        SpotifyApi api = new SpotifyApi();

        api.setAccessToken(token);
        SpotifyService spotify = api.getService();
    }

    private void showHelpScreen() {
        System.out.println("Help");
        startActivity(new Intent(this, HelpActivity.class));
    }
  
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //System.out.println("running onActivityResult");
        super.onActivityResult(requestCode, resultCode, intent);


        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            //System.out.println("request code works");
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    token = response.getAccessToken();
                    //System.out.println("token: " + token);
                    //System.out.println("token:" + token);

                    Intent intentFinish = new Intent("finish_activity");
                    sendBroadcast(intentFinish);
                    Intent intentMain = new Intent(this, MainActivity.class)
                            .putExtra("token", token);

                    startActivity(intentMain);

                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
        finish();
    }

}










