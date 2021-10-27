package com.celerii.playio.ViewModels;

import androidx.lifecycle.MutableLiveData;

import com.celerii.playio.Models.Track;
import com.celerii.playio.Repositories.Repository;

import java.util.List;

public class ArtistTracksViewModel {
    private final Repository repository;
    private MutableLiveData<List<Track>> tracks;

    public ArtistTracksViewModel() {
        this.repository = new Repository();
    }

    public MutableLiveData<List<Track>> getArtistTracks(String limit, String artistID) {
        if (tracks == null) {
            this.tracks = repository.getArtistTracks(limit, artistID);
        }

        return this.tracks;
    }
}
