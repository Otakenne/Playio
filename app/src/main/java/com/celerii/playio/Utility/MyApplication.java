package com.celerii.playio.Utility;

import android.app.Application;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.celerii.playio.Services.MusicService;

public class MyApplication extends Application implements LifecycleObserver {
    public static MusicService musicService;

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Constants.SHARED_PREFERENCES_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(Constants.HOME_ARTIST_DETAILS_FRAGMENT_VISIBLE, false);
        editor.putBoolean(Constants.ARTIST_DETAILS_FRAGMENT_VISIBLE, false);
        editor.putBoolean(Constants.HOME_ALBUM_DETAILS_FRAGMENT_VISIBLE, false);
        editor.putBoolean(Constants.ALBUM_DETAILS_FRAGMENT_VISIBLE, false);

        editor.apply();

//        unbindService(serviceConnection);
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
