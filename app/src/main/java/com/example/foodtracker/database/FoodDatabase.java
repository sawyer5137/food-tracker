package com.example.foodtracker.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.foodtracker.converters.DateConverter;
import com.example.foodtracker.models.FoodItem;
import com.example.foodtracker.models.StorageLocation;

@Database(entities = {FoodItem.class, StorageLocation.class}, version = 2)
@TypeConverters({DateConverter.class})
public abstract class FoodDatabase extends RoomDatabase {
    private static volatile FoodDatabase INSTANCE;

    public abstract FoodItemDao foodItemDao();
    public abstract StorageLocationDao storageLocationDao();

    public static FoodDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (FoodDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    FoodDatabase.class, "food_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
