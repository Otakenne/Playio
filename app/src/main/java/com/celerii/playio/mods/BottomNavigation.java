package com.celerii.playio.mods;

import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.celerii.playio.BR;
import com.celerii.playio.Enums.BottomNavigationItems;
import com.celerii.playio.R;

public class BottomNavigation extends BaseObservable {
    BottomNavigationItems bottomNavigationItems;
    Boolean isHomeSelected, isTrackSelected, isArtistSelected, isAlbumSelected;

    public BottomNavigation() {

    }

    @Bindable
    public BottomNavigationItems getBottomNavigationItems() {
        return bottomNavigationItems;
    }

    public void setBottomNavigationItems(BottomNavigationItems bottomNavigationItems) {
        this.bottomNavigationItems = bottomNavigationItems;
        notifyPropertyChanged(BR.bottomNavigationItems);
    }

    public Boolean getHomeSelected() {
        return isHomeSelected;
    }

    public void setHomeSelected(Boolean homeSelected) {
        isHomeSelected = homeSelected;
    }

    public Boolean getTrackSelected() {
        return isTrackSelected;
    }

    public void setTrackSelected(Boolean trackSelected) {
        isTrackSelected = trackSelected;
    }

    public Boolean getArtistSelected() {
        return isArtistSelected;
    }

    public void setArtistSelected(Boolean artistSelected) {
        isArtistSelected = artistSelected;
    }

    public Boolean getAlbumSelected() {
        return isAlbumSelected;
    }

    public void setAlbumSelected(Boolean albumSelected) {
        isAlbumSelected = albumSelected;
    }

    @BindingAdapter("iconHome")
    public static void setIconHome(ImageView imageView, BottomNavigationItems bottomNavigationItems) {
        if (bottomNavigationItems.equals(BottomNavigationItems.HOME)) {
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.ic_home_selected));
        } else {
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.ic_home));
        }
    }

    @BindingAdapter("iconTrack")
    public static void setIconTrack(ImageView imageView, BottomNavigationItems bottomNavigationItems) {
        if (bottomNavigationItems.equals(BottomNavigationItems.TRACK)) {
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.ic_track_selected));
        } else {
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.ic_track));
        }
    }

    @BindingAdapter("iconArtist")
    public static void setIconArtist(ImageView imageView, BottomNavigationItems bottomNavigationItems) {
        if (bottomNavigationItems.equals(BottomNavigationItems.ARTIST)) {
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.ic_artist_selected));
        } else {
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.ic_artist));
        }
    }

    @BindingAdapter("iconAlbum")
    public static void setIconAlbum(ImageView imageView, BottomNavigationItems bottomNavigationItems) {
        if (bottomNavigationItems.equals(BottomNavigationItems.ALBUM)) {
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.ic_album_selected));
        } else {
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.ic_album));
        }
    }
}
