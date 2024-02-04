package com.example.myapplication.models;

public class RecycleBin {

    private String imageUrl;
    private Location location;
    private RecycleBinType type;
    private String id;

    public RecycleBinType getType() {
        return type;
    }

    public void setType(RecycleBinType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RecycleBin() {}

    public RecycleBin(String id, String imageUrl, Location location, RecycleBinType type) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.location = location;
        this.type = type;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
