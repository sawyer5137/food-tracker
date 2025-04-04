package com.example.foodtracker.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtracker.R;
import com.example.foodtracker.models.FoodItem;
import com.example.foodtracker.models.FoodItemWithLocation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    public interface OnFoodClickListener {
        void onFoodClick(FoodItem item);
    }

    private List<FoodItemWithLocation> foodList = new ArrayList<>();
    private final List<FoodItemWithLocation> fullList = new ArrayList<>();
    private final OnFoodClickListener listener;
    private final boolean showStorageLocation;


    private static final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.getDefault());


    public FoodAdapter(List<FoodItemWithLocation> foodList, OnFoodClickListener listener, boolean showStorageLocation) {
        this.listener = listener;
        this.showStorageLocation = showStorageLocation;
        setFoodList(foodList);
    }


    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItemWithLocation itemWithLocation = foodList.get(position);
        FoodItem foodItem = itemWithLocation.food;
        String locationName = itemWithLocation.getStorageLocationName();
        Context context = holder.itemView.getContext();

        // Set food name
        holder.foodName.setText(foodItem.name);

        // Set amount left text
        String amountStr = (foodItem.currentQuantity % 1 == 0)
                ? String.valueOf((int) foodItem.currentQuantity)
                : String.valueOf(foodItem.currentQuantity);

        holder.amountLeft.setText(amountStr + " " + foodItem.unit);

        // Set progress bar
        int percent = (int) ((foodItem.currentQuantity / foodItem.originalQuantity) * 100);
        holder.amountProgressBar.setProgress(percent);

        // Set progress bar color
        int progressColor;
        if (percent > 75) {
            progressColor = R.color.dark_green;
        } else if (percent > 25) {
            progressColor = R.color.orange;
        } else {
            progressColor = R.color.red;
        }
        holder.amountProgressBar.setProgressTintList(
                ColorStateList.valueOf(ContextCompat.getColor(context, progressColor))
        );

        // Show or hide storage location
        if (showStorageLocation) {
            holder.storageLocationLabel.setVisibility(View.VISIBLE);
            holder.storageLocationLabel.setText(locationName);
        } else {
            holder.storageLocationLabel.setVisibility(View.GONE);
        }

        // Expiration text and color
        int daysLeft = Integer.parseInt(foodItem.getDaysLeft());
        String expirationText;

        if (daysLeft >= 0) {
            expirationText = "Expires in " + daysLeft + (daysLeft == 1 ? " day" : " days");
        } else {
            daysLeft = -daysLeft;
            expirationText = "Expired " + daysLeft + (daysLeft == 1 ? " day ago" : " days ago");
        }

        holder.expirationText.setText(expirationText);

        int expirationColor;
        if (daysLeft > 3 && foodItem.expirationDate.after(new java.util.Date())) {
            expirationColor = R.color.dark_green;
        } else if (daysLeft >= 0 && foodItem.expirationDate.after(new java.util.Date())) {
            expirationColor = R.color.orange;
        } else {
            expirationColor = R.color.red;
        }

        holder.expirationText.setTextColor(ContextCompat.getColor(context, expirationColor));

        // Set food icon (static for now)
        holder.foodIcon.setText("🍔");

        // Click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFoodClick(foodItem);
            }
        });
    }


    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void setFoodList(List<FoodItemWithLocation> newList) {
        foodList.clear();
        fullList.clear();
        foodList.addAll(newList);
        fullList.addAll(newList);
        notifyDataSetChanged();
    }

    public void filter(String query) {
        query = query.toLowerCase(Locale.ROOT);
        foodList.clear();

        if (query.isEmpty()) {
            foodList.addAll(fullList);
        } else {
            for (FoodItemWithLocation item : fullList) {
                if (item.food.name.toLowerCase(Locale.ROOT).contains(query)) {
                    foodList.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        ProgressBar amountProgressBar;

        TextView foodIcon, foodName, expirationText, storageLocationLabel, amountLeft;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            amountProgressBar = itemView.findViewById(R.id.amountProgressBar);
            foodIcon = itemView.findViewById(R.id.foodIcon);
            foodName = itemView.findViewById(R.id.foodName);
            expirationText = itemView.findViewById(R.id.expirationText);
            storageLocationLabel = itemView.findViewById(R.id.storageLocationLabel);
            amountLeft = itemView.findViewById(R.id.amountLeft); // <-- 🔥 Add this
        }
    }

}
