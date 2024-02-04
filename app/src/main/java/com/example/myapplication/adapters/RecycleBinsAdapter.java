package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.RecycleBin;
import com.example.myapplication.models.RecycleBinData;

import java.util.ArrayList;
import java.util.List;

public class RecycleBinsAdapter extends RecyclerView.Adapter<RecycleBinsAdapter.RecycleBinViewHolder> {

    private List<RecycleBinData> items = new ArrayList<>();

    public void setItems(List<RecycleBinData> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecycleBinsAdapter.RecycleBinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reyclerbin_item, parent, false);
        return new RecycleBinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleBinsAdapter.RecycleBinViewHolder holder, int position) {
        RecycleBin recycleBin = items.get(position).getRecycleBin();
        Glide.with(holder.itemView.getContext()).load(recycleBin.getImageUrl()).centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .into(holder.image);

        holder.type.setText(recycleBin.getType().name());
        holder.location.setText(recycleBin.getLocation().getAddress());
        holder.distance.setText(String.valueOf(items.get(position).getDistance()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class RecycleBinViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView location, type, distance;

        public RecycleBinViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            location = itemView.findViewById(R.id.location);
            type = itemView.findViewById(R.id.type);
            distance = itemView.findViewById(R.id.distance);
        }
    }
}
