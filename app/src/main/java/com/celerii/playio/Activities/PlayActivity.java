package com.celerii.playio.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.celerii.playio.R;
import com.celerii.playio.Utility.Constants;
import com.celerii.playio.databinding.ActivityPlayBinding;
import com.celerii.playio.mods.SmartPlayControls;

public class PlayActivity extends AppCompatActivity {

    private ActivityPlayBinding activityPlayBinding;
    public static SmartPlayControls smartPlayControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPlayBinding = DataBindingUtil.setContentView(this, R.layout.activity_play);

        LocalBroadcastManager.getInstance(this).registerReceiver(setInfoBroadcastReceiver, new IntentFilter(Constants.MUSIC_PLAYER_ON_SET_INFO_LISTENER_BROADCAST_INTENT_FILTER));
        LocalBroadcastManager.getInstance(this).registerReceiver(preparedBroadcastReceiver, new IntentFilter(Constants.MUSIC_PLAYER_ON_PREPARED_LISTENER_BROADCAST_INTENT_FILTER));
        LocalBroadcastManager.getInstance(this).registerReceiver(completedBroadcastReceiver, new IntentFilter(Constants.MUSIC_PLAYER_ON_COMPLETION_LISTENER_BROADCAST_INTENT_FILTER));
        LocalBroadcastManager.getInstance(this).registerReceiver(errorBroadcastReceiver, new IntentFilter(Constants.MUSIC_PLAYER_ON_ERROR_LISTENER_BROADCAST_INTENT_FILTER));
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
}