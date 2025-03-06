package com.example.foodtracker.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
@Entity
public class FoodItem {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public int quantity;
    public String unit;
    public Date expirationDate;

    public long storageLocationId;

    public FoodItem() {}

    public FoodItem(String name, int quantity, String unit, Date expirationDate, long storageLocationId) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expirationDate = expirationDate;
        this.storageLocationId = storageLocationId;
    }


}
