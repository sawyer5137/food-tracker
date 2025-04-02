package com.example.foodtracker.ui.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodtracker.R;
import com.example.foodtracker.models.StorageLocation;
import com.example.foodtracker.viewmodel.StorageLocationViewModel;

public class StorageLocationFormFragment extends Fragment {

    EditText storageNameInput;
    Button submitButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_storage_location_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storageNameInput = view.findViewById(R.id.storageLocationNameInput);
        submitButton = view.findViewById(R.id.storageSubmitButton);

        StorageLocationViewModel viewModel = new ViewModelProvider(requireActivity()).get(StorageLocationViewModel.class);

        submitButton.setOnClickListener(v -> {
            String name = storageNameInput.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
                return;
            }

            StorageLocation location = new StorageLocation(name);
            viewModel.insert(location);

            Toast.makeText(requireContext(), "Storage location added", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        });
    }
}
