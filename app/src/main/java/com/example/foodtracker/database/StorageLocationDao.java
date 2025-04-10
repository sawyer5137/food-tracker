package com.example.foodtracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.foodtracker.models.FoodItem;
import com.example.foodtracker.models.StorageLocation;
import com.example.foodtracker.ui.adapter.StorageLocationWithItems;

import java.util.List;

@Dao
public interface StorageLocationDao {
    @Insert
    public void insertStorageLocations(StorageLocation... storageLocations);

    @Update
    public void updateStorageLocations(StorageLocation... storageLocations);

    @Delete
    public void deleteStorageLocations(StorageLocation... storageLocations);

    @Query("SELECT * FROM STORAGELOCATION")
        public LiveData<List<StorageLocation>> getAllStorageLocations();

    @Transaction
    @Query("SELECT * FROM StorageLocation")
    LiveData<List<StorageLocationWithItems>> getAllStorageLocationsWithItems();
}