package com.example.foodtracker.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.foodtracker.R;
import com.example.foodtracker.ui.adapter.StorageLocationAdapter;
import com.example.foodtracker.viewmodel.StorageLocationViewModel;

import java.util.ArrayList;

public class StorageFragment extends Fragment {

    public final String TAG = "StorageFragment";
    private Button addButton;


    public StorageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addButton = view.findViewById(R.id.btn_add_food);
        addButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new StorageLocationFormFragment())
                    .addToBackStack(null) // important: so back button returns to FoodFragment
                    .commit();
        });

        //Initialize Recycler View
        RecyclerView recyclerView = view.findViewById(R.id.storageLocationRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns

        //Set up adapter. Initially empty
        StorageLocationAdapter adapter = new StorageLocationAdapter(new ArrayList<>(), location -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, FoodFragment.newInstance(location.id))
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(adapter);

        //Get View Model
        StorageLocationViewModel storageViewModel = new ViewModelProvider(this).get(StorageLocationViewModel.class);

        storageViewModel.getAllStorageLocations().observe(getViewLifecycleOwner(), storageLocations -> {
            adapter.setLocationList(storageLocations);
        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_storage, container, false);
    }
}
