package com.example.foodtracker.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.foodtracker.R;
import com.example.foodtracker.models.FoodItem;
import com.example.foodtracker.models.StorageLocation;
import com.example.foodtracker.viewmodel.FoodViewModel;
import com.example.foodtracker.viewmodel.StorageLocationViewModel;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class FoodItemFormFragment extends Fragment {

    private EditText foodNameInput, amountInput, customUnitInput, purchaseDateInput, expireDateInput;
    private Spinner unitSpinner;
    private Button submitButton;
    private MaterialAutoCompleteTextView locationSpinner;
    private List<StorageLocation> storageLocations;
    private FoodViewModel viewModel;
    private StorageLocationViewModel locationViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_item_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        foodNameInput = view.findViewById(R.id.foodNameInput);
        amountInput = view.findViewById(R.id.amountInput);
        unitSpinner = view.findViewById(R.id.unitSpinner);
        customUnitInput = view.findViewById(R.id.customUnitInput);
        purchaseDateInput = view.findViewById(R.id.purchaseDateInput);
        expireDateInput = view.findViewById(R.id.expireDateInput);
        submitButton = view.findViewById(R.id.submitButton);
        locationSpinner = view.findViewById(R.id.storageLocationDropdown);

        // Get the ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(FoodViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(StorageLocationViewModel.class);

        purchaseDateInput.setOnClickListener(v -> showDatePicker(purchaseDateInput));
        expireDateInput.setOnClickListener(v -> showDatePicker(expireDateInput));

        locationViewModel.getAllStorageLocations().observe(getViewLifecycleOwner(), locations -> {
            this.storageLocations = locations;

            List<String> locationNames = new ArrayList<>();
            for (StorageLocation loc : locations) {
                locationNames.add(loc.name);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    locationNames
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            locationSpinner.setAdapter(adapter);

            // Optional: set default selection
            locationSpinner.setSelection(0);
        });


        submitButton.setOnClickListener(v -> {
            String name = foodNameInput.getText().toString().trim();
            String amountText = amountInput.getText().toString().trim();
            String unit = unitSpinner.getSelectedItem().toString();
            String customUnit = customUnitInput.getText().toString().trim();
            String purchaseDateText = purchaseDateInput.getText().toString().trim();
            String expireDateText = expireDateInput.getText().toString().trim();

            if (!customUnit.isEmpty()) {
                unit = customUnit;
            }

            if (name.isEmpty() || amountText.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill out required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountText);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            Date purchaseDate = null;
            Date expireDate = null;
            try {
                purchaseDate = sdf.parse(purchaseDateText);
                expireDate = sdf.parse(expireDateText);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            // Create new FoodItem
            FoodItem item = new FoodItem(name, amount, unit, expireDate, purchaseDate, 1);

            // Insert using ViewModel
            viewModel.insert(item);

            Toast.makeText(requireContext(), "Item added", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed(); // navigate back
        });

    }
    private void showDatePicker(EditText targetEditText) {
        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // 0-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format date as yyyy-MM-dd
                    String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                            selectedYear, selectedMonth + 1, selectedDay);
                    targetEditText.setText(formattedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}
