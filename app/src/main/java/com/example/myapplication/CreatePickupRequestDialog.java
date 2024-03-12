package com.example.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.models.Location;
import com.example.myapplication.models.OrderRequest;
import com.example.myapplication.models.RecycleBin;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

public class CreatePickupRequestDialog extends DialogFragment {

    private User currentUser;
    private TextInputEditText addressET, phoneET, fullNameET;
    private Button timeBT, dataBT;
    private Address address;
    int pickUpHour, pickUpMin;
    private final RecycleBin recycleBin;
    private long selectedDate = System.currentTimeMillis();
    private ProgressDialog progressDialog;


    ActivityResultLauncher<Intent> mapLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    (result) -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            address = result.getData().getParcelableExtra("address");
                            addressET.setText(address.getAddressLine(0).toString());
                        }
                    }
            );

    public CreatePickupRequestDialog(RecycleBin recycleBin, User currentUser) {
        super();
        this.recycleBin = recycleBin;
        this.currentUser = currentUser;
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

        fullNameET.setText(currentUser.getFullName());
        phoneET.setText(currentUser.getPhone());
        Button saveBT = view.findViewById(R.id.send_pickup);
        timeBT = view.findViewById(R.id.timeButton);
        ImageButton backBT = view.findViewById(R.id.btbackpickup);
        addressET.setOnClickListener(v -> openMapScreen());
        timeBT.setOnClickListener(v -> openTimePicker());
        dataBT = view.findViewById(R.id.date);
        dataBT.setOnClickListener(v -> openCalender());
        saveBT.setOnClickListener(v -> savePickUpRequest());
        backBT.setOnClickListener(v -> dismiss());
        return view;
    }

    private void openCalender() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog pickerDialog = new DatePickerDialog(this.getContext(), (view, y, m, d) -> {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            cal.setTimeInMillis(0);
            cal.set(Calendar.DAY_OF_MONTH, d);
            cal.set(Calendar.MONTH, m);
            cal.set(Calendar.YEAR, y);
            onDateSelected(cal.getTimeInMillis());
        }, year, month, day);

        pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        pickerDialog.show();
    }

    private void onDateSelected(long timeInMillis) {
        selectedDate = timeInMillis;
        dataBT.setText(DateUtils.dateToStr(selectedDate));
    }

    private void savePickUpRequest() {
        boolean isValidInput = true;
        if (addressET.getText().equals("")) {
            addressET.setError(getString(R.string.please_enter_your_address));
            isValidInput = false;
        }
        if (phoneET.getText().equals("")) {
            phoneET.setError(getString(R.string.please_enter_your_phone));
            isValidInput = false;
        }

        if (dataBT.getText().toString().contains("Select")) {
            dataBT.setError(getString(R.string.please_choose_pick_up_date));
            isValidInput = false;
        }
        if (timeBT.getText().toString().contains("Select")) {
            timeBT.setError(getString(R.string.please_choose_pick_up_date));
            isValidInput = false;
        }
        
        if (isValidInput) {
            createRequest();
        }
    }

    private void createRequest() {
        progressDialog = ProgressDialog.show(this.getContext(), "", getString(R.string.please_wait), true, false);
        OrderRequest orderRequest = new OrderRequest(UUID.randomUUID().toString(),selectedDate, currentUser.getId(),
                new Location(addressET.getText().toString(), address.getLatitude(), address.getLongitude())
        , pickUpHour, pickUpMin);
        DataBaseManager.createPickUpRequest(orderRequest, recycleBin, currentUser, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
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
                        timeBT.setText(hourOfDay + ":" + minute);
                    }
                }, pickUpHour, pickUpMin, true);
        timePickerDialog.show();
    }

    private void openMapScreen() {
        Intent intent = new Intent(this.getContext(), FindMyLocationActivity.class);
        mapLauncher.launch(intent);
    }


}
