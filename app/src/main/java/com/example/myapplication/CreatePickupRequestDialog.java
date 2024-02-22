package com.example.myapplication;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.models.Location;
import com.example.myapplication.models.PickUpRequest;
import com.example.myapplication.models.RecycleBin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.UUID;

public class CreatePickupRequestDialog extends DialogFragment {

    private TextInputEditText addressET, phoneET, fullNameET;
    private Button timeBT;
    private Address address;
    int pickUpHour, pickUpMin;
    private final RecycleBin recycleBin;


    ActivityResultLauncher<Intent> mapLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    (result) -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            address = result.getData().getParcelableExtra("address");
                            addressET.setText(address.getAddressLine(0).toString());
                        }
                    }
            );


    public CreatePickupRequestDialog(RecycleBin recycleBin) {
        super();
        this.recycleBin = recycleBin;
    }

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

        fullNameET.setText(Session.currentUser.getFirstName() + " " + Session.currentUser.getLastName());
        phoneET.setText(Session.currentUser.getPhone());
        Button saveBT = view.findViewById(R.id.send_pickup);
        timeBT = view.findViewById(R.id.timeButton);
        ImageButton backBT = view.findViewById(R.id.btbackpickup);
        addressET.setOnClickListener(v -> openMapScreen());
        timeBT.setOnClickListener(v -> openTimePicker());
        saveBT.setOnClickListener(v -> savePickUpRequest());
        backBT.setOnClickListener(v -> dismiss());
        return view;
    }

    private void savePickUpRequest() {
        boolean isValidInput = true;
        if (addressET.getText().equals("")) {
            addressET.setError(getString(R.string.please_enter_your_address));
            isValidInput= false;
        }
//        if (timeET.getText().equals("")) {
//            addressET.setError(getString(R.string.please_enter_your_pick_up_time));
//            isValidInput= false;
//        }
        
        if (isValidInput) {
            createRequest();
        }
        
    }

    private void createRequest() {
        PickUpRequest pickUpRequest = new PickUpRequest(UUID.randomUUID().toString(), Session.currentUser.getId(),
                new Location(addressET.getText().toString(), address.getLatitude(), address.getLongitude())
        , pickUpHour, pickUpMin);
        DataBaseManager.createPickUpRequest(pickUpRequest, recycleBin, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                CreatePickupRequestDialog.this.dismiss();
            }
        });
    }

    private void openTimePicker() {
        final Calendar c = Calendar.getInstance();
        pickUpHour = c.get(Calendar.HOUR_OF_DAY);
        pickUpMin = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        pickUpHour = hourOfDay;
                        pickUpMin = minute;
                        //timeET.setText(hourOfDay + ":" + minute);
                    }
                }, pickUpHour, pickUpMin, true);
        timePickerDialog.show();
    }

    private void openMapScreen() {
        Intent intent = new Intent(this.getContext(), FindMyLocationActivity.class);
        mapLauncher.launch(intent);
    }


}
