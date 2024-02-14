package com.example.myapplication.models;

import java.io.Serializable;

public class Location implements Serializable {

    private String address;
    public Location(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {
    }

    public double latitude;
    public double longitude;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
