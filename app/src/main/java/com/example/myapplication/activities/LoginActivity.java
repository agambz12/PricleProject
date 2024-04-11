package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.AlertDialogUtils;
import com.example.myapplication.R;
import com.example.myapplication.dialogfragments.ForgetPasswordDialog;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth;
    TextInputEditText passwordET, emailET;

    TextView forgetPasswordTV;
    ProgressDialog dialog;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            goToHomeScreen();
        }

        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        forgetPasswordTV = findViewById(R.id.forget_password);


        findViewById(R.id.login).setOnClickListener(view -> {
            onLoginButtonClicked();
        });

        findViewById(R.id.register).setOnClickListener(view -> {
            onRegisterButtonClicked();
        });

        findViewById(R.id.logonGuest).setOnClickListener(view->{
            Intent guest= new Intent(LoginActivity.this, HomeScreenActivity.class);
            startActivity(guest);
        });

        forgetPasswordTV.setOnClickListener(v -> forgetPassword());

    }

    private void forgetPassword() {
        new ForgetPasswordDialog().show(getSupportFragmentManager(), "");
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
        startActivity(intent);
        finish();
    }

    private void onLoginButtonClicked() {
        auth.signInWithEmailAndPassword(emailET.getText().toString(), passwordET.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
                            startActivity(intent);
                        } else {
                            AlertDialogUtils.showAlertDialog(LoginActivity.this, getString(R.string.error), task.getException().getMessage());
                        }
                    }
                });
    }

    private void onRegisterButtonClicked() {
        auth.createUserWithEmailAndPassword(emailET.getText().toString(), passwordET.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, CreateOrUpdateProfileActivity.class);
                    intent.putExtra(CreateOrUpdateProfileActivity.CREATE_PROFILE, true);
                    startActivity(intent);
                } else {
                    AlertDialogUtils.showAlertDialog(LoginActivity.this, getString(R.string.error), task.getException().getMessage());
                }
            }
        });
    }
}