package com.example.foodtracker.ui.adapter;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtracker.R;
import com.example.foodtracker.models.StorageLocation;

import java.util.List;

public class StorageLocationAdapter extends RecyclerView.Adapter<StorageLocationAdapter.LocationViewHolder> {

    public interface OnLocationClickListener {
        void onLocationClick(StorageLocation location);
    }

    private List<StorageLocation> locationList;
    private final OnLocationClickListener listener;

    public StorageLocationAdapter(List<StorageLocation> locationList, OnLocationClickListener listener) {
        this.locationList = locationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.storage_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        StorageLocation location = locationList.get(position);
        holder.locationNameText.setText(location.name);

        String countText = String.valueOf(location.itemCount);
        String suffix = location.itemCount == 1 ? " item" : " items";

        SpannableString spannable = new SpannableString(countText + suffix);
        spannable.setSpan(
                new StyleSpan(Typeface.BOLD), // Make the number bold
                0,
                countText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        holder.itemCountText.setText(spannable);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLocationClick(location);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void setLocationList(List<StorageLocation> newList) {
        this.locationList.clear();
        this.locationList.addAll(newList);
        notifyItemRangeChanged(0, locationList.size());
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView locationNameText;
        TextView itemCountText;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationNameText = itemView.findViewById(R.id.txt_location_name);
            itemCountText = itemView.findViewById(R.id.txt_item_count);
        }
    }
}

