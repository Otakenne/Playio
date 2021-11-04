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
import com.celerii.playio.databinding.HomeTrendingRowBinding;
import com.celerii.playio.interfaces.OnClickHandlerInterface;
import com.celerii.playio.mods.SmartPlayControls;
import com.celerii.playio.mods.Track;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeTrackAdapter extends RecyclerView.Adapter<HomeTrackAdapter.MyViewHolder>
    implements OnClickHandlerInterface {
    private final ArrayList<Track> tracks;
    private MusicService musicService;

    @Override
    public void onClick(View view, int position) {
        if (view.getId() == R.id.track_art_clipper) {
            Context context = view.getContext();
            Intent musicIntent = new Intent(context, MusicService.class);
            ArrayList<Track> trackList = new ArrayList<>();
            trackList.add(tracks.get(position));
            musicIntent.putExtra(Constants.TRACK_LIST_FOR_MUSIC_SERVICE_INTENT, (Serializable) trackList);
            context.startService(musicIntent);
            context.bindService(musicIntent, serviceConnection, Context.BIND_AUTO_CREATE);

            BaseActivity.smartPlayControls.setLoading(true);
            Intent showSmartPlayControlsIntent = new Intent(Constants.SHOW_SMART_CONTROLS);
            showSmartPlayControlsIntent.putExtra("show_play_controls", true);
            LocalBroadcastManager.getInstance(context).sendBroadcast(showSmartPlayControlsIntent);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        HomeTrendingRowBinding homeTrendingRowBinding;

        public MyViewHolder(@NonNull HomeTrendingRowBinding homeTrendingRowBinding) {
            super(homeTrendingRowBinding.getRoot());

            this.homeTrendingRowBinding = homeTrendingRowBinding;
        }
    }

    public HomeTrackAdapter(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeTrendingRowBinding homeTrendingRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.home_trending_row, parent, false);
        return new MyViewHolder(homeTrendingRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeTrackAdapter.MyViewHolder holder, int position) {
        holder.homeTrendingRowBinding.setTrack(tracks.get(position));
        holder.homeTrendingRowBinding.setClickHandler(this);
        holder.homeTrendingRowBinding.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return tracks.size();
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
