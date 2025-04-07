package com.example.foodtracker.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodtracker.R;
import com.example.foodtracker.models.FoodItem;
import com.example.foodtracker.viewmodel.FoodViewModel;

import java.util.Date;
import java.util.Locale;

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

            // Check if the food is incrementing (e.g. 3 bananas)
            if (existingItem.isIncrementing) {
                amountSlider.setMax((int) existingItem.originalQuantity);
                amountSlider.setKeyProgressIncrement(1);
                amountSlider.setProgress((int) existingItem.currentQuantity);
                amountLabel.setText("Amount left: " + (int) existingItem.currentQuantity + " " + existingItem.unit);

                amountSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        existingItem.currentQuantity = progress;
                        amountLabel.setText("Amount left: " + progress + " " + existingItem.unit);

                        if (progress == 0) {
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Delete Item?")
                                    .setMessage("This item is now empty. Do you want to remove it?")
                                    .setPositiveButton("Yes", (dialog, which) -> {
                                        foodItemViewModel.delete(existingItem);
                                        requireActivity().getSupportFragmentManager().popBackStack();
                                    })
                                    .setNegativeButton("No", (dialog, which) -> {
                                        seekBar.setProgress(1);
                                        existingItem.currentQuantity = 1;
                                        amountLabel.setText("Amount left: 1 " + existingItem.unit);
                                    })
                                    .show();
                        }
                    }

                    @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override public void onStopTrackingTouch(SeekBar seekBar) {}
                });

            } else {
                // Variable quantity (e.g. 1500ml of juice)
                amountSlider.setMax(100);
                int initialProgress = (int) ((existingItem.currentQuantity / existingItem.originalQuantity) * 100);
                amountSlider.setProgress(initialProgress);
                updateVariableLabel(amountLabel, initialProgress, existingItem);

                amountSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        existingItem.currentQuantity = (progress / 100.0) * existingItem.originalQuantity;
                        updateVariableLabel(amountLabel, progress, existingItem);
                    }

                    @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override public void onStopTrackingTouch(SeekBar seekBar) {}
                });
            }
        }

        saveButton.setOnClickListener(v -> {
            String newName = nameInput.getText().toString().trim();
            if (!newName.isEmpty()) {
                if (existingItem != null) {
                    existingItem.name = newName;
                    existingItem.lastModified = new Date();
                    foodItemViewModel.update(existingItem);
                }
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        removeButton.setOnClickListener(v -> {
            if (existingItem != null) {
                foodItemViewModel.delete(existingItem);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (isAdded()) {
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                }, 150);
            }
        });
    }

    // Helper for displaying variable quantity amounts
    private void updateVariableLabel(TextView label, int percent, FoodItem item) {
        double current = (percent / 100.0) * item.originalQuantity;
        String formatted = (current % 1 == 0)
                ? String.valueOf((int) current)
                : String.format(Locale.getDefault(), "%.1f", current);
        label.setText("Amount left: " + formatted + " " + item.unit);
    }
}
