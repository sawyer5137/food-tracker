package com.example.foodtracker.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class StorageLocation implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    @Ignore
    public int itemCount;

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
