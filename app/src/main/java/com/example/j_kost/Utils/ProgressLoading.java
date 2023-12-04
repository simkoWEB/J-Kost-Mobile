package com.example.j_kost.Utils;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressLoading {

    private ProgressDialog progressDialog;


    public void show(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hide() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
