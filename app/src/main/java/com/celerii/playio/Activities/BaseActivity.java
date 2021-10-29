package com.celerii.playio.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.celerii.playio.Fragments.HomeFragment;
import com.celerii.playio.R;
import com.celerii.playio.databinding.ActivityBaseBinding;

public class BaseActivity extends AppCompatActivity {

    ActivityBaseBinding activityBaseBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base);

        initializeUI();

        FragmentManager fragmentManager = getSupportFragmentManager();

        HomeFragment homeFragment = (HomeFragment) fragmentManager.findFragmentById(R.id.fragment_container);

        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, homeFragment).addToBackStack(null).commit();
        } else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(homeFragment).commit();
            homeFragment = HomeFragment.newInstance();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, homeFragment).addToBackStack(null).commit();
        }

    }

    private void initializeUI() {
        setSupportActionBar(activityBaseBinding.toolbar);
        activityBaseBinding.toolbarTitle.setText(getString(R.string.app_name));

//        activityBaseBinding.smartPlayControls.setVisibility(View.GONE);
//        activityBaseBinding.separatorView.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.theme_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.dark_mode){
            int nightMode = AppCompatDelegate.getDefaultNightMode();

            // if "night mode" is active, set to day and vice versa
            if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}