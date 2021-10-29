package com.celerii.playio.Fragments;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.celerii.playio.Adapters.HomeAlbumsAdapter;
import com.celerii.playio.Adapters.HomeArtistAdapter;
import com.celerii.playio.Adapters.HomeTrackAdapter;
import com.celerii.playio.databinding.FragmentHomeBinding;
import com.celerii.playio.mods.Album;
import com.celerii.playio.mods.Artist;
import com.celerii.playio.mods.Track;
import com.celerii.playio.R;
import com.celerii.playio.ViewModels.HomeViewModels;;

import java.util.ArrayList;

/**
 *
 */
public class HomeFragment extends Fragment {

    HomeViewModels homeViewModels;
    FragmentHomeBinding fragmentHomeBinding;

    HomeTrackAdapter homeTrackAdapter;
    HomeArtistAdapter homeArtistAdapter;
    HomeAlbumsAdapter homeAlbumsAdapter;

    ArrayList<Track> tracks;
    ArrayList<Artist> artists;
    ArrayList<Album> albums;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = fragmentHomeBinding.getRoot();

        homeViewModels = new HomeViewModels();
        String limit = "20"; //String.valueOf(Resources.getSystem().getInteger(R.integer.trending_tracks_size));

        initializeUI();

        tracks = new ArrayList<>();
        homeTrackAdapter = new HomeTrackAdapter(tracks);
        fragmentHomeBinding.tracksList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        fragmentHomeBinding.tracksList.setAdapter(homeTrackAdapter);

        artists = new ArrayList<>();
        homeArtistAdapter = new HomeArtistAdapter(artists);
        fragmentHomeBinding.artistsList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        fragmentHomeBinding.artistsList.setAdapter(homeArtistAdapter);

        albums = new ArrayList<>();
        homeAlbumsAdapter = new HomeAlbumsAdapter(albums);
        fragmentHomeBinding.albumsList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        fragmentHomeBinding.albumsList.setAdapter(homeAlbumsAdapter);

        homeViewModels.getTracks(limit).observe(getViewLifecycleOwner(), tracks -> {
            if (tracks != null && !tracks.isEmpty()) {
                this.tracks.clear();
                this.tracks.addAll(tracks);
                this.homeTrackAdapter.notifyDataSetChanged();
            }
        });

        homeViewModels.getArtists(limit).observe(getViewLifecycleOwner(), artists -> {
            if (artists != null && !artists.isEmpty()) {
                this.artists.clear();
                this.artists.addAll(artists);
                this.homeArtistAdapter.notifyDataSetChanged();
            }
        });

        homeViewModels.getAlbums(limit).observe(getViewLifecycleOwner(), albums -> {
            if (albums != null && !albums.isEmpty()) {
                this.albums.clear();
                this.albums.addAll(albums);
                this.homeAlbumsAdapter.notifyDataSetChanged();

                fragmentHomeBinding.progressBar.setVisibility(View.GONE);
                fragmentHomeBinding.errorLayout.setVisibility(View.GONE);
                fragmentHomeBinding.scrollView.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void initializeUI() {
        fragmentHomeBinding.progressBar.setVisibility(View.VISIBLE);
        fragmentHomeBinding.errorLayout.setVisibility(View.GONE);
        fragmentHomeBinding.scrollView.setVisibility(View.GONE);
    }
}