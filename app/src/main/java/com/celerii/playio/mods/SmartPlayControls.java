package com.celerii.playio.mods;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.Bindable;

import com.bumptech.glide.Glide;
import com.celerii.playio.BR;
import com.celerii.playio.R;

public class SmartPlayControls extends BaseObservable implements Parcelable {
    String currentSong, currentSongImageURL, currentSongArtist, currentPosition, duration;
    int currentPositionInt, durationInt;
    boolean isPlaying, isLoading, loadFailed, isShuffle, isRepeating;

    public SmartPlayControls() {
        this.currentSong = "";
        this.currentSongImageURL = "";
        this.currentSongArtist = "";
        this.currentPosition = "";
        this.duration = "";
        this.currentPositionInt = 0;
        this.durationInt = 0;
        this.isPlaying = false;
        this.isLoading = false;
        this.loadFailed = false;
        this.isShuffle = false;
        this.isRepeating = false;
    }

    public SmartPlayControls(Track track) {
        this.currentSong = track.getName();
        this.currentSongImageURL = track.image;
        this.currentSongArtist = track.artist_name;
        this.currentPosition = "";
        this.duration = "";
        this.currentPositionInt = 0;
        this.durationInt = (int) track.duration;
        this.isPlaying = false;
        this.isLoading = true;
        this.loadFailed = false;
        this.isShuffle = false;
        this.isRepeating = false;
    }

    @Bindable
    public String getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(String currentSong) {
        this.currentSong = currentSong;
        notifyPropertyChanged(BR.currentSong);
    }

    @Bindable
    public String getCurrentSongImageURL() {
        return currentSongImageURL;
    }

    public void setCurrentSongImageURL(String currentSongImageURL) {
        this.currentSongImageURL = currentSongImageURL;
        notifyPropertyChanged(BR.currentSongImageURL);
    }

    @Bindable
    public String getCurrentSongArtist() {
        return currentSongArtist;
    }

    public void setCurrentSongArtist(String currentSongArtist) {
        this.currentSongArtist = currentSongArtist;
        notifyPropertyChanged(BR.currentSongArtist);
    }

    @Bindable
    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
        notifyPropertyChanged(BR.currentPosition);
    }

    @Bindable
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
        notifyPropertyChanged(BR.duration);
    }

    public int getCurrentPositionInt() {
        return currentPositionInt;
    }

    public void setCurrentPositionInt(int currentPositionInt) {
        this.currentPositionInt = currentPositionInt;
    }

    public int getDurationInt() {
        return durationInt;
    }

    public void setDurationInt(int durationInt) {
        this.durationInt = durationInt;
    }

    @Bindable
    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
        notifyPropertyChanged(BR.playing);
    }

    @Bindable
    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.loading);
    }

    @Bindable
    public boolean isLoadFailed() {
        return loadFailed;
    }

    public void setLoadFailed(boolean loadFailed) {
        this.loadFailed = loadFailed;
        notifyPropertyChanged(BR.loadFailed);
    }

    @Bindable
    public boolean isShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean shuffle) {
        isShuffle = shuffle;
        notifyPropertyChanged(BR.shuffle);
    }

    @Bindable
    public boolean isRepeating() {
        return isRepeating;
    }

    public void setRepeating(boolean repeating) {
        isRepeating = repeating;
        notifyPropertyChanged(BR.repeating);
    }

    @BindingAdapter("image")
    public static void loadImage(ImageView imageView, String imageURL) {
        if (imageURL != null) {
            Glide.with(imageView.getContext())
                    .load(imageURL)
                    .into(imageView);
        }
    }

    @BindingAdapter("icon")
    public static void setIcon(ImageView imageView, boolean isPlaying) {
        if (isPlaying) {
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.ic_pause_accent));
        } else {
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.ic_play_accent));
        }
    }

    @BindingAdapter("visibility")
    public static void setVisibility(View view, boolean isVisible) {
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("customBackground")
    public static void setCustomBackground(View view, boolean showBackground) {
        if (showBackground) {
            view.setBackgroundResource(R.drawable.accent_circle_background);
        } else {
            view.setBackgroundResource(0);
        }
    }
}
