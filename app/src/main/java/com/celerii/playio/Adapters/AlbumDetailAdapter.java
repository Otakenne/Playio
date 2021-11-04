package com.celerii.playio.Adapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.celerii.playio.Activities.BaseActivity;
import com.celerii.playio.R;
import com.celerii.playio.Services.MusicService;
import com.celerii.playio.Utility.Constants;
import com.celerii.playio.databinding.AlbumDetailsHeaderBinding;
import com.celerii.playio.databinding.TrackRowBinding;
import com.celerii.playio.interfaces.OnClickHandlerInterface;
import com.celerii.playio.mods.Album;
import com.celerii.playio.mods.Track;

import java.util.ArrayList;
import java.util.Collections;

public class AlbumDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements OnClickHandlerInterface {
    private final ArrayList<Track> tracks;
    private final Album album;
    private MusicService musicService;

    private static final int header = 1;
    private static final int normal = 2;

    @Override
    public void onClick(View view, int position) {
        int viewID = view.getId();
        if (viewID == R.id.constraint_layout) {
            Context context = view.getContext();
            Intent musicIntent = new Intent(context, MusicService.class);
            ArrayList<Track> trackList = new ArrayList<>(tracks.subList(1, tracks.size()));
            Collections.rotate(trackList, trackList.size() - (position - 1));
            musicIntent.putExtra(Constants.TRACK_LIST_FOR_MUSIC_SERVICE_INTENT, trackList);
            musicIntent.putExtra(Constants.TRACK_LIST_FOR_MUSIC_SERVICE_INTENT, tracks);
            context.startService(musicIntent);
            context.bindService(musicIntent, serviceConnection, Context.BIND_AUTO_CREATE);

            BaseActivity.smartPlayControls.setLoading(true);
            Intent showSmartPlayControlsIntent = new Intent(Constants.SHOW_SMART_CONTROLS);
            showSmartPlayControlsIntent.putExtra("show_play_controls", true);
            LocalBroadcastManager.getInstance(context).sendBroadcast(showSmartPlayControlsIntent);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TrackRowBinding trackRowBinding;

        public MyViewHolder(@NonNull TrackRowBinding trackRowBinding) {
            super(trackRowBinding.getRoot());
            this.trackRowBinding = trackRowBinding;
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public AlbumDetailsHeaderBinding albumDetailsHeaderBinding;

        public HeaderViewHolder(@NonNull AlbumDetailsHeaderBinding albumDetailsHeaderBinding) {
            super(albumDetailsHeaderBinding.getRoot());
            this.albumDetailsHeaderBinding = albumDetailsHeaderBinding;
        }
    }

    public AlbumDetailAdapter(ArrayList<Track> tracks, Album album) {
        this.tracks = tracks;
        this.album = album;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == header) {
            AlbumDetailsHeaderBinding albumDetailsHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.album_details_header, parent, false);
            return new HeaderViewHolder(albumDetailsHeaderBinding);
        } else {
            TrackRowBinding trackRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.track_row, parent, false);
            return new MyViewHolder(trackRowBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).albumDetailsHeaderBinding.setAlbum(album);
        } else if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).trackRowBinding.setTrack(tracks.get(position));
            ((MyViewHolder) holder).trackRowBinding.setClickHandler(this);
            ((MyViewHolder) holder).trackRowBinding.setPosition(position);
        }
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) return header;
        return normal;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.MusicServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };
}
