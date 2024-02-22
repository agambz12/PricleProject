package com.example.myapplication.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.callbacks.OnItemClickListener;
import com.example.myapplication.models.PickUpData;

import java.util.ArrayList;
import java.util.List;

public class PickUpsRequestsAdapter extends RecyclerView.Adapter<PickUpsRequestsAdapter.PickUpRequestViewHolder> {

    private List<PickUpData> items;
    private OnItemClickListener listener;

    public PickUpsRequestsAdapter(List<PickUpData> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PickUpsRequestsAdapter.PickUpRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pick_up_requet, parent, false);
        return new PickUpRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PickUpsRequestsAdapter.PickUpRequestViewHolder holder, int position) {
        PickUpData pickUpData = items.get(position);
        Glide.with(holder.itemView.getContext()).load(pickUpData.getCreated().getImage()).centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .into(holder.image);

        holder.name.setText(pickUpData.getCreated().getFirstName() + " " +pickUpData.getCreated().getLastName());
        holder.location.setText(pickUpData.getPickUpRequest().getLocation().getAddress());
        holder.phone.setText(pickUpData.getCreated().getPhone());
        String min = pickUpData.getPickUpRequest().getMin() > 10 ?
                String.valueOf(pickUpData.getPickUpRequest().getMin()) : "0"+ pickUpData.getPickUpRequest().getMin();
        holder.time.setText(pickUpData.getPickUpRequest().getHour() + ":" + min);
        holder.approveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class PickUpRequestViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView location, time, name, phone;
        Button approveBT;

        public PickUpRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            location = itemView.findViewById(R.id.location);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            approveBT = itemView.findViewById(R.id.approve);

        }
    }
}
