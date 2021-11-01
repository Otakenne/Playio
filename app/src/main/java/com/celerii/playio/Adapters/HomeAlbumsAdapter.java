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
import com.celerii.playio.Fragments.AlbumDetailFragment;
import com.celerii.playio.Fragments.HomeFragment;
import com.celerii.playio.R;
import com.celerii.playio.Utility.Constants;
import com.celerii.playio.databinding.HomeAlbumsRowBinding;
import com.celerii.playio.interfaces.OnClickHandlerInterface;
import com.celerii.playio.mods.Album;

import java.util.ArrayList;
import java.util.Objects;

public class HomeAlbumsAdapter extends RecyclerView.Adapter<HomeAlbumsAdapter.MyViewHolder>
    implements OnClickHandlerInterface {
    private final ArrayList<Album> albums;

    @Override
    public void onClick(View view, int position) {
        if (view.getId() == R.id.constraint_layout) {
            Context context = view.getContext();
            FragmentManager fragmentManager = ((BaseActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            AlbumDetailFragment albumDetailFragment = AlbumDetailFragment.newInstance(albums.get(position));
            HomeFragment homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(Constants.HOME_FRAGMENT_TAG);
            fragmentTransaction.add(R.id.fragment_container, albumDetailFragment, Constants.ALBUMS_DETAIL_FRAGMENT_TAG).addToBackStack(null);
            assert homeFragment != null;
            fragmentTransaction.hide(homeFragment).show(albumDetailFragment).commit();
            if (((AppCompatActivity) view.getContext()).getSupportActionBar() != null) {
                Objects.requireNonNull(((AppCompatActivity) view.getContext()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                Objects.requireNonNull(((AppCompatActivity) view.getContext()).getSupportActionBar()).setHomeButtonEnabled(true);
            }
            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Constants.SHARED_PREFERENCES_MODE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.HOME_ALBUM_DETAILS_FRAGMENT_VISIBLE, true);
            editor.apply();

        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public HomeAlbumsRowBinding homeAlbumsRowBinding;

        public MyViewHolder(@NonNull HomeAlbumsRowBinding homeAlbumsRowBinding) {
            super(homeAlbumsRowBinding.getRoot());

            this.homeAlbumsRowBinding = homeAlbumsRowBinding;
        }
    }

    public HomeAlbumsAdapter(ArrayList<Album> albums) {
        this.albums = albums;
    }

    @NonNull
    @Override
    public HomeAlbumsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeAlbumsRowBinding homeAlbumsRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.home_albums_row, parent, false);

        return new MyViewHolder(homeAlbumsRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAlbumsAdapter.MyViewHolder holder, int position) {
        holder.homeAlbumsRowBinding.setAlbum(albums.get(position));
        holder.homeAlbumsRowBinding.setClickHandler(this);
        holder.homeAlbumsRowBinding.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }
}
