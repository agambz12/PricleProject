package com.example.myapplication.dialogfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.AlertDialogUtils;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordDialog extends DialogFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(container.getContext()).inflate(R.layout.forget_password_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputEditText emailET = view.findViewById(R.id.email);
        Button resetBt = view.findViewById(R.id.reset);
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        resetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailET.getText().toString().isEmpty()) {
                    emailET.setText(getString(R.string.please_enter_email));
                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(emailET.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(requireContext(), getString(R.string.reset_success), Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    } else {
                                        AlertDialogUtils.showAlertDialog(requireContext(), getString(R.string.error), task.getException().getMessage());
                                    }
                                }
                            });
                }
            }
        });
    }
}
