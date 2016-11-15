package io.djnr.backdrop_v2;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dj on 11/14/2016.
 */
public class MainActivity extends SpotifyActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavView;

    // nav index to identify current nav menu item
    public static int navItemIndex = 0;
    // nav tags used to attach the fragments
    private static final String TAG_SEARCH = "search";
    private static final String TAG_SPOTLIGHT = "spotlight";
    private static final String TAG_SAVED = "saved";
    private static final String TAG_SPOTIFY = "spotify";
    public static String CURRENT_TAG = TAG_SPOTLIGHT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainFragment()).commit();
    }

    public void setupNavToggle(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_search:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_SEARCH;
                        break;
                    case R.id.nav_spotlight:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_SPOTLIGHT;
                        break;
                    case R.id.nav_saved:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_SAVED;
                        break;
                    case R.id.nav_spotify:
                        mDrawerLayout.closeDrawers();
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SPOTIFY;
                        requestSpotifyLogin();
                        break;
                    case R.id.nav_logout:
                        mDrawerLayout.closeDrawers();
                        //TODO:LOGOUT
                        break;
                    default:
                        navItemIndex = 0;
                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }

                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void requestSpotifyLogin() {
        super.requestSpotifyLogin();
    }
}
