package com.example.foodtracker.models;
// FoodWithLocation.java

import androidx.room.Embedded;
import androidx.room.Relation;

public class FoodItemWithLocation {

    @Embedded
    public FoodItem food;

    @Relation(
            parentColumn = "storageLocationId",
            entityColumn = "id"
    )
    public StorageLocation location;

    public String getStorageLocationName() {
        return location != null ? location.name : "Unknown";
    }
}
