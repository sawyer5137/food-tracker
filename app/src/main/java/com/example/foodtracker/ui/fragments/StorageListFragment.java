package com.example.foodtracker.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtracker.R;
import com.example.foodtracker.models.StorageLocation;
import com.example.foodtracker.ui.adapter.StorageLocationAdapter;
import com.example.foodtracker.viewmodel.FoodViewModel;
import com.example.foodtracker.viewmodel.StorageLocationViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class StorageListFragment extends Fragment {

    private static final String TAG = "StorageFragment";

    private FloatingActionButton addButton;
    private StorageLocation location;

    public StorageListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Setup Add Button
        addButton = view.findViewById(R.id.btn_add_location);
        addButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new NewStorageLocationFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // 2. Setup RecyclerView and Adapter
        RecyclerView recyclerView = view.findViewById(R.id.storageLocationRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns

        StorageLocationAdapter adapter = new StorageLocationAdapter(new ArrayList<>(), selectedLocation -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, StorageContentsFragment.newInstance(selectedLocation))
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(adapter);

        // 3. Initialize ViewModels
        StorageLocationViewModel storageViewModel = new ViewModelProvider(this).get(StorageLocationViewModel.class);
        FoodViewModel foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);

        // 4. Optional: Handle passed-in storage location (if any)
        if (getArguments() != null) {
            location = (StorageLocation) getArguments().getSerializable("storageLocation");

            foodViewModel.getFoodItemsByLocation(location.id).observe(getViewLifecycleOwner(), items -> {
                // You might not need this if this fragment doesn't directly show food items
                Log.d(TAG, "Loaded food items for location " + location.name);
            });
        }

        // 5. Observe all storage locations and set their item counts
        storageViewModel.getAllStorageLocations().observe(getViewLifecycleOwner(), storageLocations -> {
            adapter.setLocationList(storageLocations);

            for (int i = 0; i < storageLocations.size(); i++) {
                StorageLocation currentLocation = storageLocations.get(i);
                int finalIndex = i;

                foodViewModel.getFoodItemsByLocation(currentLocation.id)
                        .observe(getViewLifecycleOwner(), foodItems -> {
                            currentLocation.itemCount = foodItems.size();
                            adapter.notifyItemChanged(finalIndex);
                        });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_storage, container, false);
    }
}