package com.celerii.playio.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.celerii.playio.Models.Track;
import com.celerii.playio.Repositories.Repository;

import java.util.List;

public class AlbumTracksViewModel extends ViewModel {
    private final Repository repository;
    private MutableLiveData<List<Track>> tracks;

    public AlbumTracksViewModel() {
        this.repository = new Repository();
    }

    public MutableLiveData<List<Track>> getAlbumTracks(String limit, String albumID) {
        if (tracks == null) {
            this.tracks = repository.getAlbumTracks(limit, albumID);
        }

        return this.tracks;
    }
}
