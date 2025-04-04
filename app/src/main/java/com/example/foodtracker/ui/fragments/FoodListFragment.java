package com.example.foodtracker.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtracker.R;
import com.example.foodtracker.models.FoodItem;
import com.example.foodtracker.ui.adapter.FoodAdapter;
import com.example.foodtracker.viewmodel.FoodViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class FoodListFragment extends Fragment {

    public final String TAG = "FoodFragment";
    private FloatingActionButton addButton;
    private TextInputEditText searchInput;
    private TextView noFoodText;
    private FoodAdapter adapter;
    private RecyclerView recyclerView;
    private final android.os.Handler searchHandler = new android.os.Handler();
    private Runnable searchRunnable;


    public FoodListFragment() {
        // Required empty public constructor
    }

    // Factory method to pass in a storage location ID
    public static FoodListFragment newInstance(int locationId) {
        FoodListFragment fragment = new FoodListFragment();
        Bundle args = new Bundle();
        args.putInt("locationId", locationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //INSERTING TEST FOOD ITEMS
//        insertTestFoodItems();

        addButton = view.findViewById(R.id.btn_add_food);
        noFoodText = view.findViewById(R.id.noFoodText);
        searchInput = view.findViewById(R.id.searchInput);

        // Initialize recycler view
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_fade_in)
        );



        // Set up adapter - initially empty list because data comes async from LiveData
        adapter = new FoodAdapter(new ArrayList<>(), item -> {
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
        foodViewModel.getAllFoodItems().observe(getViewLifecycleOwner(), foodItems -> {
            adapter.setFoodList(foodItems);

            if (foodItems.isEmpty()) {
                noFoodText.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                noFoodText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.scheduleLayoutAnimation();
            }
        });

        //Add button onclick
        addButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new NewFoodItemFragment())
                    .addToBackStack(null)
                    .commit();
        });

        //Search View
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cancel any previous search
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                // Schedule new search after delay
                searchRunnable = () -> {
                    adapter.filter(s.toString());

                    recyclerView.setLayoutAnimation(
                            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_fade_in)
                    );
                    recyclerView.setAdapter(adapter);
                    recyclerView.scheduleLayoutAnimation();
                };

                // Delay execution
                searchHandler.postDelayed(searchRunnable, 500); // adjust delay if needed
            }

            @Override public void afterTextChanged(Editable s) {}
        });




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_list, container, false);
    }

    private void insertTestFoodItems() {
        FoodViewModel foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);

        String[] foodNames = {
                "Milk", "Eggs", "Bread", "Chicken", "Apples", "Cheese", "Yogurt",
                "Lettuce", "Carrots", "Rice", "Pasta", "Ground Beef", "Orange Juice",
                "Bananas", "Frozen Pizza", "Peanut Butter", "Cereal", "Tomatoes", "Butter", "Fish"
        };

        Random random = new Random();
        Date today = new Date();

        List<FoodItem> testItems = new ArrayList<>();

        for (String name : foodNames) {
            int amount = 1 + random.nextInt(5); // 1 to 5
            String unit = "pcs";

            // Generate purchase date within last 30 days
            long daysAgo = random.nextInt(30); // 0 to 29
            long purchaseMillis = today.getTime() - daysAgo * 24L * 60 * 60 * 1000;
            Date purchaseDate = new Date(purchaseMillis);

            // Generate expire date:
            // 50% chance it's already expired, 50% chance it's still good
            long expireOffsetDays = random.nextBoolean()
                    ? -random.nextInt(10) // expired: up to 10 days ago
                    : random.nextInt(15); // fresh: up to 15 days from today

            long expireMillis = today.getTime() + expireOffsetDays * 24L * 60 * 60 * 1000;
            Date expireDate = new Date(expireMillis);

            // Storage location ID (just use 1 for now)
            long storageLocationId = 1;

            FoodItem item = new FoodItem(name, amount, unit, expireDate, purchaseDate, storageLocationId);
            testItems.add(item);
        }

        for (FoodItem item : testItems) {
            foodViewModel.insert(item);
        }
    }

}

