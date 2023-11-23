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
    private static PopupDialog progressDialog;
    public static void showFailedDialog(Context context){
        PopupDialog.getInstance(context)
                .setStyle(Styles.FAILED)
                .setHeading("Oops")
                .setDescription("Sesi anda telah berakhir, silahkan login kembali")
                .setCancelable(false)
                .showDialog(new OnDialogButtonClickListener() {
                    @Override
                    public void onDismissClicked(Dialog dialog) {
                        super.onDismissClicked(dialog);
                    }
                });
    }

    public static void showSuccessDialog(Context context, OnDialogButtonClickListener listener){
        PopupDialog.getInstance(context)
                .setStyle(Styles.SUCCESS)
                .setHeading("Sukses")
                .setDescription("Data berhasil ditambahkan")
                .setDismissButtonText("Oke")
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
