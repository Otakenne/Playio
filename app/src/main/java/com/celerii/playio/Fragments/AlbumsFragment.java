package com.celerii.playio.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.celerii.playio.Adapters.AlbumAdapter;
import com.celerii.playio.R;
import com.celerii.playio.Utility.Constants;
import com.celerii.playio.ViewModels.AlbumViewModel;
import com.celerii.playio.databinding.FragmentAlbumsBinding;
import com.celerii.playio.mods.Album;

import java.util.ArrayList;

/**
 *
 */
public class AlbumsFragment extends Fragment {

    FragmentAlbumsBinding fragmentAlbumsBinding;
    AlbumAdapter albumAdapter;

    ArrayList<Album> albums;

    public AlbumsFragment() {
        // Required empty public constructor
    }

    public static AlbumsFragment newInstance() {
        return new AlbumsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAlbumsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_albums, container, false);
        View view = fragmentAlbumsBinding.getRoot();

        initializeUI();
        AlbumViewModel albumViewModel = new AlbumViewModel();
        albumViewModel.getAlbums(Constants.TRACK_TOP_SONG_COUNT).observe(getViewLifecycleOwner(), albumList -> {
            if (albumList != null && !albumList.isEmpty()) {
                albums.clear();
                albums.addAll(albumList);
                albumAdapter.notifyDataSetChanged();

                fragmentAlbumsBinding.errorLayout.setVisibility(View.GONE);
                fragmentAlbumsBinding.progressBar.setVisibility(View.GONE);
                fragmentAlbumsBinding.albumsList.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void initializeUI() {
        fragmentAlbumsBinding.errorLayout.setVisibility(View.GONE);
        fragmentAlbumsBinding.progressBar.setVisibility(View.VISIBLE);
        fragmentAlbumsBinding.albumsList.setVisibility(View.GONE);

        albums = new ArrayList<>();
        albumAdapter = new AlbumAdapter(albums);
        fragmentAlbumsBinding.albumsList.setAdapter(albumAdapter);
        fragmentAlbumsBinding.albumsList.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}