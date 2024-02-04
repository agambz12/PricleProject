package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class AlertDialogUtils {

    public static void showAlertDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .show();

    }

    public static void showOptionsAlertDialog(Context context, String title,
                                                  String message, String positiveTitle,DialogInterface.OnClickListener positionListener,
                                                  String negativeTitle, DialogInterface.OnClickListener negativeListener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveTitle, positionListener)
                .setNegativeButton(negativeTitle, negativeListener)
                .show();
    }
}
