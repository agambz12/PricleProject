package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

public class SplashActivity extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        countDownTimer.start();
    }

    CountDownTimer countDownTimer= new CountDownTimer(1000,100) {
        @Override
        public void onTick(long l) {
        }

        @Override
        public void onFinish() {
            intent= new Intent(SplashActivity.this,MainActivity.class);
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
                startActivity(intent);
                overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
            }

        }
    };
}
