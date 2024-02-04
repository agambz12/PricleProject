package com.example.myapplication.models;

public class RecycleBinData {
    private RecycleBin recycleBin;
    private float distance;

    public RecycleBinData(RecycleBin recycleBin, float distance) {
        this.recycleBin = recycleBin;
        this.distance = distance;
    }

    public RecycleBin getRecycleBin() {
        return recycleBin;
    }

    public void setRecycleBin(RecycleBin recycleBin) {
        this.recycleBin = recycleBin;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
