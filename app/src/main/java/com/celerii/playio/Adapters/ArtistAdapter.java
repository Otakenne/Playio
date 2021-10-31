package com.celerii.playio.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.celerii.playio.R;
import com.celerii.playio.databinding.ArtistRowBinding;
import com.celerii.playio.databinding.ArtistsFragmentHeaderBinding;
import com.celerii.playio.mods.Artist;

import java.util.ArrayList;

public class ArtistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<Artist> artists;
    private static final int header = 1;
    private static final int normal = 2;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ArtistRowBinding artistRowBinding;

        public MyViewHolder(@NonNull ArtistRowBinding artistRowBinding) {
            super(artistRowBinding.getRoot());

            this.artistRowBinding = artistRowBinding;
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public ArtistsFragmentHeaderBinding artistsFragmentHeaderBinding;

        public HeaderViewHolder(@NonNull ArtistsFragmentHeaderBinding artistsFragmentHeaderBinding) {
            super(artistsFragmentHeaderBinding.getRoot());

            this.artistsFragmentHeaderBinding = artistsFragmentHeaderBinding;
        }
    }

    public ArtistAdapter(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == header) {
            ArtistsFragmentHeaderBinding artistsFragmentHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.artists_fragment_header, parent, false);
            return new HeaderViewHolder(artistsFragmentHeaderBinding);
        } else {
            ArtistRowBinding artistRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.artist_row, parent, false);
            return new MyViewHolder(artistRowBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).artistRowBinding.setArtist(artists.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return artists.size();
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
