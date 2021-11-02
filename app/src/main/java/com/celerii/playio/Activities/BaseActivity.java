package com.celerii.playio.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.celerii.playio.Enums.BottomNavigationItems;
import com.celerii.playio.Fragments.AlbumDetailFragment;
import com.celerii.playio.Fragments.AlbumsFragment;
import com.celerii.playio.Fragments.ArtistDetailFragment;
import com.celerii.playio.Fragments.ArtistsFragment;
import com.celerii.playio.Fragments.HomeFragment;
import com.celerii.playio.Fragments.TracksFragment;
import com.celerii.playio.R;
import com.celerii.playio.Utility.Constants;
import com.celerii.playio.databinding.ActivityBaseBinding;
import com.celerii.playio.mods.BottomNavigation;

import java.util.List;
import java.util.Objects;

public class BaseActivity extends AppCompatActivity {

    private static final String SELECTED_BOTTOM_NAV_KEY = "selected_bottom_nav_key";
    private static final String ACTION_BAR_HOME_BUTTON_ENABLED_KEY = "action_bar_home_button_enabled_key";
    private static final String ACTIVE_FRAG = "active_frag";

    ActivityBaseBinding activityBaseBinding;
    BottomNavigation bottomNavigation;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment activeFrag;

    HomeFragment homeFragment;
    TracksFragment tracksFragment;
    ArtistsFragment artistsFragment;
    AlbumsFragment albumsFragment;
    ArtistDetailFragment artistDetailFragment;
    AlbumDetailFragment albumDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base);
        initializeUI();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SELECTED_BOTTOM_NAV_KEY)) {
                bottomNavigation.setBottomNavigationItems((BottomNavigationItems) savedInstanceState.get(SELECTED_BOTTOM_NAV_KEY));
            } else {
                bottomNavigation.setBottomNavigationItems(BottomNavigationItems.HOME);
            }

            if (savedInstanceState.containsKey(ACTION_BAR_HOME_BUTTON_ENABLED_KEY)) {
                setActionBarHomeButton(savedInstanceState.getBoolean(ACTION_BAR_HOME_BUTTON_ENABLED_KEY));
            } else {
                setActionBarHomeButton(false);
            }
        } else {
            bottomNavigation.setBottomNavigationItems(BottomNavigationItems.HOME);
            setActionBarHomeButton(false);
        }

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();

        if (savedInstanceState == null) {
            homeFragment = HomeFragment.newInstance();
            tracksFragment = TracksFragment.newInstance();
            artistsFragment = ArtistsFragment.newInstance();
            albumsFragment = AlbumsFragment.newInstance();

            if (bottomNavigation.getBottomNavigationItems() == BottomNavigationItems.HOME) {
                fragmentTransaction.remove(homeFragment);
                fragmentTransaction.add(R.id.fragment_container, albumsFragment, Constants.ALBUMS_FRAGMENT_TAG).addToBackStack(null).hide(albumsFragment);
                fragmentTransaction.add(R.id.fragment_container, artistsFragment, Constants.ARTISTS_FRAGMENT_TAG).addToBackStack(null).hide(artistsFragment);
                fragmentTransaction.add(R.id.fragment_container, tracksFragment, Constants.TRACKS_FRAGMENT_TAG).addToBackStack(null).hide(tracksFragment);
                fragmentTransaction.add(R.id.fragment_container, homeFragment, Constants.HOME_FRAGMENT_TAG).addToBackStack(null);
                fragmentTransaction.commit();
                activeFrag = homeFragment;
            } else if (bottomNavigation.getBottomNavigationItems() == BottomNavigationItems.TRACK) {
                fragmentTransaction.remove(tracksFragment);
                fragmentTransaction.add(R.id.fragment_container, homeFragment, Constants.HOME_FRAGMENT_TAG).addToBackStack(null).hide(homeFragment);
                fragmentTransaction.add(R.id.fragment_container, artistsFragment, Constants.ARTISTS_FRAGMENT_TAG).addToBackStack(null).hide(artistsFragment);
                fragmentTransaction.add(R.id.fragment_container, albumsFragment, Constants.ALBUMS_FRAGMENT_TAG).addToBackStack(null).hide(albumsFragment);
                fragmentTransaction.add(R.id.fragment_container, tracksFragment, Constants.TRACKS_FRAGMENT_TAG).addToBackStack(null);
                fragmentTransaction.commit();
                activeFrag = tracksFragment;
            } else if (bottomNavigation.getBottomNavigationItems() == BottomNavigationItems.ARTIST) {
                fragmentTransaction.remove(artistsFragment);
                fragmentTransaction.add(R.id.fragment_container, homeFragment, Constants.HOME_FRAGMENT_TAG).addToBackStack(null).hide(homeFragment);
                fragmentTransaction.add(R.id.fragment_container, tracksFragment, Constants.TRACKS_FRAGMENT_TAG).addToBackStack(null).hide(tracksFragment);
                fragmentTransaction.add(R.id.fragment_container, albumsFragment, Constants.ALBUMS_FRAGMENT_TAG).addToBackStack(null).hide(albumsFragment);
                fragmentTransaction.add(R.id.fragment_container, artistsFragment, Constants.ARTISTS_FRAGMENT_TAG).addToBackStack(null);
                fragmentTransaction.commit();
                activeFrag = artistsFragment;
            } else {
                fragmentTransaction.remove(albumsFragment);
                fragmentTransaction.add(R.id.fragment_container, homeFragment, Constants.HOME_FRAGMENT_TAG).addToBackStack(null).hide(homeFragment);
                fragmentTransaction.add(R.id.fragment_container, tracksFragment, Constants.TRACKS_FRAGMENT_TAG).addToBackStack(null).hide(tracksFragment);
                fragmentTransaction.add(R.id.fragment_container, artistsFragment, Constants.ARTISTS_FRAGMENT_TAG).addToBackStack(null).hide(artistsFragment);
                fragmentTransaction.add(R.id.fragment_container, albumsFragment, Constants.ALBUMS_FRAGMENT_TAG).addToBackStack(null);
                fragmentTransaction.commit();
                activeFrag = albumsFragment;
            }
        } else {
            activeFrag = getSupportFragmentManager().getFragment(savedInstanceState, ACTIVE_FRAG);
            homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(Constants.HOME_FRAGMENT_TAG);
            tracksFragment = (TracksFragment) fragmentManager.findFragmentByTag(Constants.TRACKS_FRAGMENT_TAG);
            artistsFragment = (ArtistsFragment) fragmentManager.findFragmentByTag(Constants.ARTISTS_FRAGMENT_TAG);
            albumsFragment = (AlbumsFragment) fragmentManager.findFragmentByTag(Constants.ALBUMS_FRAGMENT_TAG);
            artistDetailFragment = (ArtistDetailFragment) fragmentManager.findFragmentByTag(Constants.ARTISTS_DETAIL_FRAGMENT_TAG);
            albumDetailFragment = (AlbumDetailFragment) fragmentManager.findFragmentByTag(Constants.ALBUMS_DETAIL_FRAGMENT_TAG);

//            if (bottomNavigation.getBottomNavigationItems() == BottomNavigationItems.HOME) {
//                showHomeFragment();
//            } else if (bottomNavigation.getBottomNavigationItems() == BottomNavigationItems.TRACK) {
//                showTracksFragment();
//            } else if (bottomNavigation.getBottomNavigationItems() == BottomNavigationItems.ARTIST) {
//                showArtistsFragment();
//            } else if (bottomNavigation.getBottomNavigationItems() == BottomNavigationItems.ALBUM) {
//                showAlbumsFragment();
//            }
//
//            if (activeFrag instanceof HomeFragment) {
//
//            } else if (activeFrag instanceof TracksFragment) {
//
//            } else if (activeFrag instanceof ArtistsFragment) {
//
//            } else if (activeFrag instanceof AlbumsFragment) {
//
//            }
        }

        activityBaseBinding.home.setOnClickListener(v -> showHomeFragment());

        activityBaseBinding.tracks.setOnClickListener(v -> showTracksFragment());

        activityBaseBinding.artists.setOnClickListener(v -> showArtistsFragment());

        activityBaseBinding.albums.setOnClickListener(v -> showAlbumsFragment());
    }

    private Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    private void showHomeFragment() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Constants.SHARED_PREFERENCES_MODE);
        boolean isHomeArtistDetailsFragmentVisible = sharedPreferences.getBoolean(Constants.HOME_ARTIST_DETAILS_FRAGMENT_VISIBLE, false);
        boolean isHomeAlbumDetailsFragmentVisible = sharedPreferences.getBoolean(Constants.HOME_ALBUM_DETAILS_FRAGMENT_VISIBLE, false);

        if (isHomeArtistDetailsFragmentVisible) {
            activeFrag = getVisibleFragment();
            artistDetailFragment = (ArtistDetailFragment) fragmentManager.findFragmentByTag(Constants.ARTISTS_DETAIL_FRAGMENT_TAG);
            fragmentManager.beginTransaction().hide(activeFrag).show(artistDetailFragment).commit();
            activeFrag = artistDetailFragment;
            setActionBarHomeButton(true);
        } else if (isHomeAlbumDetailsFragmentVisible) {
            activeFrag = getVisibleFragment();
            albumDetailFragment = (AlbumDetailFragment) fragmentManager.findFragmentByTag(Constants.ALBUMS_DETAIL_FRAGMENT_TAG);
            fragmentManager.beginTransaction().hide(activeFrag).show(albumDetailFragment).commit();
            activeFrag = albumDetailFragment;
            setActionBarHomeButton(true);
        } else {
            showFragment(homeFragment);
            setActionBarHomeButton(false);
        }

        bottomNavigation.setBottomNavigationItems(BottomNavigationItems.HOME);
    }

    private void showTracksFragment() {
        showFragment(tracksFragment);
        setActionBarHomeButton(false);
        bottomNavigation.setBottomNavigationItems(BottomNavigationItems.TRACK);
    }

    private void showArtistsFragment() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Constants.SHARED_PREFERENCES_MODE);

        if (sharedPreferences.getBoolean(Constants.ARTIST_DETAILS_FRAGMENT_VISIBLE, false)) {
            activeFrag = getVisibleFragment();
            artistDetailFragment = (ArtistDetailFragment) fragmentManager.findFragmentByTag(Constants.ARTISTS_DETAIL_FRAGMENT_TAG);
            fragmentManager.beginTransaction().hide(activeFrag).show(artistDetailFragment).commit();
            activeFrag = artistDetailFragment;
            setActionBarHomeButton(true);
        } else {
            showFragment(artistsFragment);
            setActionBarHomeButton(false);
        }

        bottomNavigation.setBottomNavigationItems(BottomNavigationItems.ARTIST);
    }

    private void showAlbumsFragment() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Constants.SHARED_PREFERENCES_MODE);

        if (sharedPreferences.getBoolean(Constants.ALBUM_DETAILS_FRAGMENT_VISIBLE, false)) {
            activeFrag = getVisibleFragment();
            albumDetailFragment = (AlbumDetailFragment) fragmentManager.findFragmentByTag(Constants.ALBUMS_DETAIL_FRAGMENT_TAG);
            fragmentManager.beginTransaction().hide(activeFrag).show(albumDetailFragment).commit();
            activeFrag = albumDetailFragment;
            setActionBarHomeButton(true);
        } else {
            showFragment(albumsFragment);
            setActionBarHomeButton(false);
        }

        bottomNavigation.setBottomNavigationItems(BottomNavigationItems.ALBUM);
    }

    private void showFragment(Fragment fragment) {
        activeFrag = getVisibleFragment();
        fragmentManager.beginTransaction().hide(activeFrag).show(fragment).commit();
        activeFrag = fragment;
    }

    private void initializeUI() {
        // Initialize bottom navigation
        bottomNavigation = new BottomNavigation();
        activityBaseBinding.setBottomNav(bottomNavigation);

        // Initialize Toolbar
        setSupportActionBar(activityBaseBinding.toolbar);
        activityBaseBinding.toolbar.setTitle(null);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        activityBaseBinding.toolbarTitle.setText(getString(R.string.app_name));

        activityBaseBinding.smartPlayControls.setVisibility(View.GONE);
    }

    private void setActionBarHomeButton(Boolean value) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(value);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(value);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        boolean homeButtonEnabled = (Objects.requireNonNull(getSupportActionBar()).getDisplayOptions() & ActionBar.DISPLAY_HOME_AS_UP) != 0;

        outState.putSerializable(SELECTED_BOTTOM_NAV_KEY, bottomNavigation.getBottomNavigationItems());
        outState.putBoolean(ACTION_BAR_HOME_BUTTON_ENABLED_KEY, homeButtonEnabled);

        if (getVisibleFragment() != null) {
            fragmentManager.putFragment(outState, ACTIVE_FRAG, getVisibleFragment());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Constants.SHARED_PREFERENCES_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (bottomNavigation.getBottomNavigationItems() == BottomNavigationItems.HOME) {
            editor.putBoolean(Constants.HOME_ARTIST_DETAILS_FRAGMENT_VISIBLE, false);
            editor.putBoolean(Constants.HOME_ALBUM_DETAILS_FRAGMENT_VISIBLE, false);
        } else if (bottomNavigation.getBottomNavigationItems() == BottomNavigationItems.ARTIST) {
            editor.putBoolean(Constants.ARTIST_DETAILS_FRAGMENT_VISIBLE, false);
        } else if (bottomNavigation.getBottomNavigationItems() == BottomNavigationItems.ALBUM) {
            editor.putBoolean(Constants.ALBUM_DETAILS_FRAGMENT_VISIBLE, false);
        }

        setActionBarHomeButton(false);

        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.theme_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.dark_mode){
            int nightMode = AppCompatDelegate.getDefaultNightMode();

            // if "night mode" is active, set to day and vice versa
            if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}