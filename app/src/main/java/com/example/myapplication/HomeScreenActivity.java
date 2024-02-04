package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;


public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton IMpaper;
    ImageButton IMpackaging;
    ImageButton IMglass;
    Button BTNpickup;
    Dialog glassDialog;
    Dialog paperDialog;
    Dialog packagingDialog;
    Dialog pickupDialog;
    ImageButton IMcloseGlass;
    ImageButton IMclosePackaging;
    ImageButton IMclosePaper;
    ImageButton IMclosepickup;
    ImageButton IMcontinuePaper;
    ImageButton IMcontinueGlass;
    ImageButton IMcontinuePackaging;
    Button timeButton, createRecycleBinBT;
    int hour,minute;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        IMpaper = (ImageButton) findViewById(R.id.btpaper);
        IMpaper.setOnClickListener(this);
        IMpackaging = (ImageButton) findViewById(R.id.btpackaging);
        IMpackaging.setOnClickListener(this);
        IMglass = (ImageButton) findViewById(R.id.btglass);
        IMglass.setOnClickListener(this);
        BTNpickup = (Button) findViewById(R.id.btpickup);
        BTNpickup.setOnClickListener(this);

        timeButton=findViewById(R.id.timeButton);
        createRecycleBinBT = findViewById(R.id.create_recycle_bin);
        createRecycleBinBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreenActivity.this, CreateRecycleBinActivity.class);
                startActivity(intent);
            }
        });
    }

    public void popTimePicker(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener= new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour=selectedHour;
                minute=selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };


        TimePickerDialog timePickerDialog= new TimePickerDialog(this,onTimeSetListener,hour,minute,true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }


    public void createPaperDialog() {
        paperDialog = new Dialog(this);
        paperDialog.setContentView(R.layout.paper_dialog);
        paperDialog.setCancelable(true);
        IMcontinuePaper = (ImageButton) paperDialog.findViewById(R.id.btGoPaper);
        IMcontinuePaper.setOnClickListener(this);
        IMclosePaper = (ImageButton) paperDialog.findViewById(R.id.btbackpaper);
        IMclosePaper.setOnClickListener(this);
        paperDialog.show();
    }

    public void createGlassDialog() {
        glassDialog = new Dialog(this);
        glassDialog.setContentView(R.layout.glass_dialog);
        glassDialog.setCancelable(true);
        IMcontinueGlass = (ImageButton) glassDialog.findViewById(R.id.btGoGlass);
        IMcontinueGlass.setOnClickListener(this);
        IMcloseGlass = (ImageButton) glassDialog.findViewById(R.id.btbackglass);
        IMcloseGlass.setOnClickListener(this);
        glassDialog.show();
    }


    public void createPackagingDialog() {
        packagingDialog = new Dialog(this);
        packagingDialog.setContentView(R.layout.packaging_dialog);
        packagingDialog.setCancelable(true);
        IMcontinuePackaging = (ImageButton) packagingDialog.findViewById(R.id.btGoPackaging);
        IMcontinuePackaging.setOnClickListener(this);
        IMclosePackaging = (ImageButton) packagingDialog.findViewById(R.id.btbackpackaging);
        IMclosePackaging.setOnClickListener(this);
        packagingDialog.show();
    }

    public void createPickupDialog() {
        pickupDialog = new Dialog(this);
        pickupDialog.setContentView(R.layout.pickup_dialog);
        pickupDialog.setCancelable(true);
        IMclosepickup = (ImageButton) pickupDialog.findViewById(R.id.btbackpickup);
        IMclosepickup.setOnClickListener(this);
        pickupDialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view == IMpaper) {
            createPaperDialog();
        } else if (view == IMcontinuePaper) {
            Intent intent = new Intent(HomeScreenActivity.this, MapActivity.class);
            startActivity(intent);
        } else if (view == IMclosePaper) {
            paperDialog.dismiss();
        } else if (view == IMglass) {
            createGlassDialog();
        } else if (view == IMcontinueGlass) {
            Intent intent= new Intent(HomeScreenActivity.this, MapActivity.class);
            startActivity(intent);
        } else if (view == IMcloseGlass) {
            glassDialog.dismiss();
        } else if (view == IMpackaging) {
            createPackagingDialog();
        } else if (view == IMcontinuePackaging) {
            Intent intent=new Intent(HomeScreenActivity.this, MapActivity.class);
            startActivity(intent);
        } else if (view == IMclosePackaging) {
            packagingDialog.dismiss();
        } else if (view == BTNpickup) {
            createPickupDialog();
        } else if (view == IMclosepickup) {
            pickupDialog.dismiss();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.exit_home) {
            showExitDialog();
            return true;
        } else if (item.getItemId() == R.id.sign_out) {
            onSignOutClicked();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSignOutClicked() {
        AlertDialogUtils.showOptionsAlertDialog(this, getString(R.string.alarm), getString(R.string.are_you_sure_sign_out),
                getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToMainActivity();
                    }
                }, getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    private void goToMainActivity() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Add this method to show the exit confirmation dialog
    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity(); // Exit the app
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // Dismiss the dialog, stay in the app
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}



