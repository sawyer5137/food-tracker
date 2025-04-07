package com.example.foodtracker.ui;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.foodtracker.R;
import com.example.foodtracker.ui.fragments.FoodListFragment;
import com.example.foodtracker.ui.fragments.SettingsFragment;
import com.example.foodtracker.ui.fragments.StorageListFragment;
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
                    .replace(R.id.fragment_container, new FoodListFragment())
                    .commit();
            bottomNav.setSelectedItemId(R.id.nav_food);
        }

        // Handle bottom nav item selection
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_food) {
                Log.d("NAV", "FoodFragment selected");
                selectedFragment = new FoodListFragment();
            } else if (id == R.id.nav_locations) {
                Log.d("NAV", "StorageFragment selected");
                selectedFragment = new StorageListFragment();
            } else if (id == R.id.nav_settings) {
                Log.d("NAV", "SettingsFragment selected");
                selectedFragment = new SettingsFragment();
            }

            if (selectedFragment != null) {
                // 🔥 Clear any previous fragment back stack to prevent leftovers
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });

        //Requestion notificaoitn permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1001);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "food_reminder_channel",
                    "Food Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Reminders for expiring food");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


    }
}
