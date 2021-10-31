package com.celerii.playio.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.celerii.playio.R;
import com.celerii.playio.databinding.AlbumDetailsHeaderBinding;
import com.celerii.playio.databinding.TrackRowBinding;
import com.celerii.playio.mods.Album;
import com.celerii.playio.mods.Track;

import java.util.ArrayList;

public class AlbumDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<Track> tracks;
    private final Album album;

    private static final int header = 1;
    private static final int normal = 2;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TrackRowBinding trackRowBinding;

        public MyViewHolder(@NonNull TrackRowBinding trackRowBinding) {
            super(trackRowBinding.getRoot());
            this.trackRowBinding = trackRowBinding;
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public AlbumDetailsHeaderBinding albumDetailsHeaderBinding;

        public HeaderViewHolder(@NonNull AlbumDetailsHeaderBinding albumDetailsHeaderBinding) {
            super(albumDetailsHeaderBinding.getRoot());
            this.albumDetailsHeaderBinding = albumDetailsHeaderBinding;
        }
    }

    public AlbumDetailAdapter(ArrayList<Track> tracks, Album album) {
        this.tracks = tracks;
        this.album = album;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == header) {
            AlbumDetailsHeaderBinding albumDetailsHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.album_details_header, parent, false);
            return new HeaderViewHolder(albumDetailsHeaderBinding);
        } else {
            TrackRowBinding trackRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.track_row, parent, false);
            return new MyViewHolder(trackRowBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).albumDetailsHeaderBinding.setAlbum(album);
        } else if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).trackRowBinding.setTrack(tracks.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) return header;
        return normal;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
