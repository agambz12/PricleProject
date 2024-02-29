package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.DateUtils;
import com.example.myapplication.R;
import com.example.myapplication.callbacks.OnItemClickListener;
import com.example.myapplication.models.PickUpData;

import java.util.List;

public class PickUpsRequestsAdapter extends RecyclerView.Adapter<PickUpsRequestsAdapter.PickUpRequestViewHolder> {

    private final boolean showApprove;
    private List<PickUpData> items;
    private OnItemClickListener listener;

    public PickUpsRequestsAdapter(List<PickUpData> items, boolean showApprove, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
        this.showApprove = showApprove;
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
        if (pickUpData.getCreated() != null) {
            Glide.with(holder.itemView.getContext()).load(pickUpData.getCreated().getImage()).centerCrop()
                    //.placeholder(R.drawable.loading_spinner)
                    .into(holder.image);
            holder.name.setText(pickUpData.getCreated().getFullName());
            holder.phone.setText(pickUpData.getCreated().getPhone());
            holder.name.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.VISIBLE);
            holder.phone.setVisibility(View.VISIBLE);
        } else {
            holder.image.setVisibility(View.GONE);
            holder.name.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
            holder.phone.setVisibility(View.GONE);
        }


        String min = pickUpData.getPickUpRequest().getMin() > 10 ?
                String.valueOf(pickUpData.getPickUpRequest().getMin()) : "0"+ pickUpData.getPickUpRequest().getMin();
        holder.time.setText(pickUpData.getPickUpRequest().getHour() + ":" + min);
        holder.date.setText(DateUtils.dateToStr(pickUpData.getPickUpRequest().getDate()));
        holder.approveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(position);
            }
        });
        holder.approveBT.setVisibility(showApprove ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class PickUpRequestViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView location, time, name, phone, date;
        Button approveBT;

        public PickUpRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            location = itemView.findViewById(R.id.location);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            date = itemView.findViewById(R.id.date);
            approveBT = itemView.findViewById(R.id.approve);

        }
    }
}
