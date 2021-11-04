package com.celerii.playio.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
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
    private Intent musicIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPlayBinding = DataBindingUtil.setContentView(this, R.layout.activity_play);

        activityPlayBinding.setClickHandler(this);
        activityPlayBinding.setPlayControl(smartPlayControls);

        Bundle bundle = getIntent().getExtras();
        currentTrack = (Track) bundle.getSerializable("current_track");

        setSupportActionBar(activityPlayBinding.toolbar);
        activityPlayBinding.toolbar.setTitle(null);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        activityPlayBinding.toolbarTitle.setText(getString(R.string.app_name));

        // start MusicService
        musicIntent = new Intent(this, MusicService.class);
        startService(musicIntent);
        bindService(musicIntent, serviceConnection, Context.BIND_AUTO_CREATE);

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
            musicService.shuffle();
            smartPlayControls.setShuffle(true);
        } else if (viewID == R.id.previous) {
            musicService.previousSong();
        } else if (viewID == R.id.next) {
            musicService.nextSong();
        } else if (viewID == R.id.repeat) {
            musicService.repeat();
            smartPlayControls.setRepeating(true);
        }
    }

    public BroadcastReceiver setInfoBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int what = intent.getIntExtra("what", MediaPlayer.MEDIA_INFO_BUFFERING_START);

            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                activityPlayBinding.loadingProgressBar.setVisibility(View.VISIBLE);
                activityPlayBinding.audioFileThumbnail.setVisibility(View.INVISIBLE);
                activityPlayBinding.audioFileThumbnailBackground.setVisibility(View.INVISIBLE);
                activityPlayBinding.playPause.setEnabled(false);
            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                activityPlayBinding.loadingProgressBar.setVisibility(View.INVISIBLE);
                activityPlayBinding.audioFileThumbnail.setVisibility(View.INVISIBLE);
                activityPlayBinding.audioFileThumbnailBackground.setVisibility(View.INVISIBLE);
                activityPlayBinding.playPause.setEnabled(true);
            }
        }
    };

    public BroadcastReceiver preparedBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            isPlaying = true;
//            duration = mediaPlayer.getDuration()/1000;
//            String durationString = String.format("%02d:%02d", duration / 60, duration % 60);
//            activityPlayBinding.totalTime.setText(durationString);
//            activityPlayBinding.playbackSeekBar.setMax(duration);
            activityPlayBinding.audioFileThumbnail.setVisibility(View.VISIBLE);
            activityPlayBinding.audioFileThumbnailBackground.setVisibility(View.VISIBLE);
            activityPlayBinding.loadingProgressBar.setVisibility(View.GONE);
            activityPlayBinding.errorText.setVisibility(View.GONE);
            activityPlayBinding.playPause.setEnabled(true);
//            activityPlayBinding.playPause.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_baseline_pause_circle_filled_24));
        }
    };

    public BroadcastReceiver completedBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            playPause.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_play_circle_filled_black_24dp));
        }
    };

    public BroadcastReceiver errorBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            activityPlayBinding.loadingProgressBar.setVisibility(View.GONE);
            activityPlayBinding.errorText.setVisibility(View.VISIBLE);
            activityPlayBinding.playPause.setEnabled(false);
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