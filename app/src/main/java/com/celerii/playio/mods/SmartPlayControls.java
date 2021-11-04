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
    String currentSong, currentSongImageURL, currentSongArtist;
    boolean isPlaying, isLoading, loadFailed, isShuffle, isRepeating;

    public SmartPlayControls() {
        this.currentSong = "";
        this.currentSongImageURL = "";
        this.isPlaying = false;
        this.isLoading = false;
    }

    protected SmartPlayControls(Parcel in) {
        currentSong = in.readString();
        currentSongImageURL = in.readString();
        isPlaying = in.readByte() != 0;
        isLoading = in.readByte() != 0;
    }

    public static final Creator<SmartPlayControls> CREATOR = new Creator<SmartPlayControls>() {
        @Override
        public SmartPlayControls createFromParcel(Parcel in) {
            return new SmartPlayControls(in);
        }

        @Override
        public SmartPlayControls[] newArray(int size) {
            return new SmartPlayControls[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currentSong);
        dest.writeString(currentSongImageURL);
        dest.writeByte((byte) (isPlaying ? 1 : 0));
        dest.writeByte((byte) (isLoading ? 1 : 0));
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
