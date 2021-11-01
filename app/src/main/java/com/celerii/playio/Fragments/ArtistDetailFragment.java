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

import com.celerii.playio.Adapters.ArtistDetailAdapter;
import com.celerii.playio.Enums.BottomNavigationItems;
import com.celerii.playio.R;
import com.celerii.playio.Utility.Constants;
import com.celerii.playio.ViewModels.ArtistTracksViewModel;
import com.celerii.playio.ViewModels.TracksViewModel;
import com.celerii.playio.databinding.FragmentArtistDetailBinding;
import com.celerii.playio.mods.Artist;
import com.celerii.playio.mods.Track;

import java.util.ArrayList;

/**
 *
 */
public class ArtistDetailFragment extends Fragment {

    private FragmentArtistDetailBinding fragmentArtistDetailBinding;
    private ArtistDetailAdapter artistDetailAdapter;

    private ArrayList<Track> tracks;
    public static Artist artist;

    public ArtistDetailFragment() {
        // Required empty public constructor
    }

    public static ArtistDetailFragment newInstance(Artist artistParameter) {
        ArtistDetailFragment artistDetailFragment = new ArtistDetailFragment();
        artist = artistParameter;
        return artistDetailFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentArtistDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist_detail, container, false);
        View view = fragmentArtistDetailBinding.getRoot();


        initializeUI();
        ArtistTracksViewModel artistTracksViewModel = new ArtistTracksViewModel();
        artistTracksViewModel.getArtistTracks(Constants.TRACK_TOP_SONG_COUNT, artist.getId()).observe(getViewLifecycleOwner(), trackList -> {
            if (trackList != null && !trackList.isEmpty()) {
                tracks.clear();
                tracks.addAll(trackList);
                tracks.add(0, new Track());
                artistDetailAdapter.notifyDataSetChanged();

                fragmentArtistDetailBinding.errorLayout.setVisibility(View.GONE);
                fragmentArtistDetailBinding.progressBar.setVisibility(View.GONE);
                fragmentArtistDetailBinding.trackList.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void initializeUI() {
        fragmentArtistDetailBinding.errorLayout.setVisibility(View.GONE);
        fragmentArtistDetailBinding.progressBar.setVisibility(View.VISIBLE);
        fragmentArtistDetailBinding.trackList.setVisibility(View.GONE);

        tracks = new ArrayList<>();
        artistDetailAdapter = new ArtistDetailAdapter(tracks, artist);
        fragmentArtistDetailBinding.trackList.setAdapter(artistDetailAdapter);
        fragmentArtistDetailBinding.trackList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroy() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Constants.SHARED_PREFERENCES_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(Constants.HOME_ARTIST_DETAILS_FRAGMENT_VISIBLE, false);
        editor.putBoolean(Constants.ARTIST_DETAILS_FRAGMENT_VISIBLE, false);
        editor.apply();

        super.onDestroy();
    }
}