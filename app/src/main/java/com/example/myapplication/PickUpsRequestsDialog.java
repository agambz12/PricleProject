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
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapters.PickUpsRequestsAdapter;
import com.example.myapplication.callbacks.OnItemClickListener;
import com.example.myapplication.models.Location;
import com.example.myapplication.models.PickUpData;
import com.example.myapplication.models.PickUpRequest;
import com.example.myapplication.models.RecycleBin;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class PickUpsRequestsDialog extends DialogFragment {

    private RecyclerView recyclerView;
    private List<PickUpData> pickUpsDataList = new ArrayList<>();
    private PickUpsRequestsAdapter pickUpsRequestsAdapter;
    private final RecycleBin recycleBin;



    public PickUpsRequestsDialog(RecycleBin recycleBin) {
        super();
        this.recycleBin = recycleBin;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.recyclebin_requets_dialog, container, true);
        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pickUpsRequestsAdapter = new PickUpsRequestsAdapter(pickUpsDataList, new OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                PickUpsRequestsDialog.this.onItemClicked(position);
            }
        });
        recyclerView.setAdapter(pickUpsRequestsAdapter);
        ImageButton back = view.findViewById(R.id.back);
        back.setOnClickListener(v->dismiss());
        fetchData();
        return view;
    }

    private void onItemClicked(int position) {
        PickUpRequest request = pickUpsDataList.get(position).getPickUpRequest();
        Session.currentUser.getPickUpRequests().add(request);
        recycleBin.getPickUpRequests().removeIf(new Predicate<PickUpRequest>() {
            @Override
            public boolean test(PickUpRequest pickUpRequest) {
                return pickUpRequest.getId().equals(request.getId());
            }
        });
        DataBaseManager.updatePickUpRequest(recycleBin);
        pickUpsDataList.remove(position);
        pickUpsRequestsAdapter.notifyItemRemoved(position);
    }

    private void fetchData() {
        for (int i = 0; i < recycleBin.getPickUpRequests().size(); i++) {
            final PickUpRequest request = recycleBin.getPickUpRequests().get(i);
            final int index = i;
            DataBaseManager.getUser(request.getCreatedUserId(), new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        pickUpsDataList.add(new PickUpData(request, task.getResult().toObject(User.class)));
                        if (index == recycleBin.getPickUpRequests().size() -1) {
                            pickUpsRequestsAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }
}
