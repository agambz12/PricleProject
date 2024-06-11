package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.customviews.ProfileSection;
import com.example.myapplication.models.User;

public class ProfileActivity extends AppCompatActivity {

    private ProfileSection myRequestsPS, pickUpsPS, editPS;
    private ImageView profileIV;
    private TextView nameTV;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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

        currentUser = (User) getIntent().getSerializableExtra("user");
        myRequestsPS = findViewById(R.id.my_requests);
        myRequestsPS.setTitle(getString(R.string.my_created_orders));
        myRequestsPS.setIcon(R.drawable.baseline_send_24);
        pickUpsPS = findViewById(R.id.my_pick_ups);
        pickUpsPS.setTitle(getString(R.string.my_pick_up));
        pickUpsPS.setIcon(R.drawable.pick_ups_checklist_24);
        editPS = findViewById(R.id.edit_profile);
        editPS.setTitle(getString(R.string.edit_profile));
        editPS.setIcon(R.drawable.baseline_edit_24);

        nameTV = findViewById(R.id.name);
        nameTV.setText(getString(R.string.hello_name, currentUser.getFullName()));
        profileIV = findViewById(R.id.profile);

        Glide.with(this).load(currentUser.getImage()).centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .into(profileIV);

        myRequestsPS.setOnClickListener(v -> openOrdersScreen(true, myRequestsPS.getTitle()));
        pickUpsPS.setOnClickListener(v -> openOrdersScreen(false,  pickUpsPS.getTitle()));
        editPS.setOnClickListener(v -> goToEditProfileScreen());
    }

    private void goToEditProfileScreen() {
        Intent intent = new Intent(this, CreateOrUpdateProfileActivity.class);
        intent.putExtra("user", currentUser);
        startActivity(intent);
    }

    private void openOrdersScreen(boolean showCreated, String title) {
        Intent intent = new Intent(this, OrdersActivity.class);
        intent.putExtra("showCreated", showCreated);
        intent.putExtra("user", currentUser);
        intent.putExtra("title", title);
        startActivity(intent);
    }
}