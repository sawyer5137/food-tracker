package com.example.foodtracker.ui.fragments;

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
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.foodtracker.R;
import com.example.foodtracker.models.FoodItem;
import com.example.foodtracker.viewmodel.FoodViewModel;

public class EditFoodItemFragment extends Fragment {

    private EditText nameInput;
    private Button saveButton;
    private FoodViewModel foodItemViewModel;
    private FoodItem existingItem;

    public EditFoodItemFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        SeekBar amountSlider = view.findViewById(R.id.amountSlider);
        TextView amountLabel = view.findViewById(R.id.amountLabel);

        if (existingItem != null && existingItem.originalAmount > 0) {
            double percentLeft = (existingItem.currentAmount / existingItem.originalAmount) * 100.0;
            amountSlider.setProgress((int) percentLeft);
            amountLabel.setText("Amount left: " + (int) percentLeft + "%");
        }

        amountSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                amountLabel.setText("Amount left: " + progress + "%");

                if (existingItem != null) {
                    existingItem.currentAmount = (progress / 100.0) * existingItem.originalAmount;
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        return inflater.inflate(R.layout.fragment_edit_food_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        nameInput = view.findViewById(R.id.foodNameInput);
        saveButton = view.findViewById(R.id.saveButton);

        foodItemViewModel = new ViewModelProvider(requireActivity()).get(FoodViewModel.class);

        // Check if an existing item was passed in (for editing)
        if (getArguments() != null && getArguments().containsKey("foodItem")) {
            existingItem = (FoodItem) getArguments().getSerializable("foodItem");
            nameInput.setText(existingItem.name);
        }

        saveButton.setOnClickListener(v -> {
            String newName = nameInput.getText().toString().trim();
            if (!newName.isEmpty()) {
                if (existingItem != null) {
                    existingItem.name = newName;
                    foodItemViewModel.update(existingItem);
                } else {
//                    FoodItem newItem = new FoodItem(newName, ...); // fill out the rest
//                    foodItemViewModel.insert(newItem);
                }
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}

