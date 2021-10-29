package com.celerii.playio.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.celerii.playio.mods.Album;
import com.celerii.playio.R;
import com.celerii.playio.databinding.HomeAlbumsRowBinding;

import java.util.ArrayList;

public class HomeAlbumsAdapter extends RecyclerView.Adapter<HomeAlbumsAdapter.MyViewHolder> {
    private final ArrayList<Album> albums;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public HomeAlbumsRowBinding homeAlbumsRowBinding;

        public MyViewHolder(@NonNull HomeAlbumsRowBinding homeAlbumsRowBinding) {
            super(homeAlbumsRowBinding.getRoot());

            this.homeAlbumsRowBinding = homeAlbumsRowBinding;
        }
    }

    public HomeAlbumsAdapter(ArrayList<Album> albums) {
        this.albums = albums;
    }

    @NonNull
    @Override
    public HomeAlbumsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeAlbumsRowBinding homeAlbumsRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.home_albums_row, parent, false);

        return new MyViewHolder(homeAlbumsRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAlbumsAdapter.MyViewHolder holder, int position) {
        holder.homeAlbumsRowBinding.setAlbum(albums.get(position));
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }
}
