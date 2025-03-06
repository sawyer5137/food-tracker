package com.example.foodtracker.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "storage_locations")
public class StorageLocation {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name; // Example: "Fridge", "Pantry", "Freezer"

    public StorageLocation(String name) {
        this.name = name;
    }
}
