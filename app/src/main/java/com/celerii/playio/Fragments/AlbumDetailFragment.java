package com.celerii.playio.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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

        tracks = new ArrayList<>();
        albumDetailAdapter = new AlbumDetailAdapter(tracks, album);
        fragmentAlbumDetailBinding.trackList.setAdapter(albumDetailAdapter);
        fragmentAlbumDetailBinding.trackList.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeUI();
        AlbumTracksViewModel albumTracksViewModel = new ViewModelProvider(this).get(AlbumTracksViewModel.class);
        albumTracksViewModel.getAlbumTracks(Constants.TRACK_TOP_SONG_COUNT, album.getId()).observe(getViewLifecycleOwner(), trackList -> {
            fragmentAlbumDetailBinding.setIsLoading(false);
            if (trackList != null && !trackList.isEmpty()) {
                tracks.clear();
                tracks.addAll(trackList);
                tracks.add(0, new Track());
                albumDetailAdapter.notifyDataSetChanged();

                fragmentAlbumDetailBinding.setIsError(false);
            } else {
                fragmentAlbumDetailBinding.setIsError(true);
            }
        });
    }

    private void initializeUI() {
        fragmentAlbumDetailBinding.setIsError(false);
        fragmentAlbumDetailBinding.setIsLoading(true);
    }

    @Override
    public void onDestroyView() {
        fragmentAlbumDetailBinding = null;
        albumDetailAdapter = null;
        super.onDestroyView();
    }

    @BindingAdapter("visibility")
    public static void setVisibility(View view, boolean isVisible) {
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }
}