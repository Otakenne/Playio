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
import com.celerii.playio.mods.Track;

import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service {
    private final IBinder basBinder = new MusicServiceBinder();
    private String streamURL = "";
    private String artist = "";
    private String name = "";
    private String imageURL = "";

    private ArrayList<Track> tracks;

    private int currentlyPlaying = 0;
    private boolean setRepeat = true;
    private boolean setShuffle = true;

    MediaPlayer musicPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return basBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (musicPlayer == null) {
            musicPlayer = new MediaPlayer();
            musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        musicPlayer.setOnInfoListener((mp, what, extra) -> {
            Intent setInfoIntent = new Intent(Constants.MUSIC_PLAYER_ON_SET_INFO_LISTENER_BROADCAST_INTENT_FILTER);
            setInfoIntent.putExtra("what", what);
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(setInfoIntent);
            return false;
        });

        musicPlayer.setOnPreparedListener(mp -> {
            mp.start();
            Intent preparedIntent = new Intent(Constants.MUSIC_PLAYER_ON_PREPARED_LISTENER_BROADCAST_INTENT_FILTER);
            preparedIntent.putExtra("isReady", true);
            preparedIntent.putExtra(Constants.TRACK_FOR_MUSIC_SERVICE_INTENT, tracks.get(currentlyPlaying));
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(preparedIntent);
        });

        musicPlayer.setOnCompletionListener(mp -> {
            if (currentlyPlaying < tracks.size() - 1) {
                currentlyPlaying++;

//                streamURL = tracks.get(currentlyPlaying).getAudio();
//                name = tracks.get(currentlyPlaying).getName();
//                artist = tracks.get(currentlyPlaying).getArtist_name();
//                imageURL = tracks.get(currentlyPlaying).getImage();

                Track currentTrack = tracks.get(currentlyPlaying);
                musicPlayer.reset();
                try {
                    musicPlayer.setDataSource(currentTrack.getAudio());
                } catch (IOException e) {
                    Intent completeIntent = new Intent(Constants.MUSIC_PLAYER_ON_COMPLETION_LISTENER_BROADCAST_INTENT_FILTER);
                    completeIntent.putExtra("isComplete", true);
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(completeIntent);
                }
                musicPlayer.prepareAsync();
            } else {
                if (setRepeat) {
                    currentlyPlaying = 0;

//                    streamURL = tracks.get(currentlyPlaying).getAudio();
//                    name = tracks.get(currentlyPlaying).getName();
//                    artist = tracks.get(currentlyPlaying).getArtist_name();
//                    imageURL = tracks.get(currentlyPlaying).getImage();

                    Track currentTrack = tracks.get(currentlyPlaying);
                    musicPlayer.reset();
                    try {
                        musicPlayer.setDataSource(currentTrack.getAudio());
                    } catch (IOException e) {
                        Intent completeIntent = new Intent(Constants.MUSIC_PLAYER_ON_COMPLETION_LISTENER_BROADCAST_INTENT_FILTER);
                        completeIntent.putExtra("isComplete", true);
                        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(completeIntent);
                    }
                    musicPlayer.prepareAsync();
                } else {
                    Intent completeIntent = new Intent(Constants.MUSIC_PLAYER_ON_COMPLETION_LISTENER_BROADCAST_INTENT_FILTER);
                    completeIntent.putExtra("isComplete", true);
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(completeIntent);
                }
            }
        });

        musicPlayer.setOnErrorListener((mp, what, extra) -> {
            Intent errorIntent = new Intent(Constants.MUSIC_PLAYER_ON_ERROR_LISTENER_BROADCAST_INTENT_FILTER);
            errorIntent.putExtra("isError", true);
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(errorIntent);
            return false;
        });
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            try {
                tracks = (ArrayList<Track>) intent.getSerializableExtra(Constants.TRACK_LIST_FOR_MUSIC_SERVICE_INTENT);

                currentlyPlaying = 0;

                streamURL = tracks.get(currentlyPlaying).getAudio();
                name = tracks.get(currentlyPlaying).getName();
                artist = tracks.get(currentlyPlaying).getArtist_name();
                imageURL = tracks.get(currentlyPlaying).getImage();

                musicPlayer.reset();
                musicPlayer.setDataSource(tracks.get(currentlyPlaying).getAudio());
                musicPlayer.prepareAsync();
            } catch (Exception e) {
//                errorText.setVisibility(View.VISIBLE);
            }
        }

        return Service.START_STICKY;
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

    public boolean musicPlayerIsNotNull() {
        return musicPlayer != null;
    }

    public void setRepeat() {
        setRepeat = !setRepeat;
    }

    public void setShuffle() {
        setShuffle = !setShuffle;
    }

    public boolean getRepeat() {
        return setRepeat;
    }

    public boolean getShuffle() {
        return setShuffle;
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
        if (currentlyPlaying != 0) {
            currentlyPlaying--;
        }

        Track currentTrack = tracks.get(currentlyPlaying);
        musicPlayer.reset();
        try {
            musicPlayer.setDataSource(currentTrack.getAudio());
        } catch (IOException e) {
            Intent completeIntent = new Intent(Constants.MUSIC_PLAYER_ON_COMPLETION_LISTENER_BROADCAST_INTENT_FILTER);
            completeIntent.putExtra("isComplete", true);
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(completeIntent);
        }
        musicPlayer.prepareAsync();
    }

    public void nextSong() {
        if (currentlyPlaying == tracks.size() - 1) {
            currentlyPlaying = 0;
            if (setRepeat) {
                Track currentTrack = tracks.get(currentlyPlaying);
                musicPlayer.reset();
                try {
                    musicPlayer.setDataSource(currentTrack.getAudio());
                } catch (IOException e) {
                    Intent completeIntent = new Intent(Constants.MUSIC_PLAYER_ON_COMPLETION_LISTENER_BROADCAST_INTENT_FILTER);
                    completeIntent.putExtra("isComplete", true);
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(completeIntent);
                }
                musicPlayer.prepareAsync();
            } else {
                Track currentTrack = tracks.get(currentlyPlaying);
                musicPlayer.reset();

                try {
                    musicPlayer.setDataSource(currentTrack.getAudio());
                } catch (IOException e) {
                    Intent completeIntent = new Intent(Constants.MUSIC_PLAYER_ON_COMPLETION_LISTENER_BROADCAST_INTENT_FILTER);
                    completeIntent.putExtra("isComplete", true);
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(completeIntent);
                }
                musicPlayer.prepareAsync();
                musicPlayer.pause();

                Intent completeIntent = new Intent(Constants.MUSIC_PLAYER_ON_COMPLETION_LISTENER_BROADCAST_INTENT_FILTER);
                completeIntent.putExtra("isComplete", true);
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(completeIntent);
            }
        } else {
            currentlyPlaying++;

            Track currentTrack = tracks.get(currentlyPlaying);
            musicPlayer.reset();
            try {
                musicPlayer.setDataSource(currentTrack.getAudio());
            } catch (IOException e) {
                Intent completeIntent = new Intent(Constants.MUSIC_PLAYER_ON_COMPLETION_LISTENER_BROADCAST_INTENT_FILTER);
                completeIntent.putExtra("isComplete", true);
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(completeIntent);
            }
            musicPlayer.prepareAsync();
        }
    }

    public int getCurrentLocation() {
        return musicPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return musicPlayer.getDuration();
    }

    public boolean isPlaying() {
        return musicPlayer.isPlaying();
    }

    public class MusicServiceBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
