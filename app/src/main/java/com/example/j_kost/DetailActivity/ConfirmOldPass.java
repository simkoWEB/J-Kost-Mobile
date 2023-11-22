package com.example.j_kost.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.j_kost.R;

public class ConfirmOldPass extends AppCompatActivity {
    Button btnCofirm;
    ImageView btnBack;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_old_pass);

        btnCofirm = findViewById(R.id.confirmBtn);
        btnBack = findViewById(R.id.btnBack);

        btnCofirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConfirmOldPass.this, DetailGantiPassword.class);
                startActivity(i);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelConfirmation();
            }
        });

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showCancelConfirmation();
    }

    private void showCancelConfirmation() {
        // Tampilkan alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);
        builder.setTitle("Konfirmasi!");
        builder.setMessage("Apakah Anda yakin ingin membatalkan perubahan?");

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish(); // Kembali ke layar sebelumnya
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                // Mengubah teks tombol menjadi huruf kecil
                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                if (positiveButton != null) {
                    positiveButton.setAllCaps(false);
                    positiveButton.setTextColor(getResources().getColor(android.R.color.holo_red_light)); // Mengubah warna teks
                }

                Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                if (negativeButton != null) {
                    negativeButton.setAllCaps(false);
                    negativeButton.setTextColor(getResources().getColor(R.color.black)); // Mengubah warna teks
                }
            }
        });

        alert.show();
    }

}