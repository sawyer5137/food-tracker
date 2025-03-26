package com.example.foodtracker.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodtracker.ui.fragments.FoodItemFormFragment;
import com.example.foodtracker.ui.fragments.StorageLocationFormFragment;

public class FormPagerAdapter extends FragmentStateAdapter {
    public FormPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? new FoodItemFormFragment() : new StorageLocationFormFragment();
    }

    @Override
    public int getItemCount() {
        return 2; // Two tabs
    }
}
