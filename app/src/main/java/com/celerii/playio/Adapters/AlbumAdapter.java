package com.celerii.playio.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.celerii.playio.R;
import com.celerii.playio.databinding.AlbumRowBinding;
import com.celerii.playio.databinding.AlbumsFragmentHeaderBinding;
import com.celerii.playio.databinding.ArtistRowBinding;
import com.celerii.playio.databinding.ArtistsFragmentHeaderBinding;
import com.celerii.playio.mods.Album;
import com.celerii.playio.mods.Artist;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<Album> albums;
    private static final int header = 1;
    private static final int normal = 2;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public AlbumRowBinding albumRowBinding;

        public MyViewHolder(@NonNull AlbumRowBinding albumRowBinding) {
            super(albumRowBinding.getRoot());

            this.albumRowBinding = albumRowBinding;
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public AlbumsFragmentHeaderBinding albumsFragmentHeaderBinding;

        public HeaderViewHolder(@NonNull AlbumsFragmentHeaderBinding albumsFragmentHeaderBinding) {
            super(albumsFragmentHeaderBinding.getRoot());

            this.albumsFragmentHeaderBinding = albumsFragmentHeaderBinding;
        }
    }

    public AlbumAdapter(ArrayList<Album> albums) {
        this.albums = albums;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == header) {
            AlbumsFragmentHeaderBinding albumsFragmentHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.albums_fragment_header, parent, false);
            return new HeaderViewHolder(albumsFragmentHeaderBinding);
        } else {
            AlbumRowBinding albumRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.album_row, parent, false);
            return new MyViewHolder(albumRowBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).albumRowBinding.setAlbum(albums.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return albums.size();
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
