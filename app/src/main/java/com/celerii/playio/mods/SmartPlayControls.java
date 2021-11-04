package com.celerii.playio.mods;

import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.Bindable;

import com.bumptech.glide.Glide;
import com.celerii.playio.BR;
import com.celerii.playio.R;

public class SmartPlayControls extends BaseObservable {
    String currentSong, currentSongImageURL;
    boolean isPlaying, isLoading;

    public SmartPlayControls() {
        this.currentSong = "";
        this.currentSongImageURL = "";
        this.isPlaying = false;
        this.isLoading = false;
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
}
