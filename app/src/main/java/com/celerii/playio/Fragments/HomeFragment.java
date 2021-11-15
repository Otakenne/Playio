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
import androidx.recyclerview.widget.RecyclerView;

import com.celerii.playio.Adapters.HomeAlbumsAdapter;
import com.celerii.playio.Adapters.HomeArtistAdapter;
import com.celerii.playio.Adapters.HomeTrackAdapter;
import com.celerii.playio.R;
import com.celerii.playio.ViewModels.HomeViewModels;
import com.celerii.playio.databinding.FragmentHomeBinding;
import com.celerii.playio.mods.Album;
import com.celerii.playio.mods.Artist;
import com.celerii.playio.mods.Track;

import java.util.ArrayList;

/**
 *
 */
public class HomeFragment extends Fragment  {

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = fragmentHomeBinding.getRoot();

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeUI();

        homeViewModels = new ViewModelProvider(this).get(HomeViewModels.class);
        String limit = "20";
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

                fragmentHomeBinding.setIsError(false);
                fragmentHomeBinding.setIsLoading(false);
            }
        });
    }

    private void initializeUI() {
        fragmentHomeBinding.setIsError(false);
        fragmentHomeBinding.setIsLoading(true);
    }

    @Override
    public void onDestroyView() {
        fragmentHomeBinding = null;
        homeTrackAdapter = null;
        homeArtistAdapter = null;
        homeAlbumsAdapter = null;
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