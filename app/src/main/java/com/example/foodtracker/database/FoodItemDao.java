package com.example.foodtracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodtracker.models.FoodItem;

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

    @Query("SELECT * FROM FOODITEM WHERE STORAGELOCATIONID = :locationId")
        LiveData<List<FoodItem>> getFoodItemsByLocation(int locationId);

    @Query("DELETE FROM FoodItem WHERE storageLocationId = :locationId")
    void deleteAllByLocation(long locationId);
}
