package com.example.foodtracker.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.foodtracker.R;
import com.example.foodtracker.ui.fragments.AddFragment;
import com.example.foodtracker.ui.fragments.FoodFragment;
import com.example.foodtracker.ui.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_nav);

        // Show default fragment when activity starts
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FoodFragment())
                    .commit();
            bottomNav.setSelectedItemId(R.id.nav_food);
        }

        // Handle bottom nav item selection
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_food) {
                selectedFragment = new FoodFragment();
            } else if (id == R.id.nav_locations) {
                selectedFragment = new AddFragment();
            } else if (id == R.id.nav_settings) {
                selectedFragment = new SettingsFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });
    }
}
