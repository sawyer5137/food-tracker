package com.example.foodtracker.ui.adapter;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.foodtracker.models.FoodItem;
import com.example.foodtracker.models.StorageLocation;

import java.util.List;

public class StorageLocationWithItems {
    @Embedded
    public StorageLocation storageLocation;

    @Relation(
            parentColumn = "id",
            entityColumn = "storageLocationId"
    )
    public List<FoodItem> foodItems;
}
