package com.celerii.playio.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.celerii.playio.Models.Track;
import com.celerii.playio.Repositories.Repository;

import java.util.List;

public class TracksViewModel extends ViewModel {
    private final Repository repository;
    private MutableLiveData<List<Track>> tracks;

    public TracksViewModel() {
        this.repository = new Repository();
    }

    public MutableLiveData<List<Track>> getTracks(String limit) {
        if (this.tracks == null) {
            this.tracks = this.repository.getTracks(limit);
        }

        return this.tracks;
    }
}
