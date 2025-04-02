package com.example.foodtracker.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.foodtracker.models.StorageLocation;
import com.example.foodtracker.repository.AppRepository;
import com.example.foodtracker.ui.adapter.StorageLocationWithItems;

import java.util.List;

public class StorageLocationViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<StorageLocation>> allStorageLocations;
    private final LiveData<List<StorageLocationWithItems>> allLocationsWithItems;

    public StorageLocationViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        allStorageLocations = repository.getAllStorageLocations();
        allLocationsWithItems = repository.getAllStorageLocationsWithItems();
    }

    public LiveData<List<StorageLocation>> getAllStorageLocations() {
        return allStorageLocations;
    }

    public void insert(StorageLocation storageLocation) {
        repository.insertStorageLocation(storageLocation);
    }

    public void delete(StorageLocation storageLocation) {
        repository.deleteStorageLocation(storageLocation);
    }

    public LiveData<List<StorageLocationWithItems>> getAllLocationsWithItems() {
        return allLocationsWithItems;
    }
}
