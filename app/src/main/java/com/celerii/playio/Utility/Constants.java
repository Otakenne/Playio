package com.celerii.playio.Utility;

import com.celerii.playio.BuildConfig;

public class Constants {
    public static final String BASE_URL = "https://api.jamendo.com/v3.0/";
    public static final String CLIENT_ID = "9d795cbb";
    public static final String CLIENT_SECRET = "16b36137d35391bca050e8a1fcc6aa28";
    public static final String FORMAT = "jsonpretty";
    public static final String IMAGE_SIZE = "600";
    public static final String HAS_IMAGE = "true";
    public static final String BOOST = "popularity_week";
    public static final String ORDER = "popularity_week";

    public static final String TRACK_TOP_SONG_COUNT = "40";
    public static final String TRACK_DETAILS_HEADER_TEXT = "Top 40 Songs for the Week";
    public static final String TRACK_DETAILS_HEADER_IMAGE_URL = "https://static.radio.net/images/broadcasts/ea/8f/1380/2/c175.png";

    public static final String HOME_FRAGMENT_TAG = "home";
    public static final String TRACKS_FRAGMENT_TAG = "tracks";
    public static final String ARTISTS_FRAGMENT_TAG = "artists";
    public static final String ALBUMS_FRAGMENT_TAG = "albums";
    public static final String ARTISTS_DETAIL_FRAGMENT_TAG = "artists_detail";
    public static final String ALBUMS_DETAIL_FRAGMENT_TAG = "albums_detail";

    public static final String HOME_ARTIST_DETAILS_FRAGMENT_VISIBLE = "home_artist_details_fragment_tag";
    public static final String HOME_ALBUM_DETAILS_FRAGMENT_VISIBLE = "home_album_details_fragment_tag";
    public static final String ARTIST_DETAILS_FRAGMENT_VISIBLE = "artist_details_fragment_tag";
    public static final String ALBUM_DETAILS_FRAGMENT_VISIBLE = "album_details_fragment_tag";

    public static final String SHARED_PREFERENCES_KEY = "play_io";
    public static final int SHARED_PREFERENCES_MODE = 0;

    public static final String MUSIC_PLAYER_ON_SET_INFO_LISTENER_BROADCAST_INTENT_FILTER = BuildConfig.APPLICATION_ID + ".MUSIC_PLAYER_ON_SET_INFO_LISTENER_BROADCAST_INTENT_FILTER";
    public static final String MUSIC_PLAYER_ON_PREPARED_LISTENER_BROADCAST_INTENT_FILTER = BuildConfig.APPLICATION_ID + ".MUSIC_PLAYER_ON_PREPARED_LISTENER_BROADCAST_INTENT_FILTER";
    public static final String MUSIC_PLAYER_ON_COMPLETION_LISTENER_BROADCAST_INTENT_FILTER = BuildConfig.APPLICATION_ID + ".MUSIC_PLAYER_ON_COMPLETION_LISTENER_BROADCAST_INTENT_FILTER";
    public static final String MUSIC_PLAYER_ON_ERROR_LISTENER_BROADCAST_INTENT_FILTER = BuildConfig.APPLICATION_ID + ".MUSIC_PLAYER_ON_ERROR_LISTENER_BROADCAST_INTENT_FILTER";

    public static final String TRACK_LIST_FOR_MUSIC_SERVICE_INTENT = "track_list";
    public static final String TRACK_URL_FOR_MUSIC_SERVICE_INTENT = "track_url";
    public static final String TRACK_NAME_FOR_MUSIC_SERVICE_INTENT = "track_name";
    public static final String TRACK_ARTIST_FOR_MUSIC_SERVICE_INTENT = "track_artist";
    public static final String TRACK_IMAGE_URL_FOR_MUSIC_SERVICE_INTENT = "track_image_url";

    public static final String SHOW_SMART_CONTROLS = "show_smart_controls";
}
