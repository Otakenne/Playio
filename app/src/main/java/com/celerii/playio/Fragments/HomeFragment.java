package com.celerii.playio.Fragments;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.celerii.playio.Models.Track;
import com.celerii.playio.R;
import com.celerii.playio.ViewModels.HomeViewModels;

import java.util.List;

/**
 *
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        String limit = String.valueOf(Resources.getSystem().getInteger(R.integer.trending_tracks_size));

        HomeViewModels homeViewModels = new HomeViewModels();
        homeViewModels.getTracks(limit).observe(getViewLifecycleOwner(), new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> tracks) {
                if (tracks != null && !tracks.isEmpty()) {

                }
            }
        });

        return view;
    }
}