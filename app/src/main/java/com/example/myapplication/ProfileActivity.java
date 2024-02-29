package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.customviews.ProfileSection;
import com.example.myapplication.models.User;

public class ProfileActivity extends AppCompatActivity {

    private ProfileSection requestsPS, pickUpsPS, editPS;
    private ImageView profileIV;
    private TextView nameTV;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        currentUser = (User) getIntent().getSerializableExtra("user");

        requestsPS = findViewById(R.id.my_requests);
        requestsPS.setTitle(getString(R.string.my_created_orders));
        requestsPS.setOnClickListener(v-> openOrdersScreen());
        pickUpsPS = findViewById(R.id.my_pick_ups);
        pickUpsPS.setTitle(getString(R.string.my_pick_up));
        editPS = findViewById(R.id.edit_profile);
        editPS.setTitle(getString(R.string.edit_profile));
        editPS.setIcon(R.drawable.pen);

        nameTV = findViewById(R.id.name);
        nameTV.setText(currentUser.getFullName());
        profileIV = findViewById(R.id.profile);

        Glide.with(this).load(currentUser.getImage()).centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .into(profileIV);

    }

    private void openOrdersScreen() {
        Intent intent = new Intent(this, OrdersActivity.class);
        intent.putExtra("showCreated", true);
        intent.putExtra("user", currentUser);
        startActivity(intent);
    }
}