package com.example.myapplication;

import com.example.myapplication.models.PickUpData;
import com.example.myapplication.models.PickUpRequest;
import com.example.myapplication.models.RecycleBin;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class DataBaseManager {

    private static final String USERS="users";
    private static final String RECYCLE_BINS="recyclebins";


    public static void createUser(User user, OnCompleteListener listener) {
        FirebaseFirestore
                .getInstance()
                .collection(USERS)
                .document(user.getId())
                .set(user)
                .addOnCompleteListener(listener);

    }

    public static void createRecycleBin(RecycleBin recycleBin, OnCompleteListener listener) {
        FirebaseFirestore
                .getInstance()
                .collection(RECYCLE_BINS)
                .document(recycleBin.getId())
                .set(recycleBin)
                .addOnCompleteListener(listener);
    }

    public static void getRecycleBins(OnCompleteListener<QuerySnapshot> listener) {
        FirebaseFirestore
                .getInstance()
                .collection(RECYCLE_BINS)
                .get()
                .addOnCompleteListener(listener);
    }

    public static void getUser(String uid, OnCompleteListener<DocumentSnapshot> listener) {
        FirebaseFirestore
                .getInstance()
                .collection(USERS)
                .document(uid)
                .get()
                .addOnCompleteListener(listener);
    }

    public static void createPickUpRequest(PickUpRequest pickUpRequest, RecycleBin recycleBin, OnCompleteListener<Void> listener) {
        recycleBin.getPickUpRequests().add(pickUpRequest);
        FirebaseFirestore
                .getInstance()
                .collection(RECYCLE_BINS)
                .document(recycleBin.getId())
                .set(recycleBin)
                .addOnCompleteListener(listener);
    }

    public static void updateRecycleBin(RecycleBin recycleBin) {
        FirebaseFirestore
                .getInstance()
                .collection(RECYCLE_BINS)
                .document(recycleBin.getId())
                .set(recycleBin);

    }

    public static void updatePickUpRequest(RecycleBin recycleBin) {
        DataBaseManager.updateRecycleBin(recycleBin);
        FirebaseFirestore
                .getInstance()
                .collection(USERS)
                .document(Session.currentUser.getId())
                .set(Session.currentUser);
    }
}
