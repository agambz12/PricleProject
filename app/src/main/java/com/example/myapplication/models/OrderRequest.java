package com.example.myapplication.models;

import java.io.Serializable;

public class OrderRequest implements Serializable {

    private String createdUserId, pickUpUserId, id;
    private Location location;
    private long date;
    private int hour, min;


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public OrderRequest(){}

    public OrderRequest(String id, long data , String createdUserId, Location location, int hour, int min) {
        this.id = id;
        this.createdUserId = createdUserId;
        this.location = location;
        this.hour = hour;
        this.min = min;
        this.date = data;
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getPickUpUserId() {
        return pickUpUserId;
    }

    public void setPickUpUserId(String pickUpUserId) {
        this.pickUpUserId = pickUpUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
