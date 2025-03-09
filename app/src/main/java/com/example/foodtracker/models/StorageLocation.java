package com.example.foodtracker.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class StorageLocation {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public StorageLocation(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StorageLocation{id=" + id +
                ", name='" + name +
                '}';
    }
}
