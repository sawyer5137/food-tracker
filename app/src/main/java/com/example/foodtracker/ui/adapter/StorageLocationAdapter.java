package com.example.foodtracker.ui.adapter;

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

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationNameText = itemView.findViewById(R.id.locationNameText);
        }
    }
}
