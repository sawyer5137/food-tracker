package com.example.foodtracker.viewmodel;


import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.foodtracker.models.FoodItem;
import com.example.foodtracker.models.StorageLocation;
import com.example.foodtracker.repository.AppRepository;

import java.util.List;

public class FoodViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<FoodItem>> allFoodItems;
    private LiveData<List<StorageLocation>> allStorageLocations;

    public FoodViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        allFoodItems = repository.getAllFoodItems();
        allStorageLocations = repository.getAllStorageLocations();
    }

    public LiveData<List<FoodItem>> getAllFoodItems() {
        return allFoodItems;
    }

    public LiveData<List<StorageLocation>> getAllStorageLocations() {
        return allStorageLocations;
    }

    public LiveData<List<FoodItem>> getFoodItemsByLocation(int locationId) {
        return repository.getFoodItemsByLocation(locationId);
    }

    public void insert(FoodItem foodItem) {
        repository.insertFoodItem(foodItem);
    }

}