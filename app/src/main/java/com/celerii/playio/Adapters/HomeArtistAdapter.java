package com.celerii.playio.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class HomeArtistAdapter extends RecyclerView.Adapter<HomeArtistAdapter.MyViewHolder>
    implements OnClickHandlerInterface {
    private final ArrayList<Artist> artists;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.constraint_layout) {
            Context context = view.getContext();
            Toast.makeText(context, "Artist Clicked", Toast.LENGTH_LONG).show();
            FragmentManager fragmentManager = ((BaseActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ArtistDetailFragment artistDetailFragment = ArtistDetailFragment.newInstance();
            HomeFragment homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(Constants.HOME_FRAGMENT_TAG);
            fragmentTransaction.add(R.id.fragment_container, artistDetailFragment, Constants.ARTISTS_DETAIL_FRAGMENT_TAG).addToBackStack(null);
            assert homeFragment != null;
            fragmentTransaction.hide(homeFragment).show(artistDetailFragment).commit();
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
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }
}
