package com.example.foodtracker.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;
@Entity
public class FoodItem {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public int quantity;
    public String unit;
    public Date expirationDate;
    public Date purchaseDate;
    public long storageLocationId;

    public FoodItem() {}

    public FoodItem(String name, int quantity, String unit, Date expirationDate, Date purchaseDate, long storageLocationId) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expirationDate = expirationDate;
        this.purchaseDate = purchaseDate;
        this.storageLocationId = storageLocationId;
    }

    @Override
    public String toString() {
        return "FoodItem{id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", expirationDate='" + expirationDate + '\'' +
                ", purchaseDate='" + purchaseDate + '\'' +
                ", storageLocationId=" + storageLocationId +
                '}';
    }

    public String getDaysLeft() {
        // Reset current time to start of the day (midnight)
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        // Reset expireDate to start of its day
        Calendar expire = Calendar.getInstance();
        expire.setTime(this.expirationDate);
        expire.set(Calendar.HOUR_OF_DAY, 0);
        expire.set(Calendar.MINUTE, 0);
        expire.set(Calendar.SECOND, 0);
        expire.set(Calendar.MILLISECOND, 0);

        // Compute days difference
        long millisecondsLeft = expire.getTimeInMillis() - today.getTimeInMillis();
        return millisecondsLeft / (1000 * 60 * 60 * 24) + "";
    }


}
