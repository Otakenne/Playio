package com.celerii.playio.Fragments;

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

        tracks = new ArrayList<>();
        tracksDetailsHeader = new TrackDetailsHeader(Constants.TRACK_DETAILS_HEADER_TEXT, Constants.TRACK_DETAILS_HEADER_IMAGE_URL);
        tracksAdapter = new TracksAdapter(tracks, tracksDetailsHeader);
        fragmentTracksBinding.trackList.setAdapter(tracksAdapter);
        fragmentTracksBinding.trackList.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeUI();
        TracksViewModel tracksViewModel = new ViewModelProvider(this).get(TracksViewModel.class);
        tracksViewModel.getTracks(Constants.TRACK_TOP_SONG_COUNT).observe(getViewLifecycleOwner(), trackList -> {
            fragmentTracksBinding.setIsLoading(false);
            if (trackList != null && !trackList.isEmpty()) {
                tracks.clear();
                tracks.addAll(trackList);
                tracksAdapter.notifyDataSetChanged();

                fragmentTracksBinding.setIsError(false);
            } else {
                fragmentTracksBinding.setIsError(true);
            }
        });
    }

    private void initializeUI() {
        fragmentTracksBinding.setIsError(false);
        fragmentTracksBinding.setIsLoading(true);
    }

    @Override
    public void onDestroyView() {
        fragmentTracksBinding = null;
        tracksAdapter = null;
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