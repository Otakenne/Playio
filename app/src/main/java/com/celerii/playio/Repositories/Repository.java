package com.celerii.playio.Repositories;

import androidx.lifecycle.MutableLiveData;

import com.celerii.playio.Models.Album;
import com.celerii.playio.Models.AlbumResponse;
import com.celerii.playio.Models.Artist;
import com.celerii.playio.Models.ArtistResponse;
import com.celerii.playio.Models.Track;
import com.celerii.playio.Models.TrackResponse;
import com.celerii.playio.Network.RetrofitClient;
import com.celerii.playio.Utility.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    public MutableLiveData<List<Track>> getTracks(String limit) {
        MutableLiveData<List<Track>> tracks = new MutableLiveData<>();

        Call<TrackResponse> call = RetrofitClient
                .getInstance()
                .getMyAPIInterface()
                .getTracks(Constants.CLIENT_ID,
                        Constants.FORMAT,
                        limit,
                        Constants.IMAGE_SIZE,
                        Constants.BOOST);

        call.enqueue(new Callback<TrackResponse>() {
            @Override
            public void onResponse(Call<TrackResponse> call, Response<TrackResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TrackResponse trackResponse = response.body();
                    String status = trackResponse.getHeaders().getStatus();

                    if (status.equals("success")) {
                        tracks.setValue(trackResponse.getResults());
                    }
                }
            }

            @Override
            public void onFailure(Call<TrackResponse> call, Throwable t) {

            }
        });

        return tracks;
    }

    public MutableLiveData<List<Track>> getArtistTracks(String limit, String artistID) {
        MutableLiveData<List<Track>> tracks = new MutableLiveData<>();

        Call<TrackResponse> call = RetrofitClient
                .getInstance()
                .getMyAPIInterface()
                .getArtistTracks(Constants.CLIENT_ID,
                        Constants.FORMAT,
                        limit,
                        Constants.IMAGE_SIZE,
                        artistID);

        call.enqueue(new Callback<TrackResponse>() {
            @Override
            public void onResponse(Call<TrackResponse> call, Response<TrackResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TrackResponse trackResponse = response.body();
                    String status = trackResponse.getHeaders().getStatus();

                    if (status.equals("success")) {
                        tracks.setValue(trackResponse.getResults());
                    }
                }
            }

            @Override
            public void onFailure(Call<TrackResponse> call, Throwable t) {

            }
        });

        return tracks;
    }

    public MutableLiveData<List<Track>> getAlbumTracks(String limit, String albumID) {
        MutableLiveData<List<Track>> tracks = new MutableLiveData<>();

        Call<TrackResponse> call = RetrofitClient
                .getInstance()
                .getMyAPIInterface()
                .getArtistTracks(Constants.CLIENT_ID,
                        Constants.FORMAT,
                        limit,
                        Constants.IMAGE_SIZE,
                        albumID);

        call.enqueue(new Callback<TrackResponse>() {
            @Override
            public void onResponse(Call<TrackResponse> call, Response<TrackResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TrackResponse trackResponse = response.body();
                    String status = trackResponse.getHeaders().getStatus();

                    if (status.equals("success")) {
                        tracks.setValue(trackResponse.getResults());
                    }
                }
            }

            @Override
            public void onFailure(Call<TrackResponse> call, Throwable t) {

            }
        });

        return tracks;
    }

    public MutableLiveData<List<Artist>> getArtists(String limit) {
        MutableLiveData<List<Artist>> artists = new MutableLiveData<>();

        Call<ArtistResponse> call = RetrofitClient
                .getInstance()
                .getMyAPIInterface()
                .getArtists(Constants.CLIENT_ID,
                        Constants.FORMAT,
                        Constants.HAS_IMAGE,
                        limit,
                        Constants.ORDER);

        call.enqueue(new Callback<ArtistResponse>() {
            @Override
            public void onResponse(Call<ArtistResponse> call, Response<ArtistResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArtistResponse artistResponse = response.body();
                    String status = artistResponse.getHeaders().getStatus();

                    if (status.equals("success")) {
                        artists.setValue(artistResponse.getResults());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArtistResponse> call, Throwable t) {

            }
        });

        return artists;
    }

    public MutableLiveData<List<Album>> getAlbums(String limit) {
        MutableLiveData<List<Album>> albums = new MutableLiveData<>();

        Call<AlbumResponse> call = RetrofitClient
                .getInstance()
                .getMyAPIInterface()
                .getAlbums(Constants.CLIENT_ID,
                        Constants.FORMAT,
                        limit,
                        Constants.ORDER,
                        Constants.IMAGE_SIZE);

        call.enqueue(new Callback<AlbumResponse>() {
            @Override
            public void onResponse(Call<AlbumResponse> call, Response<AlbumResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AlbumResponse albumResponse = response.body();
                    String status = albumResponse.getHeaders().getStatus();

                    if (status.equals("success")) {
                        albums.setValue(albumResponse.getResults());
                    }
                }
            }

            @Override
            public void onFailure(Call<AlbumResponse> call, Throwable t) {

            }
        });

        return albums;
    }
}
