package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    TextInputEditText passwordET, emailET;
    ProgressDialog dialog;
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


        findViewById(R.id.login).setOnClickListener(view -> {
            onLoginButtonClicked();
        });

        findViewById(R.id.register).setOnClickListener(view -> {
            onRegisterButtonClicked();
        });

        findViewById(R.id.logonGuest).setOnClickListener(view->{
            Intent guest= new Intent(MainActivity.this, HomeScreenActivity.class);
            startActivity(guest);
        });

    }

    private void goToHomeScreen() {
        DataBaseManager.getSessionUser(auth.getCurrentUser().getUid(), new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Session.currentUser = task.getResult().toObject(User.class);
                    Intent intent = new Intent(MainActivity.this, HomeScreenActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    AlertDialogUtils.showAlertDialog(MainActivity.this, getString(R.string.error), task.getException().getMessage());
                }
            }
        });

    }

    private void onLoginButtonClicked() {
        auth.signInWithEmailAndPassword(emailET.getText().toString(), passwordET.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this, HomeScreenActivity.class);
                            startActivity(intent);
                        } else {
                            AlertDialogUtils.showAlertDialog(MainActivity.this, getString(R.string.error), task.getException().getMessage());
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
                    Intent intent = new Intent(MainActivity.this, CreateProfileActivity.class);
                    intent.putExtra(CreateProfileActivity.CREATE_PROFILE, true);
                    startActivity(intent);
                } else {
                    AlertDialogUtils.showAlertDialog(MainActivity.this, getString(R.string.error), task.getException().getMessage());
                }
            }
        });
    }
}