package com.example.foodtracker.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.foodtracker.converters.DateConverter;
import com.example.foodtracker.models.FoodItem;
import com.example.foodtracker.models.StorageLocation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FoodItem.class, StorageLocation.class}, version = 5)
@TypeConverters({DateConverter.class})
public abstract class FoodDatabase extends RoomDatabase {
    private static volatile FoodDatabase INSTANCE;
    private static final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    public abstract FoodItemDao foodItemDao();
    public abstract StorageLocationDao storageLocationDao();

    public static FoodDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (FoodDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    FoodDatabase.class, "food_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseExecutor.execute(() -> {
                //Preload database with locations
                StorageLocationDao locationDao = INSTANCE.storageLocationDao();
                locationDao.insertStorageLocations(
                        new StorageLocation("Fridge"),
                        new StorageLocation("Freezer"),
                        new StorageLocation("Pantry")
                );
            });
        }
    };
}
