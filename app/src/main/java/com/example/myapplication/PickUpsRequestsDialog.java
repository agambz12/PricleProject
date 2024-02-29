package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapters.PickUpsRequestsAdapter;
import com.example.myapplication.callbacks.OnItemClickListener;
import com.example.myapplication.models.PickUpData;
import com.example.myapplication.models.OrderRequest;
import com.example.myapplication.models.RecycleBin;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PickUpsRequestsDialog extends DialogFragment {

    private RecyclerView recyclerView;
    private List<PickUpData> pickUpsDataList = new ArrayList<>();
    private PickUpsRequestsAdapter pickUpsRequestsAdapter;
    private final RecycleBin recycleBin;
    private ProgressDialog progressDialog;
    private User currentUser;



    public PickUpsRequestsDialog(RecycleBin recycleBin, User currentUser) {
        super();
        this.recycleBin = recycleBin;
        this.currentUser = currentUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.recyclebin_requets_dialog, container, true);
        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pickUpsRequestsAdapter = new PickUpsRequestsAdapter(pickUpsDataList, true, new OnItemClickListener() {
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
        OrderRequest request = pickUpsDataList.get(position).getPickUpRequest();
        currentUser.getMyOrdersPickUp().add(request);
        recycleBin.getPickUpRequests().removeIf(new Predicate<OrderRequest>() {
            @Override
            public boolean test(OrderRequest orderRequest) {
                return orderRequest.getId().equals(request.getId());
            }
        });
        DataBaseManager.updatePickUpRequest(recycleBin, currentUser);
        pickUpsDataList.remove(position);
        pickUpsRequestsAdapter.notifyItemRemoved(position);
    }

    private void fetchData() {
        for (int i = 0; i < recycleBin.getPickUpRequests().size(); i++) {
            final OrderRequest request = recycleBin.getPickUpRequests().get(i);
            final int index = i;
            DataBaseManager.getUser(request.getCreatedUserId(), new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        pickUpsDataList.add(index, new PickUpData(request, task.getResult().toObject(User.class)));
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
