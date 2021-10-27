package com.celerii.playio.Repositories;

import androidx.lifecycle.MutableLiveData;

import com.celerii.playio.Models.Album;
import com.celerii.playio.Models.AlbumResponse;
import com.celerii.playio.Network.RetrofitClient;
import com.celerii.playio.Utility.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumFragmentRepository {

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
