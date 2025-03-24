package com.example.foodtracker.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodtracker.R;
import com.example.foodtracker.ui.adapter.FoodAdapter;
import com.example.foodtracker.viewmodel.FoodViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public final String TAG = "HomeFragment";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialize recycler view
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Set up adapter - initially empty list because data comes async from live data
        FoodAdapter adapter = new FoodAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        //Get FoodViewModel
        FoodViewModel foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);



        // Observe live data and populate recycler view
        foodViewModel.getAllFoodItems().observe(getViewLifecycleOwner(), foodItems -> {
            Log.d(TAG, foodItems.size()+"");
            adapter.setFoodList(foodItems);
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
