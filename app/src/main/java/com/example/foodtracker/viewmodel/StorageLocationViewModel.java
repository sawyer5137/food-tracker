package com.example.foodtracker.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.foodtracker.models.StorageLocation;
import com.example.foodtracker.repository.AppRepository;

import java.util.List;

public class StorageLocationViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<StorageLocation>> allStorageLocations;

    public StorageLocationViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        allStorageLocations = repository.getAllStorageLocations();
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
}
