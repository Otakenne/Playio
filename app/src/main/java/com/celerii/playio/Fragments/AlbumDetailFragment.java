package com.celerii.playio.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.celerii.playio.Adapters.AlbumDetailAdapter;
import com.celerii.playio.R;
import com.celerii.playio.Utility.Constants;
import com.celerii.playio.ViewModels.AlbumTracksViewModel;
import com.celerii.playio.databinding.FragmentAlbumDetailBinding;
import com.celerii.playio.mods.Album;
import com.celerii.playio.mods.Track;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 */
public class AlbumDetailFragment extends Fragment {

    private FragmentAlbumDetailBinding fragmentAlbumDetailBinding;
    private AlbumDetailAdapter albumDetailAdapter;

    private ArrayList<Track> tracks;
    public static Album album;

    public AlbumDetailFragment() {
        // Required empty public constructor
    }

    public static AlbumDetailFragment newInstance(Album albumParameter) {
        AlbumDetailFragment albumDetailFragment = new AlbumDetailFragment();
        album = albumParameter;
        return albumDetailFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAlbumDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_album_detail, container, false);
        View view = fragmentAlbumDetailBinding.getRoot();


        initializeUI();
        AlbumTracksViewModel albumTracksViewModel = new AlbumTracksViewModel();
        albumTracksViewModel.getAlbumTracks(Constants.TRACK_TOP_SONG_COUNT, album.getId()).observe(getViewLifecycleOwner(), trackList -> {
            if (trackList != null && !trackList.isEmpty()) {
                tracks.clear();
                tracks.addAll(trackList);
                tracks.add(0, new Track());
                albumDetailAdapter.notifyDataSetChanged();

                fragmentAlbumDetailBinding.errorLayout.setVisibility(View.GONE);
                fragmentAlbumDetailBinding.progressBar.setVisibility(View.GONE);
                fragmentAlbumDetailBinding.trackList.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void initializeUI() {
        fragmentAlbumDetailBinding.errorLayout.setVisibility(View.GONE);
        fragmentAlbumDetailBinding.progressBar.setVisibility(View.VISIBLE);
        fragmentAlbumDetailBinding.trackList.setVisibility(View.GONE);

        tracks = new ArrayList<>();
        albumDetailAdapter = new AlbumDetailAdapter(tracks, album);
        fragmentAlbumDetailBinding.trackList.setAdapter(albumDetailAdapter);
        fragmentAlbumDetailBinding.trackList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}