package com.example.foodtracker.viewmodel;


import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.foodtracker.models.FoodItem;
import com.example.foodtracker.models.FoodItemWithLocation;
import com.example.foodtracker.notifications.FoodReminderWorker;
import com.example.foodtracker.repository.AppRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FoodViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<FoodItem>> allFoodItems;

    public FoodViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        allFoodItems = repository.getAllFoodItems();
    }

    public LiveData<List<FoodItem>> getAllFoodItems() {
        return allFoodItems;
    }

    public LiveData<FoodItem> getFoodItemById(int id) {
        return repository.getFoodItemById(id);
    }

    public LiveData<List<FoodItemWithLocation>> getFoodItemsByLocation(int locationId) {
        return repository.getFoodItemsByLocation(locationId);
    }

    public LiveData<List<FoodItemWithLocation>> searchFoodWithLocation(String query) {
        return repository.searchFoodWithLocation(query);
    }

    public LiveData<List<FoodItem>> searchFoodByName(String query) {
        return repository.searchFoodByName(query);
    }

    public void deleteAllByLocation(long locationId) {
        repository.deleteAllByLocation(locationId);
    }

    public LiveData<List<FoodItemWithLocation>> getAllFoodWithLocationSortedByLastModified() {
        return repository.getAllFoodWithLocationSortedByLastModified();
    }

    public LiveData<List<FoodItemWithLocation>> getAllFoodWithLocation() {
        return repository.getAllFoodWithLocation();
    }

    public void insert(FoodItem foodItem) {
        repository.insertFoodItem(foodItem);
        scheduleReminder(foodItem);
    }

    public void update(FoodItem foodItem) {
        repository.updateFoodItem(foodItem);
    }

    public void delete(FoodItem foodItem) {
        repository.deleteFoodItem(foodItem);
        cancelReminder(foodItem.id);
    }

    public void scheduleReminder(FoodItem item) {
        Context context = getApplication();

        long shelfLifeMillis = item.expirationDate.getTime() - item.purchaseDate.getTime();
//        long reminderTimeMillis = item.purchaseDate.getTime() + (long)(shelfLifeMillis * 0.9);
        long reminderTimeMillis = System.currentTimeMillis() + 10 * 1000; // 1 minute from now
        long delayMillis = reminderTimeMillis - System.currentTimeMillis();

        if (delayMillis <= 0) return; // Don’t schedule for past times

        Data data = new Data.Builder()
                .putString("foodName", item.name)
                .putLong("foodId", item.id) // optional
                .build();

        OneTimeWorkRequest reminderRequest = new OneTimeWorkRequest.Builder(FoodReminderWorker.class)
                .setInputData(data)
                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                .addTag("food_reminder_" + item.id)
                .build();

        WorkManager.getInstance(context).enqueue(reminderRequest);
    }

    public void cancelReminder(long foodId) {
        Context context = getApplication();
        WorkManager.getInstance(context).cancelAllWorkByTag("food_reminder_" + foodId);
    }



}