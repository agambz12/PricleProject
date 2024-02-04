package com.example.myapplication;

import androidx.annotation.NonNull;

import com.example.myapplication.models.RecycleBin;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

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
}
