package com.celerii.playio.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.celerii.playio.Enums.BottomNavigationItems;
import com.celerii.playio.Fragments.AlbumsFragment;
import com.celerii.playio.Fragments.ArtistsFragment;
import com.celerii.playio.Fragments.HomeFragment;
import com.celerii.playio.Fragments.TracksFragment;
import com.celerii.playio.R;
import com.celerii.playio.Utility.Constants;
import com.celerii.playio.databinding.ActivityBaseBinding;
import com.celerii.playio.mods.BottomNavigation;

public class BaseActivity extends AppCompatActivity {

    private static final String SELECTED_BOTTOM_NAV_KEY = "selected_bottom_nav_key";

    ActivityBaseBinding activityBaseBinding;
    BottomNavigation bottomNavigation;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment activeFrag;

    HomeFragment homeFragment;
    TracksFragment tracksFragment;
    ArtistsFragment artistsFragment;
    AlbumsFragment albumsFragment;

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
        } else {
            bottomNavigation.setBottomNavigationItems(BottomNavigationItems.HOME);
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
            homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(Constants.HOME_FRAGMENT_TAG);
            tracksFragment = (TracksFragment) fragmentManager.findFragmentByTag(Constants.TRACKS_FRAGMENT_TAG);
            artistsFragment = (ArtistsFragment) fragmentManager.findFragmentByTag(Constants.ARTISTS_FRAGMENT_TAG);
            albumsFragment = (AlbumsFragment) fragmentManager.findFragmentByTag(Constants.ALBUMS_FRAGMENT_TAG);
            activeFrag = fragmentManager.findFragmentById(R.id.fragment_container);

            if (bottomNavigation.getBottomNavigationItems() == BottomNavigationItems.HOME) {
                showHomeFragment();
            } else if (bottomNavigation.getBottomNavigationItems() == BottomNavigationItems.TRACK) {
                showTracksFragment();
            } else if (bottomNavigation.getBottomNavigationItems() == BottomNavigationItems.ARTIST) {
                showArtistsFragment();
            } else if (bottomNavigation.getBottomNavigationItems() == BottomNavigationItems.ALBUM) {
                showAlbumsFragment();
            }

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

        activityBaseBinding.home.setOnClickListener(v -> {
            showHomeFragment();
        });

        activityBaseBinding.tracks.setOnClickListener(v -> {
            showTracksFragment();
        });

        activityBaseBinding.artists.setOnClickListener(v -> {
            showArtistsFragment();
        });

        activityBaseBinding.albums.setOnClickListener(v -> {
            showAlbumsFragment();
        });
    }

    private void showHomeFragment() {
        fragmentManager.beginTransaction().hide(activeFrag).show(homeFragment).commit();
        activeFrag = homeFragment;
        bottomNavigation.setBottomNavigationItems(BottomNavigationItems.HOME);
    }

    private void showTracksFragment() {
        fragmentManager.beginTransaction().hide(activeFrag).show(tracksFragment).commit();
        activeFrag = tracksFragment;
        bottomNavigation.setBottomNavigationItems(BottomNavigationItems.TRACK);
    }

    private void showArtistsFragment() {
        fragmentManager.beginTransaction().hide(activeFrag).show(artistsFragment).commit();
        activeFrag = artistsFragment;
        bottomNavigation.setBottomNavigationItems(BottomNavigationItems.ARTIST);
    }

    private void showAlbumsFragment() {
        fragmentManager.beginTransaction().hide(activeFrag).show(albumsFragment).commit();
        activeFrag = albumsFragment;
        bottomNavigation.setBottomNavigationItems(BottomNavigationItems.ALBUM);
    }

    private void initializeUI() {
        // Initialize bottom navigation
        bottomNavigation = new BottomNavigation();
        activityBaseBinding.setBottomNav(bottomNavigation);

        // Initialize Toolbar
        setSupportActionBar(activityBaseBinding.toolbar);
        activityBaseBinding.toolbar.setTitle("");
        activityBaseBinding.toolbarTitle.setText(getString(R.string.app_name));

        activityBaseBinding.smartPlayControls.setVisibility(View.GONE);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SELECTED_BOTTOM_NAV_KEY, bottomNavigation.getBottomNavigationItems());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.theme_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.dark_mode){
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