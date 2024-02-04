package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{
    Button BTforgetpass;
    EditText ETemail;
    Button BTcontinue;
    ImageButton IMclose;
    Dialog forgetpassDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        BTforgetpass=(Button)findViewById(R.id.btnFp);
        BTforgetpass.setOnClickListener(this);


        findViewById(R.id.btnBackLogIn).setOnClickListener(view -> {
            Intent back = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(back);
        });
       findViewById(R.id.btnSubmitLogin).setOnClickListener(view->{
            Intent submitLogin=new Intent(LogInActivity.this, HomeScreenActivity.class);
            startActivity(submitLogin);
        });
    }
    public void createForgetpassDialog(){
        forgetpassDialog=new Dialog(this);
        forgetpassDialog.setContentView(R.layout.forgetpass_dialog);
        forgetpassDialog.setCancelable(true);
        ETemail=(EditText) forgetpassDialog.findViewById(R.id.etEmaildialog);
        BTcontinue=(Button) forgetpassDialog.findViewById(R.id.continuedialog);
        BTcontinue.setOnClickListener(this);
        IMclose=(ImageButton)forgetpassDialog.findViewById(R.id.btnBackforgetPass);
        IMclose.setOnClickListener(this);
        forgetpassDialog.show();
    }
    @Override
    public void onClick(View view){
        if (view==BTforgetpass){
            createForgetpassDialog();
        } else if (view==BTcontinue){
            forgetpassDialog.dismiss();
        } else if (view==IMclose){
            forgetpassDialog.dismiss();
        }
    }

}