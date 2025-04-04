package com.example.foodtracker.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.foodtracker.database.FoodDatabase;
import com.example.foodtracker.database.FoodItemDao;
import com.example.foodtracker.database.StorageLocationDao;
import com.example.foodtracker.models.FoodItem;
import com.example.foodtracker.models.FoodItemWithLocation;
import com.example.foodtracker.models.StorageLocation;
import com.example.foodtracker.ui.adapter.StorageLocationWithItems;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppRepository {
    private FoodItemDao foodItemDao;
    private StorageLocationDao storageLocationDao;
    private LiveData<List<FoodItem>> allFoodItems;
    private LiveData<List<StorageLocation>> allStorageLocations;
    private final LiveData<List<StorageLocationWithItems>> allLocationsWithItems;
    private static final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    public AppRepository(Context context) {
        FoodDatabase db = FoodDatabase.getInstance(context);
        foodItemDao = db.foodItemDao();
        storageLocationDao = db.storageLocationDao();
        allFoodItems = foodItemDao.getAllFoodItems();
        allStorageLocations = storageLocationDao.getAllStorageLocations();
        allLocationsWithItems = storageLocationDao.getAllStorageLocationsWithItems();
    }

    // FoodItem Methods
    public LiveData<List<FoodItem>> getAllFoodItems() {
        return allFoodItems;
    }

    public LiveData<List<FoodItemWithLocation>> getFoodItemsByLocation(long locationId) {
        return foodItemDao.getFoodItemsByLocation(locationId);
    }


    public LiveData<FoodItem> getFoodItemById(int foodId){
        return foodItemDao.getFoodItemByID(foodId);
    }

    public void insertFoodItem(FoodItem foodItem) {
        databaseExecutor.execute(() -> foodItemDao.insertFoodItems(foodItem));
    }

    public void deleteFoodItem(FoodItem foodItem) {
        databaseExecutor.execute(() -> foodItemDao.deleteFoodItems(foodItem));
    }

    public LiveData<List<FoodItem>> searchFoodByName(String query) {
        return foodItemDao.searchFoodByName(query);
    }

    public LiveData<List<FoodItemWithLocation>> searchFoodWithLocation(String query) {
        return foodItemDao.searchFoodWithLocation(query);
    }

    public void updateFoodItem(FoodItem foodItem) {
        databaseExecutor.execute(()->foodItemDao.updateFoodItems(foodItem));
    }

    public LiveData<List<FoodItemWithLocation>> getAllFoodWithLocation() {
        return foodItemDao.getAllFoodWithLocation();
    }

    // StorageLocation Methods
    public LiveData<List<StorageLocation>> getAllStorageLocations() {
        return allStorageLocations;
    }

    public void insertStorageLocation(StorageLocation storageLocation) {
        databaseExecutor.execute(() -> storageLocationDao.insertStorageLocations(storageLocation));
    }

    public void deleteStorageLocation(StorageLocation storageLocation) {
        databaseExecutor.execute(() -> storageLocationDao.deleteStorageLocations(storageLocation));
    }

    public LiveData<List<StorageLocationWithItems>> getAllStorageLocationsWithItems() {
        return allLocationsWithItems;
    }

    public void deleteAllByLocation(long locationId) {
        databaseExecutor.execute(() -> foodItemDao.deleteAllByLocation(locationId));
    }

}
