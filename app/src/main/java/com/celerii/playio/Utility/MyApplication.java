package com.celerii.playio.Utility;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public class MyApplication extends Application implements LifecycleObserver {
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
    }
}
