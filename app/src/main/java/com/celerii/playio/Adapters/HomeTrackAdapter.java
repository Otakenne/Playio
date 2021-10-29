package com.celerii.playio.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.celerii.playio.mods.Track;
import com.celerii.playio.R;
import com.celerii.playio.databinding.HomeTrendingRowBinding;

import java.util.ArrayList;

public class HomeTrackAdapter extends RecyclerView.Adapter<HomeTrackAdapter.MyViewHolder> {
    private final ArrayList<Track> tracks;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        HomeTrendingRowBinding homeTrendingRowBinding;

        public MyViewHolder(@NonNull HomeTrendingRowBinding homeTrendingRowBinding) {
            super(homeTrendingRowBinding.getRoot());

            this.homeTrendingRowBinding = homeTrendingRowBinding;
        }
    }

    public HomeTrackAdapter(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeTrendingRowBinding homeTrendingRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.home_trending_row, parent, false);
        return new MyViewHolder(homeTrendingRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeTrackAdapter.MyViewHolder holder, int position) {
        holder.homeTrendingRowBinding.setTrack(tracks.get(position));
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }
}
