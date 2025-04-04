package com.example.foodtracker.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
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
    private Button removeButton;
    private FoodViewModel foodItemViewModel;
    private FoodItem existingItem;

    public EditFoodItemFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_food_item, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameInput = view.findViewById(R.id.foodNameInput);
        saveButton = view.findViewById(R.id.btn_save);
        removeButton = view.findViewById(R.id.btn_remove);
        SeekBar amountSlider = view.findViewById(R.id.amountSlider);
        TextView amountLabel = view.findViewById(R.id.amountLabel);

        foodItemViewModel = new ViewModelProvider(requireActivity()).get(FoodViewModel.class);

        if (getArguments() != null && getArguments().containsKey("foodItem")) {
            existingItem = (FoodItem) getArguments().getSerializable("foodItem");
            nameInput.setText(existingItem.name);

            if (existingItem.originalQuantity > 0) {
                double percentLeft = (existingItem.currentQuantity / existingItem.originalQuantity) * 100.0;
                amountSlider.setProgress((int) percentLeft);
                amountLabel.setText("Amount left: " + (int) percentLeft + "%");
            }
        }

        amountSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                amountLabel.setText("Amount left: " + progress + "%");

                if (existingItem != null) {
                    existingItem.currentQuantity = (progress / 100.0) * existingItem.originalQuantity;
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        saveButton.setOnClickListener(v -> {
            String newName = nameInput.getText().toString().trim();
            if (!newName.isEmpty()) {
                if (existingItem != null) {
                    existingItem.name = newName;
                    foodItemViewModel.update(existingItem);
                }
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        removeButton.setOnClickListener(v -> {
            if (existingItem != null) {
                foodItemViewModel.delete(existingItem);

                // Delay the popBackStack just a bit to let LiveData updates settle
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (isAdded()) {
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                }, 150);
            }
        });
    }

}

