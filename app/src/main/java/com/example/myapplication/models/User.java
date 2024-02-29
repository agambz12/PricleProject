package com.example.myapplication.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String firstName;
    private String lastName;
    private String phone;
    private String image;
    private String email;
    private String id;
    private List<OrderRequest> myCreatedOrders = new ArrayList<>();
    private List<OrderRequest> myOrdersPickUp = new ArrayList<>();


    public User(String firstName, String lastName, String phone, String image, String email, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.image = image;
        this.email = email;
        this.id = id;
    }


    public List<OrderRequest> getMyCreatedOrders() {
        return myCreatedOrders;
    }

    public void setMyCreatedOrders(List<OrderRequest> myCreatedOrders) {
        this.myCreatedOrders = myCreatedOrders;
    }

    public List<OrderRequest> getMyOrdersPickUp() {
        return myOrdersPickUp;
    }

    public void setMyOrdersPickUp(List<OrderRequest> myOrdersPickUp) {
        this.myOrdersPickUp = myOrdersPickUp;
    }

    public User() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
