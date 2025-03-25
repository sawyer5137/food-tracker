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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<FoodItem> foodList;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.getDefault());

    // Constructor
    public FoodAdapter(List<FoodItem> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the food_item.xml layout for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        // Get the current food item
        FoodItem foodItem = foodList.get(position);

        //Format date
        String formattedDate = sdf.format(foodItem.expirationDate);
        String daysLeft = foodItem.getDaysLeft();
        int textColor;

        //Create text with placeholders based on whether food is already expired
        String expirationText;
        if(Integer.parseInt(daysLeft) > 0) {
            expirationText = holder.itemView.getContext().getString(R.string.expires_on_text, formattedDate, daysLeft);
            textColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.dark_green);
        }else {
            daysLeft = (Integer.parseInt(daysLeft) * -1) + "";
            expirationText = holder.itemView.getContext().getString(R.string.expired_on_text, formattedDate, daysLeft);
            textColor = Color.RED;
        }

        // Set food name and expiration date
        holder.foodName.setText(foodItem.name);
        holder.expirationDate.setText(expirationText);
        holder.expirationDate.setTextColor(textColor);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void setFoodList(List<FoodItem> newList) {
        this.foodList.clear();
        this.foodList.addAll(newList);
        notifyItemRangeChanged(0, foodList.size());
    }

    // ViewHolder class holds references to views for each item
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
