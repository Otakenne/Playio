package com.celerii.playio.Interfaces;

import com.celerii.playio.Models.AlbumResponse;
import com.celerii.playio.Models.ArtistResponse;
import com.celerii.playio.Models.TrackResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyAPIInterface {

    @GET("tracks")
    Call<TrackResponse> getTracks(@Query("client_id") String clientID,
                                  @Query("format") String format,
                                  @Query("limit") String limit,
                                  @Query("imagesize") String imagesize,
                                  @Query("boost") String boost);

    @GET("tracks/")
    Call<TrackResponse> getArtistTracks(@Query("client_id") String clientID,
                                      @Query("format") String format,
                                      @Query("limit") String limit,
                                      @Query("imagesize") String imagesize,
                                      @Query("artist_id") String artistID);

    @GET("tracks/")
    Call<TrackResponse> getAlbumTracks(@Query("client_id") String clientID,
                                     @Query("format") String format,
                                     @Query("limit") String limit,
                                     @Query("imagesize") String imagesize,
                                     @Query("album_id") String albumID);

    @GET("artists/")
    Call<ArtistResponse> getArtists(@Query("client_id") String clientID,
                                    @Query("format") String format,
                                    @Query("hasimage") String hasImage,
                                    @Query("limit") String limit,
                                    @Query("order") String order);

    @GET("albums/")
    Call<AlbumResponse> getAlbums(@Query("client_id") String clientID,
                                  @Query("format") String format,
                                  @Query("limit") String limit,
                                  @Query("order") String order,
                                  @Query("imagesize") String imagesize);
}
