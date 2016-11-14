package io.djnr.backdrop_v2;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        setupNavToggle(mToolbar, mDrawerLayout, mNavView);
    }

    public void setupNavToggle(Toolbar toolbar, final DrawerLayout drawerLayout, NavigationView navView) {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

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
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SPOTIFY;
                        break;
                    case R.id.nav_logout:
                        drawerLayout.closeDrawers();
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
                (this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }
}
