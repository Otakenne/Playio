package com.celerii.playio.Activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.celerii.playio.R;
import com.celerii.playio.Services.MusicService;
import com.celerii.playio.Utility.Constants;
import com.celerii.playio.Utility.FormatDuration;
import com.celerii.playio.databinding.ActivityPlayBinding;
import com.celerii.playio.interfaces.OnClickHandlerInterface;
import com.celerii.playio.mods.SmartPlayControls;
import com.celerii.playio.mods.Track;

import java.util.Objects;

public class PlayActivity extends AppCompatActivity implements OnClickHandlerInterface {

    private ActivityPlayBinding activityPlayBinding;
    public static SmartPlayControls smartPlayControls;

    private Track currentTrack = null;

    private MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPlayBinding = DataBindingUtil.setContentView(this, R.layout.activity_play);

        // start MusicService
        Intent musicIntent = new Intent(this, MusicService.class);
        startService(musicIntent);
        bindService(musicIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        Bundle bundle = getIntent().getExtras();
        currentTrack = (Track) bundle.getSerializable("current_track");

        activityPlayBinding.setClickHandler(this);
        activityPlayBinding.setPlayControl(smartPlayControls);

        setSupportActionBar(activityPlayBinding.toolbar);
        activityPlayBinding.toolbar.setTitle(null);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityPlayBinding.toolbarTitle.setText(getString(R.string.app_name));

        LocalBroadcastManager.getInstance(this).registerReceiver(setInfoBroadcastReceiver, new IntentFilter(Constants.MUSIC_PLAYER_ON_SET_INFO_LISTENER_BROADCAST_INTENT_FILTER));
        LocalBroadcastManager.getInstance(this).registerReceiver(preparedBroadcastReceiver, new IntentFilter(Constants.MUSIC_PLAYER_ON_PREPARED_LISTENER_BROADCAST_INTENT_FILTER));
        LocalBroadcastManager.getInstance(this).registerReceiver(completedBroadcastReceiver, new IntentFilter(Constants.MUSIC_PLAYER_ON_COMPLETION_LISTENER_BROADCAST_INTENT_FILTER));
        LocalBroadcastManager.getInstance(this).registerReceiver(errorBroadcastReceiver, new IntentFilter(Constants.MUSIC_PLAYER_ON_ERROR_LISTENER_BROADCAST_INTENT_FILTER));
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
            finish();
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
    }

    @Override
    public void onClick(View view, int position) {
        int viewID = view.getId();
        if (viewID == R.id.play_pause) {
            if (musicService.isPlaying()) {
                musicService.pause();
                smartPlayControls.setPlaying(false);
            } else {
                musicService.play();
                smartPlayControls.setPlaying(true);
            }
        } else if (viewID == R.id.shuffle) {
            musicService.setShuffle();
            smartPlayControls.setShuffle(!smartPlayControls.isShuffle());
        } else if (viewID == R.id.previous) {
            musicService.previousSong();
        } else if (viewID == R.id.next) {
            musicService.nextSong();
        } else if (viewID == R.id.repeat) {
            musicService.setRepeat();
            smartPlayControls.setRepeating(!smartPlayControls.isRepeating());
        }
    }

//    @Override
//    public void OnSeekBarChangeListener(SeekBar seekbar) {
//        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if(musicService.musicPlayerIsNotNull() && fromUser){
//                    musicService.seek(progress);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//    }

    public BroadcastReceiver setInfoBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int what = intent.getIntExtra("what", MediaPlayer.MEDIA_INFO_BUFFERING_START);

            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                smartPlayControls.setPlaying(false);
                smartPlayControls.setLoading(true);
                smartPlayControls.setLoadFailed(false);
            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                smartPlayControls.setPlaying(true);
                smartPlayControls.setLoading(false);
                smartPlayControls.setLoadFailed(false);
            }
        }
    };

    public BroadcastReceiver preparedBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            currentTrack = (Track) intent.getSerializableExtra(Constants.TRACK_FOR_MUSIC_SERVICE_INTENT);
            smartPlayControls = new SmartPlayControls(currentTrack);
            activityPlayBinding.setPlayControl(smartPlayControls);

            int duration = musicService.getDuration() / 1000;
            String durationString = FormatDuration.formatDuration(duration);
            smartPlayControls.setPlaying(true);
            smartPlayControls.setDurationInt(duration);
            smartPlayControls.setDuration(durationString);
            smartPlayControls.setLoading(false);
            smartPlayControls.setLoadFailed(false);
        }
    };

    public BroadcastReceiver completedBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            smartPlayControls.setPlaying(false);
       }
    };

    public BroadcastReceiver errorBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            smartPlayControls.setPlaying(false);
            smartPlayControls.setLoading(false);
            smartPlayControls.setLoadFailed(true);
        }
    };

    public final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.MusicServiceBinder) service).getService();

            if (currentTrack != null) {
                smartPlayControls = new SmartPlayControls(currentTrack);
                smartPlayControls.setShuffle(musicService.getShuffle());
                smartPlayControls.setRepeating(musicService.getRepeat());
                int duration = musicService.getDuration() / 1000;
                String durationString = FormatDuration.formatDuration(duration);
                smartPlayControls.setPlaying(true);
                smartPlayControls.setDurationInt(duration);
                smartPlayControls.setDuration(durationString);
                smartPlayControls.setLoading(false);
                smartPlayControls.setLoadFailed(false);
            } else {
                smartPlayControls = new SmartPlayControls();
            }

            Handler mHandler = new Handler();
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicService.musicPlayerIsNotNull()) {
                        try {
                            int mCurrentPosition = musicService.getCurrentLocation() / 1000;
                            smartPlayControls.setCurrentPositionInt(mCurrentPosition);
                            @SuppressLint("DefaultLocale") String currentString = String.format("%02d:%02d", mCurrentPosition / 60, mCurrentPosition % 60);
                            smartPlayControls.setCurrentPosition(currentString);
                        } catch (Exception e) {
                            Log.d("Media Player", e.toString());
                        }
                    }
                    mHandler.postDelayed(this, 1000);
                }
            });

            activityPlayBinding.playBackSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (musicService.musicPlayerIsNotNull() && fromUser){
                        musicService.seek(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            activityPlayBinding.setPlayControl(smartPlayControls);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };
}