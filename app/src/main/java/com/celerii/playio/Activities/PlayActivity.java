package com.celerii.playio.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
import android.view.View;

import com.celerii.playio.R;
import com.celerii.playio.Services.MusicService;
import com.celerii.playio.Utility.Constants;
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

        Bundle bundle = getIntent().getExtras();
        currentTrack = (Track) bundle.getSerializable("current_track");
        if (currentTrack != null) {
            smartPlayControls = new SmartPlayControls(currentTrack);
            smartPlayControls.setShuffle(musicService.getShuffle());
            smartPlayControls.setRepeating(musicService.getRepeat());
        } else {
            smartPlayControls = new SmartPlayControls();
        }

        activityPlayBinding.setClickHandler(this);
        activityPlayBinding.setPlayControl(smartPlayControls);

        setSupportActionBar(activityPlayBinding.toolbar);
        activityPlayBinding.toolbar.setTitle(null);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        activityPlayBinding.toolbarTitle.setText(getString(R.string.app_name));

        // start MusicService
        Intent musicIntent = new Intent(this, MusicService.class);
        startService(musicIntent);
        bindService(musicIntent, serviceConnection, Context.BIND_AUTO_CREATE);

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

        LocalBroadcastManager.getInstance(this).registerReceiver(setInfoBroadcastReceiver, new IntentFilter(Constants.MUSIC_PLAYER_ON_SET_INFO_LISTENER_BROADCAST_INTENT_FILTER));
        LocalBroadcastManager.getInstance(this).registerReceiver(preparedBroadcastReceiver, new IntentFilter(Constants.MUSIC_PLAYER_ON_PREPARED_LISTENER_BROADCAST_INTENT_FILTER));
        LocalBroadcastManager.getInstance(this).registerReceiver(completedBroadcastReceiver, new IntentFilter(Constants.MUSIC_PLAYER_ON_COMPLETION_LISTENER_BROADCAST_INTENT_FILTER));
        LocalBroadcastManager.getInstance(this).registerReceiver(errorBroadcastReceiver, new IntentFilter(Constants.MUSIC_PLAYER_ON_ERROR_LISTENER_BROADCAST_INTENT_FILTER));
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
            smartPlayControls.setShuffle(true);
        } else if (viewID == R.id.previous) {
            musicService.previousSong();
        } else if (viewID == R.id.next) {
            musicService.nextSong();
        } else if (viewID == R.id.repeat) {
            musicService.setRepeat();
            smartPlayControls.setRepeating(true);
        }
    }

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

            int duration = musicService.getDuration();
            @SuppressLint("DefaultLocale") String durationString = String.format("%02d:%02d", duration / 60, duration % 60);
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
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };
}