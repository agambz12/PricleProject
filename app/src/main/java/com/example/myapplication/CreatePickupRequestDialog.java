package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

public class CreatePickupRequestDialog extends DialogFragment {

    private TextInputEditText addressET, phoneET, fullNameET;
    private Button saveBT;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.pickup_dialog, container, true);
        addressET = view.findViewById(R.id.address);
        phoneET = view.findViewById(R.id.phone);
        fullNameET = view.findViewById(R.id.full_name);
        saveBT = view.findViewById(R.id.send_pickup);
        return view;
    }
}
