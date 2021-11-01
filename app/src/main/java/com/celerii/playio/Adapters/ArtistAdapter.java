package com.celerii.playio.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.celerii.playio.Activities.BaseActivity;
import com.celerii.playio.Fragments.ArtistDetailFragment;
import com.celerii.playio.Fragments.ArtistsFragment;
import com.celerii.playio.R;
import com.celerii.playio.Utility.Constants;
import com.celerii.playio.databinding.ArtistRowBinding;
import com.celerii.playio.databinding.ArtistsFragmentHeaderBinding;
import com.celerii.playio.interfaces.OnClickHandlerInterface;
import com.celerii.playio.mods.Artist;

import java.util.ArrayList;
import java.util.Objects;

public class ArtistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements OnClickHandlerInterface {
    private final ArrayList<Artist> artists;
    private static final int header = 1;
    private static final int normal = 2;

    @Override
    public void onClick(View view, int position) {
        if (view.getId() == R.id.constraint_layout) {
            Context context = view.getContext();
            FragmentManager fragmentManager = ((BaseActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ArtistDetailFragment artistDetailFragment = ArtistDetailFragment.newInstance(artists.get(position));
            ArtistsFragment artistFragment = (ArtistsFragment) fragmentManager.findFragmentByTag(Constants.ARTISTS_FRAGMENT_TAG);
            fragmentTransaction.add(R.id.fragment_container, artistDetailFragment, Constants.ARTISTS_DETAIL_FRAGMENT_TAG).addToBackStack(null);
            assert artistFragment != null;
            fragmentTransaction.hide(artistFragment).show(artistDetailFragment).commit();
            if (((AppCompatActivity) view.getContext()).getSupportActionBar() != null) {
                Objects.requireNonNull(((AppCompatActivity) view.getContext()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                Objects.requireNonNull(((AppCompatActivity) view.getContext()).getSupportActionBar()).setHomeButtonEnabled(true);
            }
            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Constants.SHARED_PREFERENCES_MODE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.ARTIST_DETAILS_FRAGMENT_VISIBLE, true);
            editor.apply();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ArtistRowBinding artistRowBinding;

        public MyViewHolder(@NonNull ArtistRowBinding artistRowBinding) {
            super(artistRowBinding.getRoot());

            this.artistRowBinding = artistRowBinding;
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public ArtistsFragmentHeaderBinding artistsFragmentHeaderBinding;

        public HeaderViewHolder(@NonNull ArtistsFragmentHeaderBinding artistsFragmentHeaderBinding) {
            super(artistsFragmentHeaderBinding.getRoot());

            this.artistsFragmentHeaderBinding = artistsFragmentHeaderBinding;
        }
    }

    public ArtistAdapter(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == header) {
            ArtistsFragmentHeaderBinding artistsFragmentHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.artists_fragment_header, parent, false);
            return new HeaderViewHolder(artistsFragmentHeaderBinding);
        } else {
            ArtistRowBinding artistRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.artist_row, parent, false);
            return new MyViewHolder(artistRowBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).artistRowBinding.setArtist(artists.get(position));
            ((MyViewHolder) holder).artistRowBinding.setClickHandler(this);
            ((MyViewHolder) holder).artistRowBinding.setPosition(position);
        }
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) return header;
        return normal;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
