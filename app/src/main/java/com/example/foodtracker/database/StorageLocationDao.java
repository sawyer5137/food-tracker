package com.example.foodtracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodtracker.models.FoodItem;
import com.example.foodtracker.models.StorageLocation;

import java.util.List;

@Dao
public interface StorageLocationDao {
    @Insert
    public void insertStorageLocations(StorageLocation... storageLocations);

    @Update
    public void updateStorageLocations(StorageLocation... storageLocations);

    @Delete
    public void deleteStorageLocations(StorageLocation... storageLocations);

    @Query("SELECT * FROM STORAGE_LOCATIONS")
        public LiveData<List<StorageLocation>> getAllStorageLocations();
}