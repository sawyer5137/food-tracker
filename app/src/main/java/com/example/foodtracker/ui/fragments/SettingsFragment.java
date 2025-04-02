package com.example.foodtracker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;


import com.example.foodtracker.R;
import com.example.foodtracker.models.StorageLocation;
import com.example.foodtracker.viewmodel.StorageLocationViewModel;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    private SwitchMaterial themeSwitch, notificationsSwitch;
    private MaterialAutoCompleteTextView defaultLocationDropdown;

    private StorageLocationViewModel storageViewModel;
    private List<StorageLocation> storageLocations;
    private ArrayAdapter<String> adapter;

    private android.content.SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        themeSwitch = view.findViewById(R.id.themeSwitch);
        notificationsSwitch = view.findViewById(R.id.notificationsSwitch);

        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        // Load saved preferences
        boolean darkMode = preferences.getBoolean("dark_mode", false);
        boolean notificationsEnabled = preferences.getBoolean("notifications_enabled", true);
        long savedLocationId = preferences.getLong("default_location_id", -1);

        themeSwitch.setChecked(darkMode);
        notificationsSwitch.setChecked(notificationsEnabled);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("dark_mode", isChecked).apply();
            int mode = isChecked ?
                    androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES :
                    androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(mode);
        });

        notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("notifications_enabled", isChecked).apply();
        });

        storageViewModel = new ViewModelProvider(requireActivity()).get(StorageLocationViewModel.class);

    }
}
