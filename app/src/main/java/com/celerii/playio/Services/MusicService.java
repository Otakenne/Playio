package com.celerii.playio.Services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.celerii.playio.Utility.Constants;

public class MusicService extends Service {
    private final IBinder basBinder = new MusicServiceBinder();
    private String streamURL = "";
    MediaPlayer musicPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return basBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        musicPlayer = new MediaPlayer();
        musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        musicPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                Intent setInfoIntent = new Intent(Constants.MUSIC_PLAYER_ON_SET_INFO_LISTENER_BROADCAST_INTENT_FILTER);
                setInfoIntent.putExtra("what", what);
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(setInfoIntent);
                return false;
            }
        });

        musicPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                Intent preparedIntent = new Intent(Constants.MUSIC_PLAYER_ON_PREPARED_LISTENER_BROADCAST_INTENT_FILTER);
                preparedIntent.putExtra("isReady", true);
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(preparedIntent);
            }
        });

        musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent completeIntent = new Intent(Constants.MUSIC_PLAYER_ON_COMPLETION_LISTENER_BROADCAST_INTENT_FILTER);
                completeIntent.putExtra("isComplete", true);
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(completeIntent);
            }
        });

        musicPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Intent errorIntent = new Intent(Constants.MUSIC_PLAYER_ON_ERROR_LISTENER_BROADCAST_INTENT_FILTER);
                errorIntent.putExtra("isError", true);
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(errorIntent);
                return false;
            }
        });
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            try {
                streamURL = "https://firebasestorage.googleapis.com/v0/b/altarii-aa0e5.appspot.com/o/ELibrary%2FwAKoSvpuADThnif48BcfcXRrPhx1%2FATB%2C%20Topic%20%26%20A7S%20-%20Your%20Love%20(Lyrics).mp3?alt=media&token=490587b4-8e80-474a-b7b4-62f597efe0ae"; //(String) bundle.get("track_url");
                musicPlayer.setDataSource(streamURL);
                musicPlayer.prepareAsync();
//                musicPlayer.start();
            } catch (Exception e) {
//            errorText.setVisibility(View.VISIBLE);
            }
        }

        return Service.START_STICKY;
    }

    public void onStart(Intent intent, int startId) {
        // TODO
    }

    public IBinder onUnBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public void onStop() {

    }

    public void onPause() {

    }

    @Override
    public void onDestroy() {
        musicPlayer.stop();
        musicPlayer.release();
    }

    @Override
    public void onLowMemory() {

    }

    public void seek(int progress) {
        musicPlayer.seekTo(progress * 1000);
    }

    public void repeat() {
        musicPlayer.setLooping(!musicPlayer.isLooping());
    }

    public void shuffle() {

    }

    public void pause() {
        if (musicPlayer.isPlaying())
            musicPlayer.pause();
    }

    public void play() {
        if (!musicPlayer.isPlaying()) {
            musicPlayer.start();
        }
    }

    public void previousSong() {

    }

    public void nextSong() {

    }

    public int getPausedLocation() {
        return musicPlayer.getCurrentPosition();
    }

    public boolean isPlaying() {
        return musicPlayer.isPlaying();
    }

    public class MusicServiceBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
}
