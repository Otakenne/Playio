package com.celerii.playio.Repositories;

import androidx.lifecycle.MutableLiveData;

import com.celerii.playio.mods.Artist;
import com.celerii.playio.mods.ArtistResponse;
import com.celerii.playio.Network.RetrofitClient;
import com.celerii.playio.Utility.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistFragmentRepository {
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
}
