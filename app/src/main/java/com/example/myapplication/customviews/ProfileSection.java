package com.example.myapplication.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class ProfileSection extends LinearLayout {

    private TextView title;
    private ImageView icon;

    public ProfileSection(Context context) {
        super(context);
        init();
    }

    public ProfileSection(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProfileSection(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ProfileSection(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.profile_section_layout, this);
        title = findViewById(R.id.title);
        icon = findViewById(R.id.icon);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setIcon(int icon) {
        this.icon.setImageResource(icon);
    }
}
