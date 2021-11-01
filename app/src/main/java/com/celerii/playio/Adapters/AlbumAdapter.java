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
import com.celerii.playio.Fragments.AlbumsFragment;
import com.celerii.playio.R;
import com.celerii.playio.Utility.Constants;
import com.celerii.playio.databinding.AlbumRowBinding;
import com.celerii.playio.databinding.AlbumsFragmentHeaderBinding;
import com.celerii.playio.interfaces.OnClickHandlerInterface;
import com.celerii.playio.mods.Album;

import java.util.ArrayList;
import java.util.Objects;

public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements OnClickHandlerInterface {
    private final ArrayList<Album> albums;
    private static final int header = 1;
    private static final int normal = 2;

    @Override
    public void onClick(View view, int position) {
        if (view.getId() == R.id.constraint_layout) {
            Context context = view.getContext();
            FragmentManager fragmentManager = ((BaseActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            AlbumDetailFragment albumDetailFragment = AlbumDetailFragment.newInstance(albums.get(position));
            AlbumsFragment albumsFragment = (AlbumsFragment) fragmentManager.findFragmentByTag(Constants.ALBUMS_FRAGMENT_TAG);
            fragmentTransaction.add(R.id.fragment_container, albumDetailFragment, Constants.ALBUMS_DETAIL_FRAGMENT_TAG).addToBackStack(null);
            assert albumsFragment != null;
            fragmentTransaction.hide(albumsFragment).show(albumDetailFragment).commit();
            if (((AppCompatActivity) view.getContext()).getSupportActionBar() != null) {
                Objects.requireNonNull(((AppCompatActivity) view.getContext()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                Objects.requireNonNull(((AppCompatActivity) view.getContext()).getSupportActionBar()).setHomeButtonEnabled(true);
            }
            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Constants.SHARED_PREFERENCES_MODE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.ALBUM_DETAILS_FRAGMENT_VISIBLE, true);
            editor.apply();

        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public AlbumRowBinding albumRowBinding;

        public MyViewHolder(@NonNull AlbumRowBinding albumRowBinding) {
            super(albumRowBinding.getRoot());

            this.albumRowBinding = albumRowBinding;
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public AlbumsFragmentHeaderBinding albumsFragmentHeaderBinding;

        public HeaderViewHolder(@NonNull AlbumsFragmentHeaderBinding albumsFragmentHeaderBinding) {
            super(albumsFragmentHeaderBinding.getRoot());

            this.albumsFragmentHeaderBinding = albumsFragmentHeaderBinding;
        }
    }

    public AlbumAdapter(ArrayList<Album> albums) {
        this.albums = albums;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == header) {
            AlbumsFragmentHeaderBinding albumsFragmentHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.albums_fragment_header, parent, false);
            return new HeaderViewHolder(albumsFragmentHeaderBinding);
        } else {
            AlbumRowBinding albumRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.album_row, parent, false);
            return new MyViewHolder(albumRowBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).albumRowBinding.setAlbum(albums.get(position));
            ((MyViewHolder) holder).albumRowBinding.setClickHandler(this);
            ((MyViewHolder) holder).albumRowBinding.setPosition(position);
        }
    }

    @Override
    public int getItemCount() {
        return albums.size();
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
