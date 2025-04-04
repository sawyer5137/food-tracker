package com.example.foodtracker.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtracker.R;
import com.example.foodtracker.models.StorageLocation;
import com.example.foodtracker.ui.adapter.FoodAdapter;
import com.example.foodtracker.viewmodel.FoodViewModel;
import com.example.foodtracker.viewmodel.StorageLocationViewModel;

import java.util.ArrayList;

public class StorageContentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView emptyTextView;
    private FoodAdapter adapter;
    private FoodViewModel foodViewModel;
    private StorageLocation location;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_storage_contents, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        emptyTextView = view.findViewById(R.id.txt_empty);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

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
        }, false);

        recyclerView.setAdapter(adapter);

        foodViewModel = new ViewModelProvider(requireActivity()).get(FoodViewModel.class);

        if (getArguments() != null) {
            location = (StorageLocation) getArguments().getSerializable("storageLocation");

            foodViewModel.getFoodItemsByLocation(location.id).observe(getViewLifecycleOwner(), items -> {
                adapter.setFoodList(items);


                if (items.isEmpty()) {
                    emptyTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    emptyTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

            });
        }



        Button deleteButton = view.findViewById(R.id.deleteStorageLocationButton);
        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Delete Storage Location")
                    .setMessage("Are you sure you want to delete this location and all of its food items?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        foodViewModel.deleteAllByLocation(location.id); // if you support this
                        new ViewModelProvider(requireActivity()).get(StorageLocationViewModel.class).delete(location);
                        requireActivity().getSupportFragmentManager().popBackStack();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    public static StorageContentsFragment newInstance(StorageLocation location) {
        StorageContentsFragment fragment = new StorageContentsFragment();
        Bundle args = new Bundle();
        args.putSerializable("storageLocation", location);
        fragment.setArguments(args);
        return fragment;
    }
}
