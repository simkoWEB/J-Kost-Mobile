package com.example.j_kost.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.j_kost.Activity.MainActivity;
import com.example.j_kost.Fragment.ProfileFragment;
import com.example.j_kost.R;
import com.example.j_kost.Utils.MyPopUp;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

public class DetailGantiPassword extends AppCompatActivity {
    ImageView btnBack;
    Button btnSimpan;
    TextView currentPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ganti_password);

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelConfirm();
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showCancelConfirm();
    }

    private void showCancelConfirm(){
        MyPopUp.showConfirmDialog(DetailGantiPassword.this, "Konfirmasi!", "Apakah anda yakin ingin membatalkan ganti passwor?", new OnDialogButtonClickListener() {
            @Override
            public void onPositiveClicked(Dialog dialog) {
                super.onPositiveClicked(dialog);
                dialog.dismiss();
                // Kembali ke MainActivity (Activity yang meng-host ProfileFragment)
                Intent intent = new Intent(DetailGantiPassword.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Hapus semua activity di atasnya
                startActivity(intent);
                finish();
            }

            @Override
            public void onNegativeClicked(Dialog dialog) {
                super.onNegativeClicked(dialog);
                dialog.dismiss();
            }
        });
    }

}