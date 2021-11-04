package com.celerii.playio.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
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
import com.celerii.playio.Services.MusicService;
import com.celerii.playio.Utility.Constants;
import com.celerii.playio.databinding.ActivityBaseBinding;
import com.celerii.playio.interfaces.OnClickHandlerInterface;
import com.celerii.playio.mods.BottomNavigation;
import com.celerii.playio.mods.SmartPlayControls;
import com.celerii.playio.mods.Track;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BaseActivity extends AppCompatActivity implements OnClickHandlerInterface {

    private static final String SELECTED_BOTTOM_NAV_KEY = "selected_bottom_nav_key";
    private static final String ACTION_BAR_HOME_BUTTON_ENABLED_KEY = "action_bar_home_button_enabled_key";
    private static final String SMART_CONTROL_STATE_KEY = "smart_control_state_key";
    private static final String ACTIVE_FRAG = "active_frag";

    private ActivityBaseBinding activityBaseBinding;
    private BottomNavigation bottomNavigation;
    public static SmartPlayControls smartPlayControls;

    private FragmentManager fragmentManager;
    private Fragment activeFrag;

    private HomeFragment homeFragment;
    private TracksFragment tracksFragment;
    private ArtistsFragment artistsFragment;
    private AlbumsFragment albumsFragment;
    private ArtistDetailFragment artistDetailFragment;
    private AlbumDetailFragment albumDetailFragment;

    private MusicService musicService;
    private Intent musicIntent;
    private Track currentTrack;

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

            if (savedInstanceState.containsKey(SMART_CONTROL_STATE_KEY)) {
                smartPlayControls = savedInstanceState.getParcelable(SMART_CONTROL_STATE_KEY);
                activityBaseBinding.setPlayControl(smartPlayControls);
                activityBaseBinding.smartPlayControls.setVisibility(View.VISIBLE);
            }
        } else {
            bottomNavigation.setBottomNavigationItems(BottomNavigationItems.HOME);
            setActionBarHomeButton(false);
        }

        fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

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
        }

        activityBaseBinding.home.setOnClickListener(v -> showHomeFragment());

        activityBaseBinding.tracks.setOnClickListener(v -> showTracksFragment());

        activityBaseBinding.artists.setOnClickListener(v -> showArtistsFragment());

        activityBaseBinding.albums.setOnClickListener(v -> showAlbumsFragment());

        LocalBroadcastManager.getInstance(this).registerReceiver(setInfoBroadcastReceiver, new IntentFilter(Constants.MUSIC_PLAYER_ON_SET_INFO_LISTENER_BROADCAST_INTENT_FILTER));
        LocalBroadcastManager.getInstance(this).registerReceiver(preparedBroadcastReceiver, new IntentFilter(Constants.MUSIC_PLAYER_ON_PREPARED_LISTENER_BROADCAST_INTENT_FILTER));
        LocalBroadcastManager.getInstance(this).registerReceiver(completedBroadcastReceiver, new IntentFilter(Constants.MUSIC_PLAYER_ON_COMPLETION_LISTENER_BROADCAST_INTENT_FILTER));
        LocalBroadcastManager.getInstance(this).registerReceiver(errorBroadcastReceiver, new IntentFilter(Constants.MUSIC_PLAYER_ON_ERROR_LISTENER_BROADCAST_INTENT_FILTER));
        LocalBroadcastManager.getInstance(this).registerReceiver(showSmartControlsBroadcastReceiver, new IntentFilter(Constants.SHOW_SMART_CONTROLS));
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
        smartPlayControls = new SmartPlayControls();
        activityBaseBinding.setBottomNav(bottomNavigation);
        activityBaseBinding.setPlayControl(smartPlayControls);
        activityBaseBinding.setClickHandler(this);

        // Initialize Toolbar
        setSupportActionBar(activityBaseBinding.toolbar);
        activityBaseBinding.toolbar.setTitle(null);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        activityBaseBinding.toolbarTitle.setText(getString(R.string.app_name));

        // start MusicService
        musicIntent = new Intent(this, MusicService.class);
        startService(musicIntent);
        bindService(musicIntent, serviceConnection, Context.BIND_AUTO_CREATE);

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
        outState.putParcelable(SMART_CONTROL_STATE_KEY, smartPlayControls);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        stopService(musicIntent);
    }

    public BroadcastReceiver setInfoBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int what = intent.getIntExtra("what", MediaPlayer.MEDIA_INFO_BUFFERING_START);

            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                smartPlayControls.setLoading(true);
                smartPlayControls.setPlaying(false);
            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                smartPlayControls.setLoading(false);
                smartPlayControls.setPlaying(true);
            }
        }
    };

    public BroadcastReceiver preparedBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            currentTrack = (Track) intent.getSerializableExtra(Constants.TRACK_FOR_MUSIC_SERVICE_INTENT);
//            String artist = intent.getStringExtra(Constants.TRACK_ARTIST_FOR_MUSIC_SERVICE_INTENT);
//            String imageURL = intent.getStringExtra(Constants.TRACK_IMAGE_URL_FOR_MUSIC_SERVICE_INTENT);

            activityBaseBinding.smartPlayControls.setVisibility(View.VISIBLE);
            smartPlayControls.setCurrentSong(currentTrack.getName());
            smartPlayControls.setCurrentSongImageURL(currentTrack.getImage());
            smartPlayControls.setLoading(false);
            smartPlayControls.setPlaying(true);
        }
    };

    public BroadcastReceiver completedBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            smartPlayControls.setLoading(false);
            smartPlayControls.setPlaying(false);
        }
    };

    public BroadcastReceiver errorBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            smartPlayControls.setCurrentSong("Error");
            smartPlayControls.setLoading(false);
            smartPlayControls.setPlaying(false);
        }
    };

    public BroadcastReceiver showSmartControlsBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean showControls = intent.getBooleanExtra("show_play_controls", true);

            if (showControls) {
                activityBaseBinding.smartPlayControls.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onClick(View view, int position) {
        int viewID = view.getId();
        if (viewID == R.id.play_button) {
            if (musicService.isPlaying()) {
                musicService.pause();
                smartPlayControls.setPlaying(false);
            } else {
                musicService.play();
                smartPlayControls.setPlaying(true);
            }
        } else if (viewID == R.id.smart_play_controls) {
            Intent intent = new Intent(this, PlayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("current_track", currentTrack);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.MusicServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };
}