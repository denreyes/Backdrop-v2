package io.djnr.backdrop_v2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

/**
 * Created by Dj on 11/14/2016.
 */
public class MainFragment extends Fragment {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private SpotifyApi mApi;
    private SpotifyService mSpotify;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        ((MainActivity) getActivity()).setupNavToggle(mToolbar);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_IDS.SPOTIFY_CONNECTED);
        getActivity().registerReceiver(spotifyConnectedReceiver, intentFilter);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(spotifyConnectedReceiver);
    }

    //RECEIVERS
    private BroadcastReceiver spotifyConnectedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mApi = new SpotifyApi();
            mApi = mApi.setAccessToken(intent.getStringExtra(Constants.SPOTIFY_CONNECTED_EXTRAS.OAUTH_TOKEN));
            mSpotify = mApi.getService();

            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    return mSpotify.getMe().id;
                }

                @Override
                protected void onPostExecute(String displayName) {
                    super.onPostExecute(displayName);
                    Toast.makeText(getActivity(), "Hey, " + displayName + "!", Toast.LENGTH_LONG).show();
                }
            }.execute();
        }
    };

}
