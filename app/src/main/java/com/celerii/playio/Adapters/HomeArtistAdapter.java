package com.celerii.playio.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.celerii.playio.mods.Artist;
import com.celerii.playio.R;
import com.celerii.playio.databinding.HomeArtistsRowBinding;

import java.util.ArrayList;

public class HomeArtistAdapter extends RecyclerView.Adapter<HomeArtistAdapter.MyViewHolder> {
    private final ArrayList<Artist> artists;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        HomeArtistsRowBinding homeArtistsRowBinding;

        public MyViewHolder(@NonNull HomeArtistsRowBinding homeArtistsRowBinding) {
            super(homeArtistsRowBinding.getRoot());

            this.homeArtistsRowBinding = homeArtistsRowBinding;
        }
    }

    public HomeArtistAdapter(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeArtistsRowBinding homeArtistsRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.home_artists_row, parent, false);
        return new MyViewHolder(homeArtistsRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.homeArtistsRowBinding.setArtist(artists.get(position));
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }
}
