package com.example.foodtracker.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtracker.R;
import com.example.foodtracker.models.FoodItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    public interface OnFoodClickListener {
        void onFoodClick(FoodItem item);
    }

    private final OnFoodClickListener listener;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.getDefault());

    private List<FoodItem> foodList = new ArrayList<>();        // Current displayed list
    private final List<FoodItem> fullList = new ArrayList<>();  // Full original list (unfiltered)

    public FoodAdapter(List<FoodItem> foodList, OnFoodClickListener listener) {
        this.listener = listener;
        setFoodList(foodList); // initializes both foodList and fullList
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodList.get(position);

        String formattedDate = sdf.format(foodItem.expirationDate);
        String daysLeft = foodItem.getDaysLeft();
        int textColor;

        String expirationText;
        if (Integer.parseInt(daysLeft) > 0) {
            expirationText = holder.itemView.getContext().getString(R.string.expires_on_text, formattedDate, daysLeft);
            textColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.dark_green);
        } else {
            daysLeft = String.valueOf(-Integer.parseInt(daysLeft));
            expirationText = holder.itemView.getContext().getString(R.string.expired_on_text, formattedDate, daysLeft);
            textColor = Color.RED;
        }

        holder.foodName.setText(foodItem.name);
        holder.expirationDate.setText(expirationText);
        holder.expirationDate.setTextColor(textColor);

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

    public void setFoodList(List<FoodItem> newList) {
        this.foodList.clear();
        this.fullList.clear();
        this.foodList.addAll(newList);
        this.fullList.addAll(newList);
        notifyDataSetChanged();
    }

    public void filter(String query) {
        query = query.toLowerCase(Locale.ROOT);
        foodList.clear();

        if (query.isEmpty()) {
            foodList.addAll(fullList);
        } else {
            for (FoodItem item : fullList) {
                if (item.name.toLowerCase(Locale.ROOT).contains(query)) {
                    foodList.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodIcon, foodName, expirationDate;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodIcon = itemView.findViewById(R.id.foodIcon);
            foodName = itemView.findViewById(R.id.foodName);
            expirationDate = itemView.findViewById(R.id.expirationDate);
        }
    }
}
