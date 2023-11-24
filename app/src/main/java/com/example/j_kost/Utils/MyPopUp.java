package com.example.j_kost.Utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.example.j_kost.R;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

public class MyPopUp {

    public static void showSuccessDialog(Context context, String title, String desc, OnDialogButtonClickListener listener){
        PopupDialog.getInstance(context)
                .setStyle(Styles.SUCCESS)
                .setHeading(title)
                .setDescription(desc)
                .setDismissButtonText("Oke")
                .setCancelable(false)
                .showDialog(listener);
    }

    public static void showErrorDialog(Context context, String title, String desc, OnDialogButtonClickListener listener){
        PopupDialog.getInstance(context)
                .setStyle(Styles.FAILED)
                .setHeading(title)
                .setDescription(desc)
                .setDismissButtonText("Oke")
                .setCancelable(false)
                .showDialog(listener);
    }

    public static void showAlertDialog(Context context, String title, String desc, OnDialogButtonClickListener listener){
        PopupDialog.getInstance(context)
                .setStyle(Styles.ALERT)
                .setHeading(title)
                .setDescription(desc)
                .setDismissButtonText("Oke")
                .setDismissButtonTextColor(R.color.white)
                .setCancelable(false)
                .showDialog(listener);
    }

    public static void showConfirmDialog(Context context,String title, String desc, OnDialogButtonClickListener listener) {

        PopupDialog.getInstance(context)
                .setStyle(Styles.IOS)
                .setHeading(title)
                .setDescription(desc)
                .setPositiveButtonText("Ya")
                .setPositiveButtonTextColor(android.R.color.holo_red_light)
                .setNegativeButtonText("Tidak")
                .setCancelable(false)
                .showDialog(listener);
    }


}
