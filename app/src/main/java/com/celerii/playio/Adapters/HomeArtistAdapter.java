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
import com.celerii.playio.Fragments.HomeFragment;
import com.celerii.playio.R;
import com.celerii.playio.Utility.Constants;
import com.celerii.playio.databinding.HomeArtistsRowBinding;
import com.celerii.playio.interfaces.OnClickHandlerInterface;
import com.celerii.playio.mods.Artist;

import java.util.ArrayList;
import java.util.Objects;

public class HomeArtistAdapter extends RecyclerView.Adapter<HomeArtistAdapter.MyViewHolder>
    implements OnClickHandlerInterface {
    private final ArrayList<Artist> artists;

    @Override
    public void onClick(View view, int position) {
        if (view.getId() == R.id.constraint_layout) {
            Context context = view.getContext();
            FragmentManager fragmentManager = ((BaseActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ArtistDetailFragment artistDetailFragment = ArtistDetailFragment.newInstance(artists.get(position));
            HomeFragment homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(Constants.HOME_FRAGMENT_TAG);
            fragmentTransaction.add(R.id.fragment_container, artistDetailFragment, Constants.ARTISTS_DETAIL_FRAGMENT_TAG).addToBackStack(null);
            assert homeFragment != null;
            fragmentTransaction.hide(homeFragment).show(artistDetailFragment).commit();
            if (((AppCompatActivity) view.getContext()).getSupportActionBar() != null) {
                Objects.requireNonNull(((AppCompatActivity) view.getContext()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                Objects.requireNonNull(((AppCompatActivity) view.getContext()).getSupportActionBar()).setHomeButtonEnabled(true);
            }
            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Constants.SHARED_PREFERENCES_MODE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.HOME_ARTIST_DETAILS_FRAGMENT_VISIBLE, true);
            editor.apply();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        HomeArtistsRowBinding homeArtistsRowBinding;

        public MyViewHolder(@NonNull HomeArtistsRowBinding homeArtistsRowBinding) {
            super(homeArtistsRowBinding.getRoot());

            this.homeArtistsRowBinding = homeArtistsRowBinding;
        }
    }

    public HomeArtistAdapter(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeArtistsRowBinding homeArtistsRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.home_artists_row, parent, false);
        return new MyViewHolder(homeArtistsRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.homeArtistsRowBinding.setArtist(artists.get(position));
        holder.homeArtistsRowBinding.setClickHandler(this);
        holder.homeArtistsRowBinding.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }
}
