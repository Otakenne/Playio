package com.celerii.playio.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.celerii.playio.Models.Album;
import com.celerii.playio.Models.Artist;
import com.celerii.playio.Repositories.Repository;

import java.util.List;

public class AlbumViewModel extends ViewModel {
    private final Repository repository;
    private MutableLiveData<List<Album>> album;

    public AlbumViewModel() {
        this.repository = new Repository();
    }

    public MutableLiveData<List<Album>> getAlbums(String limit) {
        if (this.album == null) {
            this.album = this.repository.getAlbums(limit);
        }

        return this.album;
    }
}
