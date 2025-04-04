package com.example.foodtracker.viewmodel;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.foodtracker.models.FoodItem;
import com.example.foodtracker.models.FoodItemWithLocation;
import com.example.foodtracker.repository.AppRepository;

import java.util.List;

public class FoodViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<FoodItem>> allFoodItems;

    public FoodViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        allFoodItems = repository.getAllFoodItems();
    }

    public LiveData<List<FoodItem>> getAllFoodItems() {
        return allFoodItems;
    }

    public LiveData<FoodItem> getFoodItemById(int id) {
        return repository.getFoodItemById(id);
    }

    public LiveData<List<FoodItemWithLocation>> getFoodItemsByLocation(int locationId) {
        return repository.getFoodItemsByLocation(locationId);
    }

    public LiveData<List<FoodItemWithLocation>> searchFoodWithLocation(String query) {
        return repository.searchFoodWithLocation(query);
    }

    public LiveData<List<FoodItem>> searchFoodByName(String query) {
        return repository.searchFoodByName(query);
    }

    public void deleteAllByLocation(long locationId) {
        repository.deleteAllByLocation(locationId);
    }

    public LiveData<List<FoodItemWithLocation>> getAllFoodWithLocation() {
        return repository.getAllFoodWithLocation();
    }

    public void insert(FoodItem foodItem) {
        repository.insertFoodItem(foodItem);
    }

    public void update(FoodItem foodItem) {
        repository.updateFoodItem(foodItem);
    }

    public void delete(FoodItem foodItem) {repository.deleteFoodItem(foodItem);}

}