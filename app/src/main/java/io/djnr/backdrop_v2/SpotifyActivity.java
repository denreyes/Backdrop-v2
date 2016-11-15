package io.djnr.backdrop_v2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

/**
 * Created by Dj on 11/14/2016.
 */
public abstract class SpotifyActivity extends AppCompatActivity  implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback {
    private static final String TAG = SpotifyActivity.class.getSimpleName();

    private Player mPlayer;
    private static final int REQUEST_CODE = 1337;
    private boolean isLoggedIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLoggedIn = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), AppConfig.Spotify.CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(SpotifyActivity.this);
                        mPlayer.addNotificationCallback(SpotifyActivity.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    public void requestSpotifyLogin(){
        if(!isLoggedIn) {
            showTwoButtonedDialog(getString(R.string.spotify), getString(R.string.spotify_connect_warning),
                    getString(R.string.contnue), getString(R.string.cancel), connectSpotify, null);
        }else {
            showTwoButtonedDialog(getString(R.string.spotify), getString(R.string.spotify_disconnect_warning),
                    getString(R.string.disconnect), getString(R.string.cancel), disconnectSpotify, null);
        }
    }

    public void showTwoButtonedDialog(String title, String message, String positiveBtnLabel, String negativeBtnLabel,
                                      DialogInterface.OnClickListener positiveListener,
                                      DialogInterface.OnClickListener negativeListener) {
        final boolean[] retBoolean = new boolean[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (title != null) builder.setTitle(title);

        builder.setMessage(message);
        builder.setPositiveButton(positiveBtnLabel, positiveListener);
        builder.setNegativeButton(negativeBtnLabel, negativeListener);
        builder.show();
    }

    //Spotify Methods
    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d(TAG, "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d(TAG, "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
        isLoggedIn = true;
//        mPlayer.playUri(null, "spotify:track:2TpxZ7JUBn3uw46aR7qd6V", 0, 0);
    }

    @Override
    public void onLoggedOut() {
        Log.d(TAG, "User logged out");
        isLoggedIn = false;
    }

    @Override
    public void onLoginFailed(int i) {
        Log.d(TAG, "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d(TAG, "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d(TAG, "Received connection message: " + message);
    }

    private DialogInterface.OnClickListener connectSpotify
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(AppConfig.Spotify.CLIENT_ID,
                    AuthenticationResponse.Type.TOKEN,
                    AppConfig.Spotify.REDIRECT_URI);
            builder.setScopes(new String[]{"user-read-private", "streaming"});
            AuthenticationRequest request = builder.build();
            AuthenticationClient.openLoginActivity(SpotifyActivity.this, REQUEST_CODE, request);
        }
    };

    private DialogInterface.OnClickListener disconnectSpotify
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            AuthenticationClient.clearCookies(SpotifyActivity.this);
            onLoggedOut();
        }
    };
}
