package com.example.foodtracker.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity(
    foreignKeys = @ForeignKey(
            entity = StorageLocation.class,
            parentColumns = "id",
            childColumns = "storageLocationId",
            onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("storageLocationId")}
)

public class FoodItem implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public double originalQuantity;
    public boolean isIncrementing; // true = whole number count (e.g. bananas), false = variable (e.g. ml)
    public double currentQuantity;
    public String unit;
    @NonNull
    public Date lastModified = new Date();
    public Date expirationDate;
    public Date purchaseDate;
    public long storageLocationId;

    public FoodItem() {}

    public FoodItem(String name, double originalQuantity, String unit, Date expirationDate, Date purchaseDate, boolean isIncrementing, long storageLocationId) {
        this.name = name;
        this.originalQuantity = originalQuantity;
        this.currentQuantity = originalQuantity;
        this.unit = unit;
        this.lastModified = new Date();
        this.expirationDate = expirationDate;
        this.purchaseDate = purchaseDate;
        this.isIncrementing = isIncrementing;
        this.storageLocationId = storageLocationId;
    }

    @Override
    public String toString() {
        return "FoodItem{id=" + id +
                ", name='" + name + '\'' +
                ", currentQuantity=" + currentQuantity +
                ", expirationDate='" + expirationDate + '\'' +
                ", purchaseDate='" + purchaseDate + '\'' +
                ", storageLocationId=" + storageLocationId +
                '}';
    }

    public boolean isExpired() {
        return this.expirationDate.before(new Date());
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
