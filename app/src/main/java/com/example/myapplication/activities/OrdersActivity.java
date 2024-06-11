package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.DataBaseManager;
import com.example.myapplication.R;
import com.example.myapplication.adapters.PickUpsRequestsAdapter;
import com.example.myapplication.models.OrderRequest;
import com.example.myapplication.models.PickUpData;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private List<PickUpData> pickUpData = new ArrayList<>();
    private PickUpsRequestsAdapter adapter;
    private boolean showCreated;
    private User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        String title = getIntent().getStringExtra("title");
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        showCreated = (Boolean) getIntent().getBooleanExtra("showCreated", true);
        currentUser = (User)getIntent().getSerializableExtra("user");
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PickUpsRequestsAdapter(pickUpData, false, null);
        recyclerView.setAdapter(adapter);
        fetchData();

    }

    private void fetchData() {
        if (showCreated) {
            currentUser.getMyCreatedOrders().forEach(orderRequest -> {
                pickUpData.add(new PickUpData(orderRequest, null));
            });
            orderList();
            adapter.notifyDataSetChanged();
        } else {
            for (int i = 0; i < currentUser.getMyOrdersPickUp().size(); i++) {
                final OrderRequest request = currentUser.getMyOrdersPickUp().get(i);
                final int index = i;
                DataBaseManager.getUser(request.getCreatedUserId(), new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            pickUpData.add(new PickUpData(request, task.getResult().toObject(User.class)));
                            if (index == currentUser.getMyOrdersPickUp().size() -1) {
                                orderList();
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
            }
        }
    }

    private void orderList() {
        pickUpData.sort(new Comparator<PickUpData>() {
            @Override
            public int compare(PickUpData t1, PickUpData t2) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(t1.getPickUpRequest().getDate());
                calendar.set(Calendar.HOUR_OF_DAY, t1.getPickUpRequest().getHour());
                calendar.set(Calendar.MINUTE, t1.getPickUpRequest().getMin());
                long t1Millis = calendar.getTimeInMillis();
                calendar.setTimeInMillis(t2.getPickUpRequest().getDate());
                calendar.set(Calendar.HOUR_OF_DAY, t2.getPickUpRequest().getHour());
                calendar.set(Calendar.MINUTE, t2.getPickUpRequest().getMin());
                long t2Millis = calendar.getTimeInMillis();
                return Long.compare(t1Millis, t2Millis);
            }
        });
    }
}