package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.DataBaseManager;
import com.example.myapplication.R;
import com.example.myapplication.adapters.RecycleBinsAdapter;
import com.example.myapplication.models.RecycleBin;
import com.example.myapplication.models.RecycleBinData;
import com.example.myapplication.models.RecycleBinType;
import com.example.myapplication.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class RecyclerBinListActivity extends AppCompatActivity {

    public static final String TYPE = "TYPE";
    private RecycleBinType type = RecycleBinType.GLASS;
    private FusedLocationProviderClient fusedLocationClient;
    private User currentUser;
    private Location myLocation;
    private RecycleBinsAdapter adapter;
    private LocationManager locationManager;
    private List<RecycleBinData> items = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        currentUser = (User)getIntent().getSerializableExtra("user");
        progressDialog = ProgressDialog.show(this, "", getString(R.string.please_wait), true, false);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        type = (RecycleBinType) getIntent().getSerializableExtra(TYPE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecycleBinsAdapter(this, currentUser);
        recyclerView.setAdapter(adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.find_your_recycle_bin);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.inflateMenu(R.menu.menu_home);
        findMyLocationIfHasAccess();

    }

    private void initializeItems(Task<QuerySnapshot> task) {
        task.getResult().forEach(new Consumer<QueryDocumentSnapshot>() {
            @Override
            public void accept(QueryDocumentSnapshot queryDocumentSnapshot) {
                RecycleBin recycleBin = queryDocumentSnapshot.toObject(RecycleBin.class);
                if (recycleBin.getType() == type) {
                    items.add(new RecycleBinData(recycleBin, getDistance(recycleBin)));
                }
            }
        });

        items.sort(new Comparator<RecycleBinData>() {
            @Override
            public int compare(RecycleBinData t1, RecycleBinData t2) {
                return Float.compare(t1.getDistance(), t2.getDistance());
            }
        });

        adapter.setItems(items);
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    private float getDistance(RecycleBin recycleBin) {
        float distance = 0;

        Location recycleBinLocation = new Location("location");
        recycleBinLocation.setLatitude(recycleBin.getLocation().latitude);
        recycleBinLocation.setLongitude(recycleBin.getLocation().longitude);
        if (this.myLocation != null) {
            distance = this.myLocation.distanceTo(recycleBinLocation) / 1000;
        }

        return distance;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findMyLocation();
            }
        }
    }

    private void findMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            fetchRecycleBins();
        } else {
            fetchRecycleBins();
        }
    }

    private void fetchRecycleBins() {
        DataBaseManager.getRecycleBins(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    initializeItems(task);
                }
            }
        });

    }

    private void findMyLocationIfHasAccess() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            findMyLocation();
        }
    }
}