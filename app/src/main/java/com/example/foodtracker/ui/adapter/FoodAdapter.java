package com.example.foodtracker.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


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

        try{


        FoodItemWithLocation itemWithLocation = foodList.get(position);
        FoodItem foodItem = itemWithLocation.food;
        String locationName = itemWithLocation.getStorageLocationName();
        Context context = holder.itemView.getContext();

        // Set food name
        holder.foodName.setText(foodItem.name);

        // Set amount left text
        String amountStr;
        if (foodItem.currentQuantity % 1 == 0) {
            amountStr = String.valueOf((int) foodItem.currentQuantity);
        } else {
            amountStr = String.format(Locale.getDefault(), "%.2f", foodItem.currentQuantity);
        }

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
            int years = daysLeft / 365;
            int remainingDays = daysLeft % 365;

            if (years > 0) {
                expirationText = "Expires in " + years + (years == 1 ? " year" : " years");
                if (remainingDays > 0) {
                    expirationText += " " + remainingDays + (remainingDays == 1 ? " day" : " days");
                }
            } else {
                expirationText = "Expires in " + daysLeft + (daysLeft == 1 ? " day" : " days");
            }
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
        holder.foodIcon.setText(getEmojiForFoodName(foodItem.name));
        if(foodItem.isExpired()) holder.foodIcon.setText("\uD83E\uDD2E");

        // Click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFoodClick(foodItem);
            }
        });}catch(Exception e) {
            Log.e("ADAPTER_BIND", "Error binding view at position " + position, e);
        }

    }


    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void setFoodList(List<FoodItemWithLocation> list) {
        fullList.clear();
        fullList.addAll(list);
        foodList.clear();
        foodList.addAll(list);
        notifyDataSetChanged();
    }

    public void filter(String query) {
        Log.d("ADAPTER_FILTER", "Filtering with query: " + query);
        Log.d("ADAPTER_FILTER", "Filtered size: " + foodList.size());

        foodList.clear();

        if (query.isEmpty()) {
            foodList.addAll(fullList); // Restore full list
        } else {
            for (FoodItemWithLocation item : fullList) {
                if (item.food.name.toLowerCase().contains(query.toLowerCase())) {
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
            amountLeft = itemView.findViewById(R.id.amountLeft);
        }
    }

    public List<FoodItemWithLocation> getCurrentList() {
        return foodList;
    }
    private String getEmojiForFoodName(String name) {
        String lower = name.toLowerCase();
        String bestMatch = null;

        for (String key : emojiMap.keySet()) {
            if (lower.contains(key)) {
                // Choose the longest matching key
                if (bestMatch == null || key.length() > bestMatch.length()) {
                    bestMatch = key;
                }
            }
        }

        return bestMatch != null ? emojiMap.get(bestMatch) : "🍔";
    }

    private static final Map<String, String> emojiMap = new HashMap<>();
    static {
        emojiMap.put("beef", "🥩");
        emojiMap.put("ground beef", "🥩");
        emojiMap.put("pork", "🍖");
        emojiMap.put("turkey", "🦃");
        emojiMap.put("duck", "🦆");
        emojiMap.put("lamb", "🐑");
        emojiMap.put("sausage", "🌭");
        emojiMap.put("pasta", "🍝");
        emojiMap.put("macaroni", "🧀🍝");
        emojiMap.put("rice", "🍚");
        emojiMap.put("brown rice", "🍘");
        emojiMap.put("cereal", "🥣");
        emojiMap.put("granola", "🥣");
        emojiMap.put("oats", "🌾");
        emojiMap.put("yogurt", "🥣");
        emojiMap.put("juice", "🧃");
        emojiMap.put("butter", "🧈");
        emojiMap.put("chicken", "🍗");
        emojiMap.put("apple", "🍎");
        emojiMap.put("green apple", "🍏");
        emojiMap.put("banana", "🍌");
        emojiMap.put("grape", "🍇");
        emojiMap.put("melon", "🍈");
        emojiMap.put("watermelon", "🍉");
        emojiMap.put("tangerine", "🍊");
        emojiMap.put("lemon", "🍋");
        emojiMap.put("peach", "🍑");
        emojiMap.put("pineapple", "🍍");
        emojiMap.put("mango", "🥭");
        emojiMap.put("strawberry", "🍓");
        emojiMap.put("cherries", "🍒");
        emojiMap.put("cherry", "🍒");
        emojiMap.put("blueberries", "🫐");
        emojiMap.put("blueberry", "🫐");
        emojiMap.put("kiwi", "🥝");
        emojiMap.put("tomato", "🍅");
        emojiMap.put("coconut", "🥥");
        emojiMap.put("avocado", "🥑");
        emojiMap.put("broccoli", "🥦");
        emojiMap.put("carrot", "🥕");
        emojiMap.put("corn", "🌽");
        emojiMap.put("cucumber", "🥒");
        emojiMap.put("leafy greens", "🥬");
        emojiMap.put("lettuce", "🥬");
        emojiMap.put("garlic", "🧄");
        emojiMap.put("onion", "🧅");
        emojiMap.put("potato", "🥔");
        emojiMap.put("sweet potato", "🍠");
        emojiMap.put("bread", "🍞");
        emojiMap.put("croissant", "🥐");
        emojiMap.put("baguette", "🥖");
        emojiMap.put("flatbread", "🫓");
        emojiMap.put("pretzel", "🥨");
        emojiMap.put("cheese", "🧀");
        emojiMap.put("egg", "🥚");
        emojiMap.put("fried egg", "🍳");
        emojiMap.put("bacon", "🥓");
        emojiMap.put("pancake", "🥞");
        emojiMap.put("sandwich", "🥪");
        emojiMap.put("burger", "🍔");
        emojiMap.put("fries", "🍟");
        emojiMap.put("pizza", "🍕");
        emojiMap.put("hot dog", "🌭");
        emojiMap.put("hotdog", "🌭");
        emojiMap.put("taco", "🌮");
        emojiMap.put("burrito", "🌯");
        emojiMap.put("stuffed flatbread", "🥙");
        emojiMap.put("falafel", "🧆");
        emojiMap.put("poultry", "🍗");
        emojiMap.put("drumstick", "🍗");
        emojiMap.put("steak", "🥩");
        emojiMap.put("fish", "🐟");
        emojiMap.put("sushi", "🍣");
        emojiMap.put("bento", "🍱");
        emojiMap.put("curry", "🍛");
        emojiMap.put("ramen", "🍜");
        emojiMap.put("spaghetti", "🍝");
        emojiMap.put("noodle", "🍜");
        emojiMap.put("salad", "🥗");
        emojiMap.put("popcorn", "🍿");
        emojiMap.put("canned food", "🥫");
        emojiMap.put("chocolate", "🍫");
        emojiMap.put("cookie", "🍪");
        emojiMap.put("cake", "🍰");
        emojiMap.put("ice cream", "🍨");
        emojiMap.put("shaved ice", "🍧");
        emojiMap.put("doughnut", "🍩");
        emojiMap.put("lollipop", "🍭");
        emojiMap.put("custard", "🍮");
        emojiMap.put("honey", "🍯");
        emojiMap.put("milk", "🥛");
        emojiMap.put("coffee", "☕");
        emojiMap.put("tea", "🍵");
        emojiMap.put("soda", "🥤");
        emojiMap.put("beer", "🍺");
        emojiMap.put("wine", "🍷");
        emojiMap.put("tropical drink", "🍹");
        emojiMap.put("basket", "🧺");
        emojiMap.put("plate", "🍽️");

    }


}
