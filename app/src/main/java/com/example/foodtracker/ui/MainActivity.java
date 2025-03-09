package com.example.foodtracker.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtracker.R;
import com.example.foodtracker.database.FoodDatabase;
import com.example.foodtracker.database.FoodItemDao;
import com.example.foodtracker.models.FoodItem;
import com.example.foodtracker.models.StorageLocation;
import com.example.foodtracker.ui.adapter.FoodAdapter;
import com.example.foodtracker.viewmodel.FoodViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private FoodViewModel foodViewModel;
    private RecyclerView recyclerView;
    private FoodAdapter adapter;

    // Helper function to generate a future date
    public static Date getFutureDate(int daysAhead) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, daysAhead);
        return calendar.getTime();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Initialize recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Set up adapter - initially empty list because data comes async from live data
        adapter = new FoodAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        //Get FoodViewModel
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);

        // Observe live data and populate recycler view
        foodViewModel.getAllFoodItems().observe(this, foodItems -> {
            Log.d(TAG, foodItems.toString());
            adapter.setFoodList(foodItems);
        });

    }
}