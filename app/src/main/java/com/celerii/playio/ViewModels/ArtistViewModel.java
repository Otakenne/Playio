package com.celerii.playio.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.celerii.playio.mods.Artist;
import com.celerii.playio.Repositories.Repository;

import java.util.List;

public class ArtistViewModel extends ViewModel {
    private final Repository repository;
    private MutableLiveData<List<Artist>> artist;

    public ArtistViewModel() {
        this.repository = new Repository();
    }

    public MutableLiveData<List<Artist>> getArtists(String limit) {
        if (this.artist == null) {
            this.artist = this.repository.getArtists(limit);
        }

        return this.artist;
    }
}
