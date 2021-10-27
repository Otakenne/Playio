package com.celerii.playio.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.celerii.playio.Models.Album;
import com.celerii.playio.Models.Artist;
import com.celerii.playio.Models.Track;
import com.celerii.playio.Repositories.Repository;

import java.util.List;

public class HomeViewModels extends ViewModel {
    private final Repository repository;
    private MutableLiveData<List<Track>> tracks;
    private MutableLiveData<List<Artist>> artists;
    private MutableLiveData<List<Album>> albums;

    public HomeViewModels() {
        this.repository = new Repository();
    }

    public MutableLiveData<List<Track>> getTracks(String limit) {
        if (this.tracks == null) {
            this.tracks = this.repository.getTracks(limit);
        }

        return this.tracks;
    }

    public MutableLiveData<List<Artist>> getArtists(String limit) {
        if (this.artists == null) {
            this.artists = this.repository.getArtists(limit);
        }

        return this.artists;
    }

    public MutableLiveData<List<Album>> getAlbums(String limit) {
        if (this.albums == null) {
            this.albums = this.repository.getAlbums(limit);
        }

        return this.albums;
    }
}
