package com.example.myapplication.models;

public class PickUpData {
    private OrderRequest orderRequest;
    private User created;

    public OrderRequest getPickUpRequest() {
        return orderRequest;
    }

    public void setPickUpRequest(OrderRequest orderRequest) {
        this.orderRequest = orderRequest;
    }

    public User getCreated() {
        return created;
    }

    public void setCreated(User created) {
        this.created = created;
    }

    public PickUpData(OrderRequest orderRequest, User created) {
        this.orderRequest = orderRequest;
        this.created = created;
    }
}
