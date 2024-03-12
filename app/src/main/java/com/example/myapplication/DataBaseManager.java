package com.example.myapplication;

import androidx.annotation.Nullable;

import com.example.myapplication.models.OrderRequest;
import com.example.myapplication.models.RecycleBin;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

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

    public static void createPickUpRequest(OrderRequest orderRequest, RecycleBin recycleBin, User user, OnCompleteListener<Void> listener) {
        recycleBin.getPickUpRequests().add(orderRequest);
        FirebaseFirestore
                .getInstance()
                .collection(RECYCLE_BINS)
                .document(recycleBin.getId())
                .set(recycleBin)
                .addOnCompleteListener(listener);
        user.getMyCreatedOrders().add(orderRequest);
        FirebaseFirestore
                .getInstance()
                .collection(USERS)
                .document(orderRequest.getCreatedUserId())
                .set(user);

    }

    public static void updateRecycleBin(RecycleBin recycleBin) {
        FirebaseFirestore
                .getInstance()
                .collection(RECYCLE_BINS)
                .document(recycleBin.getId())
                .set(recycleBin);

    }

    public static void updatePickUpRequest(RecycleBin recycleBin, User currentUser) {
        DataBaseManager.updateRecycleBin(recycleBin);
        FirebaseFirestore
                .getInstance()
                .collection(USERS)
                .document(currentUser.getId())
                .set(currentUser);
    }

    public static void registerToUserChanges(User currentUser, EventListener<DocumentSnapshot> listener) {
        FirebaseFirestore
                .getInstance()
                .collection(USERS)
                .document(currentUser.getId())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        listener.onEvent(value, error);
                    }
                });
    }
}
