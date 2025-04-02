package com.example.foodtracker.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.foodtracker.R;
import com.example.foodtracker.ui.adapter.FoodAdapter;
import com.example.foodtracker.viewmodel.FoodViewModel;

import java.util.ArrayList;

public class FoodFragment extends Fragment {

    public final String TAG = "FoodFragment";
    private Button addButton;
    private int storageLocationId = -1;

    public FoodFragment() {
        // Required empty public constructor
    }

    // Factory method to pass in a storage location ID
    public static FoodFragment newInstance(int locationId) {
        FoodFragment fragment = new FoodFragment();
        Bundle args = new Bundle();
        args.putInt("locationId", locationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get locationId from arguments if available
        if (getArguments() != null) {
            storageLocationId = getArguments().getInt("locationId", -1);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addButton = view.findViewById(R.id.btn_add_food);

        // Hide the button if we're showing a filtered list
        if (storageLocationId != -1) {
            addButton.setVisibility(View.GONE);
        }

        // Initialize recycler view
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up adapter - initially empty list because data comes async from LiveData
        FoodAdapter adapter = new FoodAdapter(new ArrayList<>(), item -> {
            // Handle click here, e.g., open edit fragment
            EditFoodItemFragment fragment = new EditFoodItemFragment();

            Bundle args = new Bundle();
            args.putSerializable("foodItem", item);
            fragment.setArguments(args);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setAdapter(adapter);

        // Get ViewModel
        FoodViewModel foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);

        // Observe live data based on storage location
        if (storageLocationId == -1) {
            foodViewModel.getAllFoodItems().observe(getViewLifecycleOwner(), foodItems -> {
                Log.d(TAG, "All items: " + foodItems.size());
                adapter.setFoodList(foodItems);
            });
        } else {
            foodViewModel.getFoodItemsByLocation(storageLocationId).observe(getViewLifecycleOwner(), foodItems -> {
                Log.d(TAG, "Items in location " + storageLocationId + ": " + foodItems.size());
                adapter.setFoodList(foodItems);
            });
        }

        // Add button behavior (only visible if not filtered)
        addButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new FoodItemFormFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foods, container, false);
    }
}

