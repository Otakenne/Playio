package com.celerii.playio.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.celerii.playio.Adapters.ArtistAdapter;
import com.celerii.playio.R;
import com.celerii.playio.Utility.Constants;
import com.celerii.playio.ViewModels.ArtistViewModel;
import com.celerii.playio.databinding.FragmentArtistsBinding;
import com.celerii.playio.mods.Artist;

import java.util.ArrayList;

/**
 *
 */
public class ArtistsFragment extends Fragment {

    FragmentArtistsBinding fragmentArtistsBinding;
    ArtistAdapter artistAdapter;

    ArrayList<Artist> artists;

    public ArtistsFragment() {
        // Required empty public constructor
    }

    public static ArtistsFragment newInstance() {
        return new ArtistsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentArtistsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_artists, container, false);
        View view = fragmentArtistsBinding.getRoot();

        initializeUI();
        ArtistViewModel artistViewModel = new ArtistViewModel();
        artistViewModel.getArtists(Constants.TRACK_TOP_SONG_COUNT).observe(getViewLifecycleOwner(), artistList -> {
            if (artistList != null && !artistList.isEmpty()) {
                artists.clear();
                artists.addAll(artistList);
                artistAdapter.notifyDataSetChanged();

                fragmentArtistsBinding.errorLayout.setVisibility(View.GONE);
                fragmentArtistsBinding.progressBar.setVisibility(View.GONE);
                fragmentArtistsBinding.artistList.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void initializeUI() {
        fragmentArtistsBinding.errorLayout.setVisibility(View.GONE);
        fragmentArtistsBinding.progressBar.setVisibility(View.VISIBLE);
        fragmentArtistsBinding.artistList.setVisibility(View.GONE);

        artists = new ArrayList<>();
        artistAdapter = new ArtistAdapter(artists);
        fragmentArtistsBinding.artistList.setAdapter(artistAdapter);
        fragmentArtistsBinding.artistList.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}