package com.example.myapplication.dialogfragments;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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

import com.example.myapplication.AlertDialogUtils;
import com.example.myapplication.DataBaseManager;
import com.example.myapplication.R;
import com.example.myapplication.adapters.PickUpsRequestsAdapter;
import com.example.myapplication.alarmmanager.AlarmReceiver;
import com.example.myapplication.callbacks.OnItemClickListener;
import com.example.myapplication.models.PickUpData;
import com.example.myapplication.models.OrderRequest;
import com.example.myapplication.models.RecycleBin;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
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
        AlertDialogUtils.showOptionsAlertDialog(this.getContext(), getString(R.string.alarm),
                getString(R.string.are_you_sure), getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentUser.getMyOrdersPickUp().add(request);
                        recycleBin.getPickUpRequests().removeIf(new Predicate<OrderRequest>() {
                            @Override
                            public boolean test(OrderRequest orderRequest) {
                                return orderRequest.getId().equals(request.getId());
                            }
                        });
                        DataBaseManager.updatePickUpRequest(recycleBin, currentUser);
                        scheduleAlarm(currentUser, request);
                        pickUpsDataList.remove(position);
                        pickUpsRequestsAdapter.notifyItemRemoved(position);
                    }
                }, getString(R.string.cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

    }

    private void scheduleAlarm(User currentUser, OrderRequest request) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(request.getDate());
        calendar.add(Calendar.MINUTE, -10);
        Intent intent = new Intent(requireContext(), AlarmReceiver.class);
        intent.putExtra("name", currentUser.getFullName());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Set up the AlarmManager
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(ALARM_SERVICE);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

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
