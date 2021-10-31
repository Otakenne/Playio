package com.celerii.playio.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.celerii.playio.Adapters.TracksAdapter;
import com.celerii.playio.R;
import com.celerii.playio.Utility.Constants;
import com.celerii.playio.ViewModels.TracksViewModel;
import com.celerii.playio.databinding.FragmentTracksBinding;
import com.celerii.playio.mods.Track;
import com.celerii.playio.mods.TrackDetailsHeader;

import java.util.ArrayList;

/**
 *
 */
public class TracksFragment extends Fragment {

    FragmentTracksBinding fragmentTracksBinding;
    TracksAdapter tracksAdapter;

    ArrayList<Track> tracks;
    TrackDetailsHeader tracksDetailsHeader;

    public TracksFragment() {
        // Required empty public constructor
    }

    public static TracksFragment newInstance() {
        return new TracksFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentTracksBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tracks, container, false);
        View view = fragmentTracksBinding.getRoot();


        initializeUI();
        TracksViewModel tracksViewModel = new TracksViewModel();
        tracksViewModel.getTracks(Constants.TRACK_TOP_SONG_COUNT).observe(getViewLifecycleOwner(), trackList -> {
            if (trackList != null && !trackList.isEmpty()) {
                tracks.clear();
                tracks.addAll(trackList);
                tracksAdapter.notifyDataSetChanged();

                fragmentTracksBinding.errorLayout.setVisibility(View.GONE);
                fragmentTracksBinding.progressBar.setVisibility(View.GONE);
                fragmentTracksBinding.trackList.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void initializeUI() {
        fragmentTracksBinding.errorLayout.setVisibility(View.GONE);
        fragmentTracksBinding.progressBar.setVisibility(View.VISIBLE);
        fragmentTracksBinding.trackList.setVisibility(View.GONE);

        tracks = new ArrayList<>();
        tracksDetailsHeader = new TrackDetailsHeader(Constants.TRACK_DETAILS_HEADER_TEXT, Constants.TRACK_DETAILS_HEADER_IMAGE_URL);
        tracksAdapter = new TracksAdapter(tracks, tracksDetailsHeader);
        fragmentTracksBinding.trackList.setAdapter(tracksAdapter);
        fragmentTracksBinding.trackList.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}