package com.celerii.playio.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.celerii.playio.interfaces.OnClickHandlerInterface;
import com.celerii.playio.mods.Track;
import com.celerii.playio.R;
import com.celerii.playio.databinding.HomeTrendingRowBinding;

import java.util.ArrayList;

public class HomeTrackAdapter extends RecyclerView.Adapter<HomeTrackAdapter.MyViewHolder>
    implements OnClickHandlerInterface {
    private final ArrayList<Track> tracks;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.track_art_clipper) {
            Toast.makeText(view.getContext(), "Track Clicked", Toast.LENGTH_LONG).show();
        }
    }

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
        holder.homeTrendingRowBinding.setClickHandler(this);
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }
}
