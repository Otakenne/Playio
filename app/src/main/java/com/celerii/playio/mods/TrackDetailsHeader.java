package com.celerii.playio.mods;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class TrackDetailsHeader {
    String text, imageURL;

    public TrackDetailsHeader(String text, String imageURL) {
        this.text = text;
        this.imageURL = imageURL;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @BindingAdapter("image")
    public static void loadImage(ImageView imageView, String imageURL) {
        if (imageURL != null) {
            Glide.with(imageView.getContext())
                    .load(imageURL)
                    .into(imageView);
        }
    }
}
