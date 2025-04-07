package com.example.foodtracker.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.foodtracker.models.FoodItemWithLocation;
import com.example.foodtracker.ui.adapter.FoodAdapter;
import com.example.foodtracker.viewmodel.FoodViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class FoodListFragment extends Fragment {

    public final String TAG = "FoodFragment";
    private FloatingActionButton addButton;
    private EditText searchInput;
    private TextView noFoodText;
    private FoodAdapter adapter;
    private RecyclerView recyclerView;
    private List<FoodItemWithLocation> fullFoodList = new ArrayList<>();
    private FoodViewModel foodViewModel;

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
        searchInput = (EditText) view.findViewById(R.id.searchInput);


        // Initialize recycler view
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
        }, true);
        recyclerView.setAdapter(adapter);

        // Get ViewModel
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);

        // Observe live data with storage location
        foodViewModel.getAllFoodWithLocationSortedByLastModified().observe(getViewLifecycleOwner(), foodWithLocations -> {
            Log.d("FOOD_LIST", "Updating adapter with list of size: " + foodWithLocations.size());

            fullFoodList.clear();
            fullFoodList.addAll(foodWithLocations); // ✅ Keep master copy

            String query = searchInput.getText().toString().toLowerCase().trim();

            List<FoodItemWithLocation> filtered = new ArrayList<>();
            for (FoodItemWithLocation item : fullFoodList) {
                if (item.food.name.toLowerCase().contains(query)) {
                    filtered.add(item);
                }
            }

            // still update in case of minor change
            adapter.setFoodList(filtered);

            boolean isEmpty = filtered.isEmpty();
            noFoodText.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

        });


        //Add button onclick
        addButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new NewFoodItemFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Search View
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("SEARCH_INPUT", "Current query: '" + s + "'");

                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                searchRunnable = () -> {
                    String query = s.toString().toLowerCase().trim();
                    List<FoodItemWithLocation> filtered = new ArrayList<>();

                    for (FoodItemWithLocation item : fullFoodList) {
                        if (item.food.name.toLowerCase().contains(query)) {
                            filtered.add(item);
                        }
                    }

                    adapter.setFoodList(filtered);

                    boolean isEmpty = filtered.isEmpty();
                    noFoodText.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                    recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
                    recyclerView.scheduleLayoutAnimation();
                };

                searchHandler.postDelayed(searchRunnable, 300);
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
                "Bananas", "Frozen Pizza", "Peanut Butter", "Cereal", "Tomatoes", "Butter", "Fish",
                "Strawberries", "Avocados", "Hot Dog", "Bacon", "Mushrooms", "Onions", "Tacos",
                "Watermelon", "Broccoli", "Sandwich"
        };

        Random random = new Random();
        Date today = new Date();

        List<FoodItem> testItems = new ArrayList<>();

        for (String name : foodNames) {
            boolean isIncrementing = random.nextBoolean();

            double amount;
            String unit;

            if (isIncrementing) {
                amount = 1 + random.nextInt(5); // 1–5
                unit = "pcs";
            } else {
                amount = 500 + random.nextInt(2000); // 500–2500
                unit = random.nextBoolean() ? "ml" : "g";
            }

            // Purchase date: random within past 30 days
            long daysAgo = random.nextInt(30);
            long purchaseMillis = today.getTime() - daysAgo * 24L * 60 * 60 * 1000;
            Date purchaseDate = new Date(purchaseMillis);

            // Expiration date
            Date expireDate;
            if (random.nextInt(10) == 0) { // 10% chance it's already expired
                long expireMillis = today.getTime() - (1 + random.nextInt(10)) * 24L * 60 * 60 * 1000;
                expireDate = new Date(expireMillis);
            } else {
                long expireMillis = today.getTime() + random.nextInt(15) * 24L * 60 * 60 * 1000;
                expireDate = new Date(expireMillis);
            }

            // Storage location: random 1, 2, or 3
            long storageLocationId = 1 + random.nextInt(3);

            FoodItem item = new FoodItem(name, amount, unit, expireDate, purchaseDate, isIncrementing, storageLocationId);
            testItems.add(item);
        }

        for (FoodItem item : testItems) {
            foodViewModel.insert(item);
        }
    }


}

