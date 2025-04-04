package com.example.foodtracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.foodtracker.models.FoodItem;
import com.example.foodtracker.models.FoodItemWithLocation;

import java.util.List;

@Dao
public interface FoodItemDao {
    @Insert
    public void insertFoodItems(FoodItem... foodItems);

    @Update
    public void updateFoodItems(FoodItem... foodItems);

    @Delete
    public void deleteFoodItems(FoodItem... foodItems);

    @Query("SELECT * FROM FOODITEM")
    public LiveData<List<FoodItem>> getAllFoodItems();

    @Query("SELECT * FROM FOODITEM WHERE ID = :id")
    public LiveData<FoodItem> getFoodItemByID(long id);

    @Transaction
    @Query("SELECT * FROM FoodItem WHERE storageLocationId = :locationId")
    LiveData<List<FoodItemWithLocation>> getFoodItemsByLocation(long locationId);

    @Query("SELECT * FROM FoodItem WHERE name LIKE '%' || :query || '%'")
    LiveData<List<FoodItem>> searchFoodByName(String query);

    @Query("SELECT * FROM FoodItem WHERE name LIKE '%' || :query || '%'")
    LiveData<List<FoodItemWithLocation>> searchFoodWithLocation(String query);


    @Transaction
    @Query("SELECT * FROM FoodItem")
    LiveData<List<FoodItemWithLocation>> getAllFoodWithLocation();

    @Query("DELETE FROM FoodItem WHERE storageLocationId = :locationId")
    void deleteAllByLocation(long locationId);
}
