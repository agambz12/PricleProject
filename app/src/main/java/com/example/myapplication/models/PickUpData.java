package com.example.myapplication.models;

public class PickUpData {
    private PickUpRequest pickUpRequest;
    private User created;

    public PickUpRequest getPickUpRequest() {
        return pickUpRequest;
    }

    public void setPickUpRequest(PickUpRequest pickUpRequest) {
        this.pickUpRequest = pickUpRequest;
    }

    public User getCreated() {
        return created;
    }

    public void setCreated(User created) {
        this.created = created;
    }

    public PickUpData(PickUpRequest pickUpRequest, User created) {
        this.pickUpRequest = pickUpRequest;
        this.created = created;
    }
}
